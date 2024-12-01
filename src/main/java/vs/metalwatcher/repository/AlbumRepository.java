package vs.metalwatcher.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vs.metalwatcher.model.Album;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {

    @Query("select * from albums where band_id = :bandId")
    List<Album> findByBand(Long bandId);

    @Query("select * from albums where metal_archives_id = :metalArchivesId")
    Optional<Album> findByMetalArchivesId(Long metalArchivesId);

}
