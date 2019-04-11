package tn.insat.pfe.sparkmanagerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ISparkManagerService {
    boolean submitJob() throws JsonProcessingException;
}
