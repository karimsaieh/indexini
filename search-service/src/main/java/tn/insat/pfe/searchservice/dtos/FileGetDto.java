package tn.insat.pfe.searchservice.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileGetDto {

    private String id;
    private String bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;
    private String contentType;
    private String fileName;
    private String kmeansPrediction;
    private String bisectingKmeansPrediction;
    private Map<String, Double> ldaTopics;
    private Map<String,Integer> mostCommonWords;
    private String[] summary;
    private String text;
    private List<String> highlightList;
    private String thumbnail;
    private float score;

    public void setMostCommonWords(String mostCommonWords) throws IOException {
        this.mostCommonWords = (HashMap<String, Integer>) JsonUtils.jsonStringToObject(mostCommonWords, HashMap.class);
    }

    public ArrayList<ArrayList<Object>> getMostCommonWords() {
        ArrayList<ArrayList<Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : this.mostCommonWords.entrySet()) {
            ArrayList<Object> wordWithCount = new ArrayList<>();
            wordWithCount.add(entry.getKey());
            wordWithCount.add(entry.getValue());
            result.add(wordWithCount);
        }
        return result;
    }

    public ArrayList<Double> getLdaTopics() {
        ArrayList<Double> topicsList = new ArrayList<>();
        for (int i = 0; i < this.ldaTopics.size(); i++) {
            topicsList.add(0.0);
        }
        for (Map.Entry<String, Double> entry : this.ldaTopics.entrySet()) {
            topicsList.set(Integer.valueOf(entry.getKey()), entry.getValue());
        }
        return topicsList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<String> getHighlightList() {
        return highlightList;
    }

    public void setHighlightList(List<String> highlightList) {
        this.highlightList = highlightList;
    }

    public String getBisectingKmeansPrediction() {
        return bisectingKmeansPrediction;
    }

    public void setBisectingKmeansPrediction(String bisectingKmeansPrediction) {
        this.bisectingKmeansPrediction = bisectingKmeansPrediction;
    }


    public void setLdaTopics(Map<String, Double> ldaTopics) {
        this.ldaTopics = ldaTopics;
    }


    public String[] getSummary() {
        return summary;
    }

    public void setSummary(String[] summary) {
        this.summary = summary;
    }

//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
