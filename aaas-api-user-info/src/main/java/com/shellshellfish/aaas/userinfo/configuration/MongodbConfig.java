package com.shellshellfish.aaas.userinfo.configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.shellshellfish.aaas.userinfo.repositories.mongo"}, mongoTemplateRef = "mongoTemplate")
public class MongodbConfig {

	@Value("${spring.data.mongodb.host}")
	String host;

	@Value("${spring.data.mongodb.port}")
	int port;

	@Value("${spring.data.mongodb.database}")
	String database;
	
	// 连接到 mongodb 服务
    @Bean
    MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Bean
    @PostConstruct
    MongoDatabase mongoDatabase() {
        // 连接到数据库
        return mongoClient().getDatabase(database);
    }

	@Bean
	public MongoTemplate mongoTemplate(){

		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
				database);
		return mongoTemplate;

	}
}
