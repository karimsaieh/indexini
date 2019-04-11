package tn.insat.pfe.searchservice.dtos;


import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;


public class FileGetDto extends FileIndexPayload {

    private float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
