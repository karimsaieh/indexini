package tn.insat.pfe.searchservice.dtos;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class FileSaveDto {
    @NotNull
    private String bulkOperationUuid;
    @NotNull
    private String text;
    @NotNull
    private String summary;
    @NotNull
    private String thumbnailUrl;
    @NotNull
//    @Size(min = 13, max = 13)
//    @Digits(integer=13, fraction = 0)
    private String timestamp;
    @NotNull
    private String fileName;


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("bulkOperationUuid", this.getBulkOperationUuid());
        map.put("text", this.getText());
        map.put("summary", this.getSummary());
        map.put("thumbnailUrl", this.getThumbnailUrl());
        map.put("fileName", this.getFileName());
        map.put("timestamp", this.getTimestamp());
        return map;
    }


    public FileSaveDto(@NotNull String bulkOperationUuid, @NotNull String text, @NotNull String summary, @NotNull String thumbnailUrl, @NotNull String timestamp, @NotNull String fileName) {
        this.bulkOperationUuid = bulkOperationUuid;
        this.text = text;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.timestamp = timestamp;
        this.fileName = fileName;
    }

    public String getBulkOperationUuid() {
        return bulkOperationUuid;
    }

    public void setBulkOperationUuid(String bulkOperationUuid) {
        this.bulkOperationUuid = bulkOperationUuid;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileSaveDto{" +
                "bulkOperationUuid='" + bulkOperationUuid + '\'' +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
