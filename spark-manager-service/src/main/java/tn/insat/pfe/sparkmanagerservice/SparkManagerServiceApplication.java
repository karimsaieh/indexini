package tn.insat.pfe.sparkmanagerservice;

import com.mongodb.MongoClient;
import io.jaegertracing.Configuration;
import io.opentracing.contrib.mongo.TracingMongoClient;
import io.opentracing.contrib.mongo.common.TracingCommandListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;

@EnableCircuitBreaker
@SpringBootApplication
public class SparkManagerServiceApplication {

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		io.opentracing.Tracer  tracer = Configuration.fromEnv().getTracer();

		return tracer;
	}

	public static void main(String[] args) {
		SpringApplication.run(SparkManagerServiceApplication.class, args);
	}

}
