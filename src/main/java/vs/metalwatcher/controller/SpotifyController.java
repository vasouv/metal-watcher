package vs.metalwatcher.controller;

import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import vs.metalwatcher.model.Album;
import vs.metalwatcher.service.SpotifyClient;

import java.util.List;

@RestController
@RequestMapping("spotify")
@CrossOrigin
public class SpotifyController {

    private final SpotifyClient spotifyClient;

    public SpotifyController(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("band/{id}")
    public Artist getArtist(@PathVariable("id") String id) {
        return spotifyClient.getArtist(id);
    }

    @GetMapping("band/{id}/albums")
    public List<Album> getAlbums(@PathVariable("id") String id) {
        return spotifyClient.getArtistsAlbums(id);
    }
}
