package tn.insat.pfe.filemanagementservice.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "files")
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private boolean isIndexed;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private Long bulkSaveOperationTimestamp;
    @Column(nullable = false)
    private String bulkSaveOperationUuid;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "modified_at")
    private Date modifiedAt;
    private boolean deleteFlag;

    @PrePersist
    protected void onCreate() {
        Date currentDate = new Date();
        this.createdAt = currentDate;
        this.modifiedAt = currentDate;
        this.isIndexed  = false;
        this.deleteFlag = false;
    }

    public File(String url, String name, String contentType, Long bulkSaveOperationTimestamp, String bulkSaveOperationUuid) {
        this.url = url;
        this.name = name;
        this.contentType = contentType;
        this.bulkSaveOperationTimestamp = bulkSaveOperationTimestamp;
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = new Date();
    }

    public File() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
