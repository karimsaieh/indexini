package tn.insat.pfe.sparkmanagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class SparkManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkManagerServiceApplication.class, args);
	}

}
