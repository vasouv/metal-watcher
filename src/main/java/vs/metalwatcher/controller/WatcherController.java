package vs.metalwatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vs.metalwatcher.model.Band;
import vs.metalwatcher.repository.BandRepository;
import vs.metalwatcher.service.MetalApiService;
import vs.metalwatcher.service.UpdateDiscographyService;

import java.util.Optional;

@RestController
@RequestMapping("watcher")
public class WatcherController {

    private final BandRepository bandRepository;
    private final UpdateDiscographyService updateDiscographyService;

    public WatcherController(BandRepository bandRepository, UpdateDiscographyService updateDiscographyService) {
        this.bandRepository = bandRepository;
        this.updateDiscographyService = updateDiscographyService;
    }

    @GetMapping("bands")
    public Iterable<Band> fetchBands() {
        return bandRepository.findAll();
    }

    @GetMapping("bands/{id}")
    public ResponseEntity<Band> fetchBandById(@PathVariable Long id) {
        return bandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("update")
    public void update() {
        updateDiscographyService.update();
    }

}
