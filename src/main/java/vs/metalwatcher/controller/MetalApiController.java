package vs.metalwatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vs.metalwatcher.archives.ArchivesAlbum;
import vs.metalwatcher.archives.ArchivesBand;
import vs.metalwatcher.service.MetalApiService;

import java.util.List;

@RestController
@RequestMapping("metalapi")
@CrossOrigin
public class MetalApiController {

    private final MetalApiService metalApiService;

    public MetalApiController(MetalApiService metalApiService) {
        this.metalApiService = metalApiService;
    }

    @GetMapping("band/{id}")
    public ResponseEntity<ArchivesBand> getBand(@PathVariable String id) {
        return ResponseEntity.ok(metalApiService.fetchBand(id));
    }

    @GetMapping("band/{id}/albums")
    public ResponseEntity<List<ArchivesAlbum>> getAlbums(@PathVariable String id) {
        return ResponseEntity.ok(metalApiService.fetchAlbums(id));
    }
}
