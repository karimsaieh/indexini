package tn.insat.pfe.sparkmanagerservice.dtos;

import java.util.Map;

public class JobInfoDto {
    private int id;
    private String state;
    private String appId;
    private Map appInfo;
    private String[] log;

    public JobInfoDto() {
    }

    public JobInfoDto(int id, String state, String appId, Map appInfo, String[] log) {
        this.id = id;
        this.state = state;
        this.appId = appId;
        this.appInfo = appInfo;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(Map appInfo) {
        this.appInfo = appInfo;
    }

    public String[] getLog() {
        return log;
    }

    public void setLog(String[] log) {
        this.log = log;
    }
}
