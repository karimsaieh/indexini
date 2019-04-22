package tn.insat.pfe.sparkmanagerservice.endpoints.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.insat.pfe.sparkmanagerservice.dtos.SparkStatsDto;
import tn.insat.pfe.sparkmanagerservice.dtos.SubmitJobDto;
import tn.insat.pfe.sparkmanagerservice.services.ISparkManagerService;
import tn.insat.pfe.sparkmanagerservice.services.SparkManagerService;

@RestController
@RequestMapping("/v1/spark-manager")
@CrossOrigin(origins = "http://localhost:9527")
public class SparkManagerEndpoint {

    private final ISparkManagerService sparkManagerService;
    @Autowired
    public SparkManagerEndpoint(SparkManagerService sparkManagerService) {
        this.sparkManagerService =sparkManagerService;
    }

    @PostMapping(value = "/func/submitJob")
    public ResponseEntity<Boolean> submitJob(@RequestBody SubmitJobDto submitJobDto) throws JsonProcessingException {
        if(this.sparkManagerService.submitJob(submitJobDto)) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/func/sparkStats")
    public SparkStatsDto sparkStats(){
        return this.sparkManagerService.sparkStats();
    }

}
