package ch.michelneeser.trackr.model;

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

    public void addStatValue(StatValue statValue) {
        this.statValues.add(statValue);
    }

    @Override
    public String toString() {
        return "Stat{" +
                "id=" + id +
                ", token=" + token + "}";
    }

}