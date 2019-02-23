package tn.insat.pfe.searchservice.dtos;

public class FileGetDto {
    private String id;
    private String bulkSaveOperationUuid;
    private String text;
    private String summary;
    private String thumbnailUrl;
    private String bulkSaveOperationTimestamp;
    private String fileName;
    private float score;
    // private String worldCloudUrl;


    public FileGetDto(String id, String bulkSaveOperationUuid, String text, String summary, String thumbnailUrl, String bulkSaveOperationTimestamp, String fileName, float score) {
        this.id = id;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.text = text;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.fileName = fileName;
        this.score = score;
    }

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "FileGetDto{" +
                "id='" + id + '\'' +
                ", bulkSaveOperationUuid='" + bulkSaveOperationUuid + '\'' +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", bulkSaveOperationTimestamp='" + bulkSaveOperationTimestamp + '\'' +
                ", fileName='" + fileName + '\'' +
                ", score=" + score +
                '}';
    }
}
