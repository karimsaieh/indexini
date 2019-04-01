package tn.insat.pfe.filemanagementservice.dtos;

import java.util.Date;
import java.util.List;

public class FileGetDto {

    private boolean isIndexed;
    private String name;
    private String contentType;
    private Long bulkSaveOperationTimestamp;
    private List<String> metadata;

    public FileGetDto() {
    }

    public FileGetDto(boolean isIndexed, String name, String contentType, Long bulkSaveOperationTimestamp, List<String> metadata) {
        this.isIndexed = isIndexed;
        this.name = name;
        this.contentType = contentType;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.metadata = metadata;
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

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }
}
