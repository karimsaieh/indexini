package tn.insat.pfe.filemanagementservice.dtos;

public class FileTypesGetDto {
    private String contentType;
    private long count;

    public FileTypesGetDto(String contentType, long count) {
        this.contentType = contentType;
        this.count = count;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
