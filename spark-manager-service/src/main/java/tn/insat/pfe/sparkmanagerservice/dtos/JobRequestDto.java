package tn.insat.pfe.sparkmanagerservice.dtos;

import java.util.Map;

public class JobRequestDto {
    private String file;
    private Map conf;

    public JobRequestDto() {
    }

    public JobRequestDto(String file, Map conf) {
        this.file = file;
        this.conf = conf;
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
}
