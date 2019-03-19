package tn.insat.pfe.searchservice.dtos;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class FileUpdateDto {
    @NotNull
    private String text;
    @NotNull
    private String summary;
    @NotNull
    private String thumbnailUrl;

    public FileUpdateDto(String text, String summary, String thumbnailUrl) {
        this.text = text;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
    }
    // use fatser xml object mapper instead
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("text", this.getText());
        map.put("summary", this.getSummary());
        map.put("thumbnailUrl", this.getThumbnailUrl());
        return map;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "FileUpdateDto{" +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
