package tn.insat.pfe.filemanagementservice.mq.payloads;

public class FileDbUpdatePayload {

    private String location;
    private boolean isIndexed;

    public FileDbUpdatePayload() {
    }

    public FileDbUpdatePayload(String location, boolean isIndexed) {
        this.location = location;
        this.isIndexed = isIndexed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }
}
