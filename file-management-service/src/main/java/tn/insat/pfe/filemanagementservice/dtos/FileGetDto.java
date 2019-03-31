package tn.insat.pfe.filemanagementservice.dtos;

import java.util.Date;

public class FileGetDto {

    private boolean isIndexed;
    private String name;
    private Date createdAt;
    private String url;
    private String contentType;
    private Long bulkSaveOperationTimestamp;

    public FileGetDto() {
    }

    public FileGetDto(String name, Date createdAt, String url, String contentType, Long bulkSaveOperationTimestamp) {
        this.name = name;
        this.createdAt = createdAt;
        this.url = url;
        this.contentType = contentType;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }
}
