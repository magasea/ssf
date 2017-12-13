package com.shellshellfish.aaas.finance.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration																									
@EnableMongoRepositories(basePackages = "com.shellshellfish.aaas.finance.repository.mongo3", mongoTemplateRef = "thirdMongoTemplate")
public class ThirdMongoConfiguration {

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(host, port);
	}

	@Bean(name = "thirdMongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(new MongoClient(host, port), "test001");
	}
}