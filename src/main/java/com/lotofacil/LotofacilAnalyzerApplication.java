package com.lotofacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Import and enable Async

@SpringBootApplication
@EnableAsync // Enable asynchronous processing
public class LotofacilAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotofacilAnalyzerApplication.class, args);
	}

}
