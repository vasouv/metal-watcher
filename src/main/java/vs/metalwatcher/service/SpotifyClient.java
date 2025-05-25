package vs.metalwatcher.service;

import jakarta.annotation.PostConstruct;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import vs.metalwatcher.model.Album;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SpotifyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyClient.class);

    @Value("${spotify.client.id}")
    private String id;

    @Value("${spotify.client.secret}")
    private String secret;

    private SpotifyApi spotifyApi;
    private LocalDateTime tokenExpiresIn;

    @PostConstruct
    public void init() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(id)
                .setClientSecret(secret)
                .setRedirectUri(URI.create("http://localhost:8888"))
                .build();
        authorize();
    }

    private void authorize() {
        try {
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            tokenExpiresIn = LocalDateTime.now().plusSeconds(clientCredentials.getExpiresIn());
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException e) {
            LOGGER.error("Auth Error - IOException: {}", e.getMessage());
        } catch (SpotifyWebApiException e) {
            LOGGER.error("Auth Error - SpotifyWebApiException: {}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("Auth Error - ParseException: {}", e.getMessage());
        }
    }

    private void reauthorize() {
        if (isEmpty(tokenExpiresIn)) throw new RuntimeException("Client not authorized, cannot check token expiration");
        if (LocalDateTime.now().isBefore(tokenExpiresIn)) return;
        authorize();
    }

    public Artist getArtist(String artistLink) {
        Objects.requireNonNull(artistLink);

        reauthorize();

        GetArtistRequest artistRequest = spotifyApi.getArtist(artistLink).build();
        try {
            return artistRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            LOGGER.error("Get Artist Error - {}: {}", e.getClass(), e.getMessage());
            return null;
        }
    }

    public List<Album> getArtistsAlbums(String artistLink) {
        Objects.requireNonNull(artistLink);

        reauthorize();

        int limit = 50;
        int offset = 0;
        String next;

        List<AlbumSimplified> albums = new ArrayList<>();
        do {
            GetArtistsAlbumsRequest albumsRequest = spotifyApi.getArtistsAlbums(artistLink).limit(limit).offset(offset).build();
            Paging<AlbumSimplified> albumsPage;
            try {
                albumsPage = albumsRequest.execute();

                albums.addAll(Arrays.asList(albumsPage.getItems()));

                next = albumsPage.getNext();
                offset += limit + 1;
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                LOGGER.error("Get Albums Error - {}: {}", e.getClass(), e.getMessage());
                return Collections.emptyList();
            }

        } while (next != null);

        return albums.stream().map(this::convertAlbumSimplified).toList();
    }

    private Album convertAlbumSimplified(AlbumSimplified albumSimplified) {
        Objects.requireNonNull(albumSimplified);

        String band = Arrays.stream(albumSimplified.getArtists())
                .findFirst()
                .map(ArtistSimplified::getName)
                .orElse(null);
        String title = albumSimplified.getName();
        String releaseDate = albumSimplified.getReleaseDate();
        String albumType = albumSimplified.getAlbumType().name();
        String imageUrl = Arrays.stream(albumSimplified.getImages())
                .findFirst()
                .map(Image::getUrl)
                .orElse(null);

        return new Album(band, title, releaseDate, albumType, imageUrl);
    }

}
