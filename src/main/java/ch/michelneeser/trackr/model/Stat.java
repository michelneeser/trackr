package ch.michelneeser.trackr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    private Date createDate = new Date();
    private String name = StringUtils.EMPTY;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    @JsonProperty("values")
    public int getStatValueCount() {
        return statValues.size();
    }

    public List<StatValue> getStatValues() {
        return Collections.unmodifiableList(statValues);
    }

    public StatValue addStatValue(String statValue) {
        long id = statValues.size() + 1;
        StatValue newStatValue = new StatValue(id, statValue);
        statValues.add(newStatValue);
        return newStatValue;
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