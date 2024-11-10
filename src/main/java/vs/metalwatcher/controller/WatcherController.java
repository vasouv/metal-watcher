package vs.metalwatcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vs.metalwatcher.model.Band;
import vs.metalwatcher.repository.BandRepository;

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

}
