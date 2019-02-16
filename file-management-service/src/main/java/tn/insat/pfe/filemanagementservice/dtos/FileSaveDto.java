package tn.insat.pfe.filemanagementservice.dtos;

public class FileSaveDto {
    private String metadata;

    public FileSaveDto() {
    }

    public FileSaveDto(String metadata) {
        this.metadata = metadata;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
