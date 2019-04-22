package tn.insat.pfe.sparkmanagerservice.dtos;

import java.util.Date;

public class SparkStatsDto {
    private long numberOfJobs;
    private Date lastJobDate;
    private int currentSuggestionPrecision;
    private int currentTopicsNumber;

    public SparkStatsDto(long numberOfJobs, Date lastJobDate, int currentSuggestionPrecision, int currentTopicsNumber) {
        this.numberOfJobs = numberOfJobs;
        this.lastJobDate = lastJobDate;
        this.currentSuggestionPrecision = currentSuggestionPrecision;
        this.currentTopicsNumber = currentTopicsNumber;
    }

    public SparkStatsDto() {
    }

    public long getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(long numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public Date getLastJobDate() {
        return lastJobDate;
    }

    public void setLastJobDate(Date lastJobDate) {
        this.lastJobDate = lastJobDate;
    }

    public int getCurrentSuggestionPrecision() {
        return currentSuggestionPrecision;
    }

    public void setCurrentSuggestionPrecision(int currentSuggestionPrecision) {
        this.currentSuggestionPrecision = currentSuggestionPrecision;
    }

    public int getCurrentTopicsNumber() {
        return currentTopicsNumber;
    }

    public void setCurrentTopicsNumber(int currentTopicsNumber) {
        this.currentTopicsNumber = currentTopicsNumber;
    }
}
