package tn.insat.pfe.sparkmanagerservice.endpoints.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.insat.pfe.sparkmanagerservice.dtos.JobRequestDto;
import tn.insat.pfe.sparkmanagerservice.services.SparkManagerService;

@RestController
@RequestMapping("/v1/SparkManager")
public class SparkManagerEndpoint {

    private final SparkManagerService sparkManagerService;
    @Autowired
    public SparkManagerEndpoint(SparkManagerService sparkManagerService) {
        this.sparkManagerService =sparkManagerService;
    }

    @PostMapping(value = "/func/submitJob")
    public boolean submitJob() throws JsonProcessingException {
        return this.sparkManagerService.submitJob();
    }

}
