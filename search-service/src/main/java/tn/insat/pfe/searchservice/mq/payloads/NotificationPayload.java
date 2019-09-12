package tn.insat.pfe.searchservice.mq.payloads;


public class NotificationPayload {
    private String event;
    private String fileUrl;
    private String fileName;

    public NotificationPayload() {
    }

    public NotificationPayload(String event, String fileUrl, String fileName) {
        this.event = event;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

