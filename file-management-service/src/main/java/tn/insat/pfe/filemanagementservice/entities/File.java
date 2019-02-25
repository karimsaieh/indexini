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
    @Column(nullable = false)
    private String bulkSaveOperationTimestamp;
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

    public File(String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) {
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

    public String getBulkSaveOperationUuid() {
        return bulkSaveOperationUuid;
    }

    public void setBulkSaveOperationUuid(String bulkSaveOperationUuid) {
        this.bulkSaveOperationUuid = bulkSaveOperationUuid;
    }

    public String getBulkSaveOperationTimestamp() {
        return bulkSaveOperationTimestamp;
    }

    public void setBulkSaveOperationTimestamp(String bulkSaveOperationTimestamp) {
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
