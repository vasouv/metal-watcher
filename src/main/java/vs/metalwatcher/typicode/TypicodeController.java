package vs.metalwatcher.typicode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("typicode")
public class TypicodeController {

    private final TypicodeService typicodeService;

    public TypicodeController(TypicodeService typicodeService) {
        this.typicodeService = typicodeService;
    }

    @GetMapping("users")
    public List<User> getUsers() {
        return typicodeService.findUsers();
    }
}
