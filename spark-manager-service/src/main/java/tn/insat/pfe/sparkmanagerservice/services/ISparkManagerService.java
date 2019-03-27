package tn.insat.pfe.sparkmanagerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.insat.pfe.sparkmanagerservice.dtos.JobRequestDto;

public interface ISparkManagerService {
    boolean submitJob() throws JsonProcessingException;
}
