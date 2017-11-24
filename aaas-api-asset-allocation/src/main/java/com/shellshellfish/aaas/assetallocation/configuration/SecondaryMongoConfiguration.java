package com.shellshellfish.aaas.assetallocation.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration																									
@EnableMongoRepositories(basePackages = "com.shellshellfish.aaas.finance.repository.dummy", mongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfiguration {

	// @Bean
	// public Mongo mongo(MongoProperties mongoProperties) throws Exception {
	// return new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
	// }
	//
	// @Bean
	// public MongoTemplate mongoTemplate(MongoProperties mongoProperties) throws
	// Exception {
	// return new MongoTemplate(mongo(mongoProperties),
	// mongoProperties.getDatabase());
	// }

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Bean(name = "secondaryMongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(new MongoClient(host, port), "test1");
	}
}