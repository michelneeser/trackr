package ch.michelneeser.trackr.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    private Date createDate = new Date();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "stat_id"))
    private List<StatValue> statValues = new ArrayList<>();

    private Stat() {
    }

    public Stat(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getURL() {
        return "/stats/" + token;
    }

    public List<StatValue> getStatValues() {
        return Collections.unmodifiableList(statValues);
    }

    public long addStatValue(String statValue) {
        long id = statValues.size() + 1;
        statValues.add(new StatValue(id, statValue));
        return id;
    }

    public boolean deleteStatValue(long statValueId) {
        return statValues.removeIf(statValue -> statValue.getId() == statValueId);
    }

    public boolean isNumeric() {
        if (statValues.size() == 0) return false;
        return statValues.stream()
                .allMatch(statValue -> NumberUtils.isParsable(statValue.getValue()));
    }

    @Override
    public String toString() {
        return "Stat{" +
                "id=" + id +
                ", token=" + token + "}";
    }

}