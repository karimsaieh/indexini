package tn.insat.pfe.searchservice.dtos;

public class FileGetDto {
    private String id;
    private String bulkOperationUuid;
    private String text;
    private String summary;
    private String thumbnailUrl;
    private String timestamp;
    private String fileName;
    private float score;
    // private String worldCloudUrl;


    public FileGetDto(String id, String bulkOperationUuid, String text, String summary, String thumbnailUrl, String timestamp, String fileName, float score) {
        this.id = id;
        this.bulkOperationUuid = bulkOperationUuid;
        this.text = text;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.timestamp = timestamp;
        this.fileName = fileName;
        this.score = score;
    }

    public String getBulkOperationUuid() {
        return bulkOperationUuid;
    }

    public void setBulkOperationUuid(String bulkOperationUuid) {
        this.bulkOperationUuid = bulkOperationUuid;
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
                ", bulkOperationUuid='" + bulkOperationUuid + '\'' +
                ", text='" + text + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fileName='" + fileName + '\'' +
                ", score=" + score +
                '}';
    }
}
