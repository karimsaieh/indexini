package tn.insat.pfe.sparkmanagerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.insat.pfe.sparkmanagerservice.dtos.*;
import tn.insat.pfe.sparkmanagerservice.entities.Job;
import tn.insat.pfe.sparkmanagerservice.repositories.IJobRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SparkManagerService implements ISparkManagerService {

    @Value("${pfe_livy_host}")
    private String livyHost;
    @Value("${pfe_tika_host}")
    private String tikaHost;
    @Value("${pfe_rabbitmq_host}")
    private String rabbitMqHost;
//    @Value("${pfe_env}")
//    private String env;
    @Value("${pfe_main_file}")
    private String mainFile;
    @Value("${pfe_livy_port}")
    private String livyPort;

    private final RestTemplate restTemplate;
    private final IJobRepository jobRepository;

    @Autowired
    public SparkManagerService(RestTemplate restTemplate,
                               IJobRepository jobRepository){
        this.restTemplate = restTemplate;
        this.jobRepository = jobRepository;
    }

    @Override
//    @HystrixCommand(fallbackMethod = "fallback")
    public boolean submitJob(SubmitJobDto submitJobDto) throws JsonProcessingException {
        // livy sessions are deleted after a while, so batches won't have batches from 2005
        String batchesUrl = "http://" + this.livyHost + ":" + this.livyPort + "/batches/";
        ResponseEntity<BatchesInfoDto> batchesResponse = this.restTemplate.getForEntity(batchesUrl, BatchesInfoDto.class);
        for (JobInfoDto jobInfoDto: batchesResponse.getBody().getSessions()) {
            if(Arrays.asList("starting","idle","busy", "shutting_down", "running").contains(jobInfoDto.getState())) {
                return false;
            }
        }
        Map conf = new HashMap<String, Object>();
//        conf.put("spark.yarn.appMasterEnv.pfe_rabbitmq_host", this.rabbitMqHost);// not needing cause i used client mode
        conf.put("spark.executorEnv.pfe_rabbitmq_host", this.rabbitMqHost);
        conf.put("spark.executorEnv.pfe_tika_host", this.tikaHost);
        long kmeansK = Math.round(Math.max(2.0, (submitJobDto.getSuggestionPrecision()/100.0) * (submitJobDto.getFiles()/2.0)));
        String[] args = new String []{String.valueOf(kmeansK), String.valueOf(submitJobDto.getTopicsNumber())};
//        conf.put("spark.executorEnv.pfe_env",this.env);
        JobRequestDto  jobRequestDto = new JobRequestDto(this.mainFile, conf, 1, 1, args);
        String url = "http://" + this.livyHost + ":" + this.livyPort + "/batches";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JobRequestDto> request = new HttpEntity<>(jobRequestDto, headers);
        this.restTemplate.postForEntity(url,request, JobInfoDto.class);
        this.jobRepository.save(new Job(new Date(), submitJobDto.getSuggestionPrecision(), submitJobDto.getTopicsNumber()));
        return true;
    }

    public boolean fallback(SubmitJobDto submitJobDto) throws JsonProcessingException {
        return false;
    }

    @Override
    public SparkStatsDto sparkStats(){
        SparkStatsDto sparkStatsDto = new SparkStatsDto();
        Job lastJob = this.jobRepository.findFirst1ByOrderByDateDesc();
        //should have used mapStruct
        sparkStatsDto.setCurrentSuggestionPrecision(lastJob.getSuggestionPrecision());
        sparkStatsDto.setCurrentTopicsNumber(lastJob.getTopicsNumber());
        sparkStatsDto.setLastJobDate(lastJob.getDate());
        sparkStatsDto.setNumberOfJobs(this.jobRepository.count());
        return sparkStatsDto;
    }

}
