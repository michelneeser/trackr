package ch.michelneeser.trackr.dao;

import ch.michelneeser.trackr.model.Stat;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByToken(String token);
    List<Stat> findByCreateDateLessThan(Date date);

}