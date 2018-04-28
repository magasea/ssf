package com.shellshellfish.aaas.userinfo.configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import ch.qos.logback.classic.Level;
import javax.annotation.PostConstruct;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.shellshellfish.aaas.userinfo.repositories.mongo"}, mongoTemplateRef = "mongoTemplate")
public class MongodbConfig {

	static Logger root = (Logger) LoggerFactory
			.getLogger(Logger.ROOT_LOGGER_NAME);

	static {
		root.setLevel(Level.INFO);
	}

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
