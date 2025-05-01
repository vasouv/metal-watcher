package vs.metalwatcher.service;

import jakarta.annotation.PostConstruct;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public List<AlbumSimplified> getArtistsAlbums(String artistLink) throws IOException, ParseException, SpotifyWebApiException {
        Objects.requireNonNull(artistLink);

        reauthorize();

        int limit = 50;
        int offset = 0;
        String next;

        List<AlbumSimplified> albumList = new ArrayList<>();
        do {
            GetArtistsAlbumsRequest albumsRequest = spotifyApi.getArtistsAlbums(artistLink).limit(limit).offset(offset).build();
            Paging<AlbumSimplified> albums = albumsRequest.execute();
            AlbumSimplified[] albumSimplifieds = albums.getItems();
            LOGGER.info("Retrieved {} albums from total {} with limit: {} and offset: {}", albumSimplifieds.length, albums.getTotal(), limit, offset);

            albumList.addAll(Arrays.asList(albumSimplifieds));

            next = albums.getNext();
            offset += limit + 1;
        } while (next != null);

        return albumList;
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void test() {
        String bathory = "6rBvjnvdsRxFRSrq1StGOM";
        try {
            List<AlbumSimplified> artistsAlbums = getArtistsAlbums(bathory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

}
