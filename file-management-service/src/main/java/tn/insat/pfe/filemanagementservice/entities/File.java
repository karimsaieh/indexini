package tn.insat.pfe.filemanagementservice.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class File implements Serializable {
    @Id
    private String id;
    private boolean isIndexed;
    private String location;
    private String name;
    private String contentType;
    private Long bulkSaveOperationTimestamp;
    private String bulkSaveOperationUuid;
    private Date createdAt;
    private Date modifiedAt;
    private boolean deleteFlag;

    private List<String> metadata;

    public File(String location, String name, String contentType, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid, List<String> metadata) {
        this.location = location;
        this.name = name;
        this.contentType = contentType;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
        this.metadata = metadata;
    }

    public File() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    public Long getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(Long bulkSaveOperationTimestamp) {
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }
}
