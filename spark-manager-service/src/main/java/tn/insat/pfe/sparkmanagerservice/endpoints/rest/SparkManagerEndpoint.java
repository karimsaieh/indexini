package tn.insat.pfe.sparkmanagerservice.endpoints.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.insat.pfe.sparkmanagerservice.services.SparkManagerService;

@RestController
@RequestMapping("/v1/spark-manager")
public class SparkManagerEndpoint {

    private final SparkManagerService sparkManagerService;
    @Autowired
    public SparkManagerEndpoint(SparkManagerService sparkManagerService) {
        this.sparkManagerService =sparkManagerService;
    }

    @PostMapping(value = "/func/submitJob")
    public ResponseEntity<Boolean> submitJob() throws JsonProcessingException {
        if(this.sparkManagerService.submitJob()) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}