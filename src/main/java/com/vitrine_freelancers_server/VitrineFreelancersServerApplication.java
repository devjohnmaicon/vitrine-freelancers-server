package com.vitrine_freelancers_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VitrineFreelancersServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitrineFreelancersServerApplication.class, args);
	}

}
