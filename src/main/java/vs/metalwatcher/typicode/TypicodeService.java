package vs.metalwatcher.typicode;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TypicodeService {

    private final RestClient restClient;

    public TypicodeService() {
        restClient = RestClient.create("https://jsonplaceholder.typicode.com");
    }

    public List<User> findUsers() {
        return restClient
                .get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
