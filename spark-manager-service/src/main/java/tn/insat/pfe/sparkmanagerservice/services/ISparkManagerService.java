package tn.insat.pfe.sparkmanagerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.insat.pfe.sparkmanagerservice.dtos.SparkStatsDto;
import tn.insat.pfe.sparkmanagerservice.dtos.SubmitJobDto;

public interface ISparkManagerService {
    boolean submitJob(SubmitJobDto submitJobDto) throws JsonProcessingException;

    SparkStatsDto sparkStats();
}
