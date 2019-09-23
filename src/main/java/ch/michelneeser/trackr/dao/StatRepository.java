package ch.michelneeser.trackr.dao;

import ch.michelneeser.trackr.model.Stat;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByToken(String token);

}