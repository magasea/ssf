package com.shellshellfish.aaas.zhongzhengapi.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.shellshellfish.aaas.zhongzhengapi.repositories")
public class MongoConfiguration {

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.database}")
	private String database;




	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(host, port);
	}

	@Bean

	@Qualifier("mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(new MongoClient(host, port), database);
	}


}