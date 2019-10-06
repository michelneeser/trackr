package ch.michelneeser.trackr.service;

import ch.michelneeser.trackr.model.Stat;

import java.util.Optional;

public interface StatService {

    Stat create();
    Optional<Stat> get(String token);
    boolean delete(Stat stat, long statValueId);
    void delete(Stat stat);
    Stat save(Stat stat);

}