package com.shellshellfish.aaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.shellshellfish.aaas.repositories")
public class MongoServiceApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(MongoServiceApplication.class, args);
	}
	

}
