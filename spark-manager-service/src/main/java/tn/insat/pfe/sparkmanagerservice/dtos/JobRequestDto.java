package tn.insat.pfe.sparkmanagerservice.dtos;

import java.util.Map;

public class JobRequestDto {
    private String file;
    private Map conf;
    private int driverCores;
    private int executorCores;
    private String[] args;

    public JobRequestDto() {
    }

    public JobRequestDto(String file, Map conf, int driverCores, int executorCores, String[] args) {
        this.file = file;
        this.conf = conf;
        this.driverCores = driverCores;
        this.executorCores = executorCores;
        this.args = args;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Map getConf() {
        return conf;
    }

    public void setConf(Map conf) {
        this.conf = conf;
    }

    public int getDriverCores() {
        return driverCores;
    }

    public void setDriverCores(int driverCores) {
        this.driverCores = driverCores;
    }

    public int getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(int executorCores) {
        this.executorCores = executorCores;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
