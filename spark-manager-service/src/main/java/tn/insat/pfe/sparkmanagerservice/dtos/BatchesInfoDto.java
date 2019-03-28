package tn.insat.pfe.sparkmanagerservice.dtos;

public class BatchesInfoDto {
    private int from;
    private int total;
    private JobInfoDto[] sessions;

    public BatchesInfoDto() {
    }

    public BatchesInfoDto(int from, int total, JobInfoDto[] sessions) {
        this.from = from;
        this.total = total;
        this.sessions = sessions;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public JobInfoDto[] getSessions() {
        return sessions;
    }

    public void setSessions(JobInfoDto[] sessions) {
        this.sessions = sessions;
    }
}
