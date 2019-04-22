package tn.insat.pfe.sparkmanagerservice.dtos;

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
}
