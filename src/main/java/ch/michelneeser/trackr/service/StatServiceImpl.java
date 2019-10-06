package ch.michelneeser.trackr.service;

import ch.michelneeser.trackr.dao.StatRepository;
import ch.michelneeser.trackr.model.Stat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatRepository repo;

    @Override
    public Stat create() {
        Stat stat = new Stat(generateToken());
        return repo.save(stat);
    }

    @Override
    public Optional<Stat> get(String token) {
        return Optional.ofNullable(repo.findByToken(token));
    }

    @Override
    public boolean delete(Stat stat, long statValueId) {
        boolean deleteSuccessful = stat.deleteStatValue(statValueId);
        if (deleteSuccessful) {
            repo.save(stat);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Stat stat) {
        repo.delete(stat);
    }

    @Override
    public Stat save(Stat stat) {
        return repo.save(stat);
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
    }

}