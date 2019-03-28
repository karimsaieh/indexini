package tn.insat.pfe.sparkmanagerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tn.insat.pfe.sparkmanagerservice.dtos.BatchesInfoDto;
import tn.insat.pfe.sparkmanagerservice.dtos.JobInfoDto;
import tn.insat.pfe.sparkmanagerservice.dtos.JobRequestDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class SparkManagerService implements ISparkManagerService {

    @Value("${LIVY_HOST}")
    private String livyHost;
    @Value("${TIKA_HOST}")
    private String tikaHost;
    @Value("${RABBITMQ_HOST}")
    private String rabbitMqHost;
    @Value("${ENV}")
    private String env;
    @Value("${MAIN_FILE}")
    private String mainFile;
    @Value("${LIVY_PORT}")
    private String livyPort;

    private final RestTemplate restTemplate;

    @Autowired
    public SparkManagerService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean submitJob() throws JsonProcessingException {
        // livy sessions are deleted after a while, so batches won't have batches from 2005
        String batches_url = "http://" + this.livyHost + ":" + this.livyPort + "/batches/";
        ResponseEntity<BatchesInfoDto> batches_response = this.restTemplate.getForEntity(batches_url, BatchesInfoDto.class);
        for (JobInfoDto jobInfoDto: batches_response.getBody().getSessions()) {
            if(Arrays.asList("starting","idle","busy", "shutting_down", "running").contains(jobInfoDto.getState())) {
                return false;
            }
        }
        Map conf = new HashMap<String, Object>();
        conf.put("spark.yarn.appMasterEnv.RABBITMQ_HOST", this.rabbitMqHost);
        conf.put("spark.executorEnv.RABBITMQ_HOST", this.rabbitMqHost);
        conf.put("spark.executorEnv.TIKA_HOST", this.tikaHost);
        conf.put("spark.executorEnv.ENV",this.env);
        conf.put("driverCores",1);
        conf.put("executorCores",2);
        JobRequestDto  jobRequestDto = new JobRequestDto(this.mainFile, conf);
        String url = "http://" + this.livyHost + ":" + this.livyPort + "/batches";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JobRequestDto> request = new HttpEntity<>(jobRequestDto, headers);
        this.restTemplate.postForEntity(url,request, JobInfoDto.class);
        return true;
    }
}
