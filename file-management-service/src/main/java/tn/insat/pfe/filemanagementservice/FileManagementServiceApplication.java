package tn.insat.pfe.filemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class FileManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileManagementServiceApplication.class, args);
	}



}

