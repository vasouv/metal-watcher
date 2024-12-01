package vs.metalwatcher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import vs.metalwatcher.archives.ArchivesAlbum;
import vs.metalwatcher.archives.ArchivesBand;

import java.util.*;

@Service
public class MetalApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetalApiService.class);

    private final RestClient restClient;

    private final Comparator<ArchivesAlbum> byYearDesc = (a1, a2) -> a2.year().compareTo(a1.year());

    public MetalApiService(@Value("${metal-api.url}") String metalApiUrl) {
        this.restClient = RestClient.create(metalApiUrl);
    }

    public List<ArchivesAlbum> fetchAlbums(String bandId) {
        Objects.requireNonNull(bandId);
        ArchivesBand archivesBand = fetchBand(bandId);
        return Optional.ofNullable(archivesBand)
                .map(ArchivesBand::albums)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(byYearDesc)
                .toList();
    }

    public ArchivesBand fetchBand(String bandId) {
        Objects.requireNonNull(bandId);
        return restClient
                .get()
                .uri("/bands/{id}", bandId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (resp, requ) -> LOGGER.error("Metal API Error"))
                .body(ArchivesBand.class);
    }

}
