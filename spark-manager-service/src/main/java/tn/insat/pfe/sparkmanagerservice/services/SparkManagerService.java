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
import tn.insat.pfe.sparkmanagerservice.dtos.JobInfoDto;
import tn.insat.pfe.sparkmanagerservice.dtos.JobRequestDto;

import javax.annotation.PostConstruct;
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

    // stateful variable, cause livy doesn't provide a methods that filters batches based on state
    // only one job at a time
    private int currentJobId;

    private final RestTemplate restTemplate;

    @PostConstruct
    public void init(){
        this.currentJobId = -1;
    }
    @Autowired
    public SparkManagerService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean submitJob() throws JsonProcessingException {
        if (this.currentJobId !=-1) {
            String url = "http://" + this.livyHost + ":" + this.livyPort + "/batches/" + this.currentJobId;
            // sometimes livy
            String currentJobState="";
            try{
                ResponseEntity<JobInfoDto> response = this.restTemplate.getForEntity(url,JobInfoDto.class);
                currentJobState = response.getBody().getState();
                //livy returns a 404 when the session is deleted
            }catch (HttpClientErrorException ex) {
                currentJobState = "a dummy value";
            }
            if ( !currentJobState.equals("success") && !currentJobState.equals("dead") && !currentJobState.equals("error") ){
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
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(jobRequestDto);
        String url = "http://" + this.livyHost + ":" + this.livyPort + "/batches";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        ResponseEntity<JobInfoDto> response = this.restTemplate.postForEntity(url,request, JobInfoDto.class);
        this.currentJobId =response.getBody().getId();
        return true;
    }
}
