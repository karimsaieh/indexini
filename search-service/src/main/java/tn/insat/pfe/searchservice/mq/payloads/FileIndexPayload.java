package tn.insat.pfe.searchservice.mq.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class FileIndexPayload {
    @JsonProperty("id")
    private String id;
    @JsonProperty("bulkSaveOperationTimestamp")
    private String bulkSaveOperationTimestamp;
    @JsonProperty("bulkSaveOperationUuid")
    private String bulkSaveOperationUuid;
    @JsonProperty("contentType")
    private String contentType;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("kmeansPrediction")
    private String kmeansPrediction;
    @JsonProperty("bisectingKmeansPrediction")
    private String bisectingKmeansPrediction;
    @JsonProperty("ldaTopics")
    private Map<String, Double> ldaTopics;
    @JsonProperty("mostCommonWords")
    private String mostCommonWords;
    @JsonProperty("summary")
    private String[] summary;
    @JsonProperty("text")
    private String text;
    @JsonProperty("suggestionText")
    private String suggestionText;
    @JsonProperty("completionText")
    private String completionText;
    @JsonProperty("thumbnail")
    private String thumbnail;


    public Map toMap() {
        return new ObjectMapper().convertValue(this, Map.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKmeansPrediction() {
        return kmeansPrediction;
    }

    public void setKmeansPrediction(String kmeansPrediction) {
        this.kmeansPrediction = kmeansPrediction;
    }

    public String getBisectingKmeansPrediction() {
        return bisectingKmeansPrediction;
    }

    public void setBisectingKmeansPrediction(String bisectingKmeansPrediction) {
        this.bisectingKmeansPrediction = bisectingKmeansPrediction;
    }

    public Map<String, Double> getLdaTopics() {
        return ldaTopics;
    }

    public void setLdaTopics(Map<String, Double> ldaTopics) {
        this.ldaTopics = ldaTopics;
    }

    public String getMostCommonWords() {
        return mostCommonWords;
    }

    public void setMostCommonWords(String mostCommonWords) {
        this.mostCommonWords = mostCommonWords;
    }

    public String[] getSummary() {
        return summary;
    }

    public void setSummary(String[] summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getCompletionText() {
        return completionText;
    }

    public void setCompletionText(String completionText) {
        this.completionText = completionText;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "FileIndexPayload{" +
                "id='" + id + '\'' +
                ", bulkSaveOperationTimestamp='" + bulkSaveOperationTimestamp + '\'' +
                ", bulkSaveOperationUuid='" + bulkSaveOperationUuid + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", kmeansPrediction='" + kmeansPrediction + '\'' +
                ", bisectingKmeansPrediction='" + bisectingKmeansPrediction + '\'' +
                ", ldaTopics=" + ldaTopics +
                ", mostCommonWords=" + this.getMostCommonWords() +
                ", summary='" + summary + '\'' +
                ", text='" + text + '\'' +
                ", suggestionText='" + suggestionText + '\'' +
                ", completionText='" + completionText + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
