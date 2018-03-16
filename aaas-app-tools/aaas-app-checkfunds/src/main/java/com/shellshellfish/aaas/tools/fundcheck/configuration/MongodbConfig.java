package com.shellshellfish.aaas.tools.fundcheck.configuration;

import com.mongodb.MongoClient;
import com.shellshellfish.aaas.tools.fundcheck.commons.BigDecimalToDoubleConverter;
import com.shellshellfish.aaas.tools.fundcheck.commons.DoubleToBigDecimalConverter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.shellshellfish.aaas.tools.fundcheck.repositories.mongo"})
public class MongodbConfig {

	@Value("${spring.data.mongodb.host}")
	String host;

	@Value("${spring.data.mongodb.port}")
	int port;

	@Value("${spring.data.mongodb.database}")
	String database;

	@Value("${spring.data.mongodb.toolsdb}")
	String toolsdb;

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
				database);

		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping
				.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
		mongoMapping.afterPropertiesSet();
		return mongoTemplate;
	}

	@Bean
	public MongoTemplate mongoToolsTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
				toolsdb);

		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping
				.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
		mongoMapping.afterPropertiesSet();
		return mongoTemplate;
	}
	/**
	 * Returns the list of custom converters that will be used by the MongoDB template
	 **/
	@Bean
	public CustomConversions customConversions() {
		return new CustomConversions(
				Arrays.asList(new DoubleToBigDecimalConverter(), new BigDecimalToDoubleConverter()));
	}
}
