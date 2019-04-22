package tn.insat.pfe.sparkmanagerservice.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
    @Id
    private String id;
    private Date date;
    private int suggestionPrecision;
    private int topicsNumber;

    public Job() {
    }

    public Job(Date date, int suggestionPrecision, int topicsNumber) {
        this.id = id;
        this.date = date;
        this.suggestionPrecision = suggestionPrecision;
        this.topicsNumber = topicsNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSuggestionPrecision() {
        return suggestionPrecision;
    }

    public void setSuggestionPrecision(int suggestionPrecision) {
        this.suggestionPrecision = suggestionPrecision;
    }

    public int getTopicsNumber() {
        return topicsNumber;
    }

    public void setTopicsNumber(int topicsNumber) {
        this.topicsNumber = topicsNumber;
    }
}
