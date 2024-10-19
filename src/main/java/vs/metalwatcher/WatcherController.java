package vs.metalwatcher;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vs.metalwatcher.archives.Album;
import vs.metalwatcher.archives.Band;

import java.util.List;

@RestController
@RequestMapping("watcher")
public class WatcherController {

    private final MetalApiService metalApiService;

    public WatcherController(MetalApiService metalApiService) {
        this.metalApiService = metalApiService;
    }

    @GetMapping("band/{bandId}")
    public Band fetchBand(@PathVariable String bandId) {
        return metalApiService.fetchBand(bandId);
    }

    @GetMapping("albums/{bandId}")
    public List<Album> fetchAlbums(@PathVariable("bandId") String bandId) {
        return metalApiService.fetchAlbums(bandId);
    }
    
}
