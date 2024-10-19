package vs.metalwatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import vs.metalwatcher.archives.Album;
import vs.metalwatcher.archives.Band;

import java.util.*;

@Service
public class MetalApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetalApiService.class);

    private final RestClient restClient;

    private final Comparator<Album> byYearDesc = (a1, a2) -> a2.year().compareTo(a1.year());

    public MetalApiService(@Value("${metal-api.url}") String metalApiUrl) {
        this.restClient = RestClient.create(metalApiUrl);
    }

    public List<Album> fetchAlbums(String bandId) {
        Objects.requireNonNull(bandId);
        Band band = fetchBand(bandId);
        return Optional.ofNullable(band)
                .map(Band::albums)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(byYearDesc)
                .toList();
    }

    public Band fetchBand(String bandId) {
        Objects.requireNonNull(bandId);
        return restClient
                .get()
                .uri("/bands/{id}", bandId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (resp, requ) -> LOGGER.error("Metal API Error"))
                .body(Band.class);
    }

}
