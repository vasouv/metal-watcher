package vs.metalwatcher.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vs.metalwatcher.model.Band;

@Repository
public interface BandRepository extends CrudRepository<Band, Long> {
}
