package com.shellshellfish.aaas.risk.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.shellshellfish.aaas.risk.repository")
public class MongoConfiguration {

    @Bean
    public Mongo mongo(MongoProperties mongoProperties) throws Exception {
        return new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
    }
    
    @Bean
    public MongoTemplate mongoTemplate(MongoProperties mongoProperties) throws Exception {
        return new MongoTemplate(mongo(mongoProperties), mongoProperties.getDatabase());
    }

}