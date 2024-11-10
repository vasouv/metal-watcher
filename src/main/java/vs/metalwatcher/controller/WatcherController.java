package vs.metalwatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vs.metalwatcher.model.Band;
import vs.metalwatcher.repository.BandRepository;

import java.util.Optional;

@RestController
@RequestMapping("watcher")
public class WatcherController {

    private final BandRepository bandRepository;

    public WatcherController(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
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

}
