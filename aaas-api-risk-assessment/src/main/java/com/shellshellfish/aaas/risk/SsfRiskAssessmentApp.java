package com.shellshellfish.aaas.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.shellshellfish.aaas.risk.configuration.Properties;

@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableConfigurationProperties({ Properties.class })
public class SsfRiskAssessmentApp {

	public static void main(String[] args) {
		SpringApplication.run(SsfRiskAssessmentApp.class, args);
	}
}
