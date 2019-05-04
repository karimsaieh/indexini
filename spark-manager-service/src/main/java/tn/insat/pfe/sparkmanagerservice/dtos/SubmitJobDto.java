package tn.insat.pfe.sparkmanagerservice.dtos;

import java.util.Objects;

public class SubmitJobDto {

    private int suggestionPrecision;
    private int topicsNumber;
    private int files;

    public SubmitJobDto() {
    }

    public SubmitJobDto(int suggestionPrecision, int topicsNumber, int files) {
        this.suggestionPrecision = suggestionPrecision;
        this.topicsNumber = topicsNumber;
        this.files = files;
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

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitJobDto that = (SubmitJobDto) o;
        return suggestionPrecision == that.suggestionPrecision &&
                topicsNumber == that.topicsNumber &&
                files == that.files;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestionPrecision, topicsNumber, files);
    }
}
