package ch.michelneeser.trackr.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Embeddable
public class StatValue {

    private long id;
    private String value;
    private Date createDate = new Date();

    private StatValue() {
    }

    public StatValue(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Date getCreateDate() {
        return createDate;
    }

}