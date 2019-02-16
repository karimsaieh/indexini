package tn.insat.pfe.filemanagementservice.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "files")
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private boolean isIndexed;
    @Column(unique = true, nullable = false, length = 255)
    private String name;
    //don't know them yet,
    private String metadata;
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

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = new Date();
    }

    public File() {
    }

    public File(long id, boolean isIndexed, String name, String metadata, Date createdAt, Date modifiedAt, boolean deleteFlag) {
        this.id = id;
        this.isIndexed = isIndexed;
        this.name = name;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deleteFlag = deleteFlag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", isIndexed=" + isIndexed +
                ", name='" + name + '\'' +
                ", metadata='" + metadata + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", deleteFlag=" + deleteFlag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return getId() == file.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
