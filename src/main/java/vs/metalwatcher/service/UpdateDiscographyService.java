package vs.metalwatcher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import vs.metalwatcher.archives.ArchivesAlbum;
import vs.metalwatcher.model.Album;
import vs.metalwatcher.model.Band;
import vs.metalwatcher.repository.AlbumRepository;
import vs.metalwatcher.repository.BandRepository;

import java.util.List;

@Service
public class UpdateDiscographyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDiscographyService.class);

    private final BandRepository bandRepository;
    private final AlbumRepository albumRepository;
    private final MetalApiService metalApiService;

    public UpdateDiscographyService(BandRepository bandRepository,
                                    AlbumRepository albumRepository,
                                    MetalApiService metalApiService) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.metalApiService = metalApiService;
    }

    public void update() {
        LOGGER.info("--- Updating Discography ---");

        Iterable<Band> bands = bandRepository.findAll();

        bands.forEach(band -> {
            List<ArchivesAlbum> archivesAlbums = metalApiService.fetchAlbums(String.valueOf(band.getMetalArchivesId()));
            for (ArchivesAlbum maAlbum : archivesAlbums) {
                AggregateReference<Band, Long> bandAggregate = AggregateReference.to(band.getId());
                Album album = albumRepository.findByMetalArchivesId(Long.parseLong(maAlbum.id()))
                        .map(foundAlbum -> {
                            foundAlbum.setName(maAlbum.name());
                            foundAlbum.setYear(maAlbum.year());
                            foundAlbum.setType(maAlbum.type());
                            foundAlbum.setMetalArchivesLink(maAlbum.link());
                            return foundAlbum;
                        })
                        .orElse(new Album(maAlbum, bandAggregate));
                albumRepository.save(album);
            }
            LOGGER.info("Updated {} with {} albums", band.getName(), archivesAlbums.size());
        });

        LOGGER.info("--- Finished Updating Discography ---");
    }
}
