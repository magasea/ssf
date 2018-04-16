package com.shellshellfish.aaas.datamanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.shellshellfish.aaas.datamanager"})
public class DataManagerServiceApplication {

	public static void main(String[] args) {
	     SpringApplication.run(DataManagerServiceApplication.class, args);
	}
	
	
     	
}
