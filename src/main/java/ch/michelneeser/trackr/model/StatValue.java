package ch.michelneeser.trackr.model;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class StatValue {

    private String value;
    private Date createDate = new Date();

    private StatValue() {
    }

    public StatValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Date getCreateDate() {
        return createDate;
    }

}