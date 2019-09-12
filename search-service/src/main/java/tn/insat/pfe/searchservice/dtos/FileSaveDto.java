package tn.insat.pfe.searchservice.dtos;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class FileSaveDto {
    @NotNull
    private String bulkSaveOperationUuid;
    @NotNull
    private String text;
    @NotNull
    private String summary;
    @NotNull
    private String thumbnailUrl;
    @NotNull
//    @Size(min = 13, max = 13)
//    @Digits(integer=13, fraction = 0)
    private String bulkSaveOperationTimestamp;
    @NotNull
    private String fileName;


    // use fatser xml object mapper instead
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("bulkSaveOperationUuid", this.getBulkSaveOperationUuid());
        map.put("text", this.getText());
        map.put("summary", this.getSummary());
        map.put("thumbnailUrl", this.getThumbnailUrl());
        map.put("fileName", this.getFileName());
        map.put("bulkSaveOperationTimestamp", this.getBulkSaveOperationTimestamp());
        return map;
    }


    public FileSaveDto(@NotNull String bulkSaveOperationUuid, @NotNull String text, @NotNull String summary, @NotNull String thumbnailUrl, @NotNull String bulkSaveOperationTimestamp, @NotNull String fileName) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.text = text;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.fileName = fileName;
    }

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
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

    public String getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
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
                "bulkSaveOperationUuid='" + bulkSaveOperationUuid + '\'' +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", bulkSaveOperationTimestamp='" + bulkSaveOperationTimestamp + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
