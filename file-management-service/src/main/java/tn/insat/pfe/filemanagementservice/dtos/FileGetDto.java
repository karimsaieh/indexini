package tn.insat.pfe.filemanagementservice.dtos;

public class FileGetDto {

    private long id;
    private boolean isIndexed;
    private String metadata;
    private String name;
    private String modifiedAt;
    // ADD
    // last modified
    // created at

    public FileGetDto() {
    }

    public FileGetDto(long id, boolean isIndexed, String metadata, String name, String modifiedAt) {
        this.id = id;
        this.isIndexed = isIndexed;
        this.metadata = metadata;
        this.name = name;
        this.modifiedAt = modifiedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
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

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
