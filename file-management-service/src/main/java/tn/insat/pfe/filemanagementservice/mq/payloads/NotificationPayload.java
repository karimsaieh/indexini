package tn.insat.pfe.filemanagementservice.mq.payloads;

public class NotificationPayload {
    private String changeMe;

    public NotificationPayload() {
    }

    public NotificationPayload(String changeMe) {
        this.changeMe = changeMe;
    }

    public String getChangeMe() {
        return changeMe;
    }

    public void setChangeMe(String changeMe) {
        this.changeMe = changeMe;
    }
}
