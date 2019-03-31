package tn.insat.pfe.searchservice.mq.payloads;

public class FileDeletePayload {
    private String deleteBy;
    private String value;

    public FileDeletePayload() {
    }

    public FileDeletePayload(String deleteBy, String value) {
        this.deleteBy = deleteBy;
        this.value = value;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FileDeletePayload{" +
                "deleteBy='" + deleteBy + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

