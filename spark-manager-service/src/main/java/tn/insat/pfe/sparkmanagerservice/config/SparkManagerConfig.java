package tn.insat.pfe.sparkmanagerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SparkManagerConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
