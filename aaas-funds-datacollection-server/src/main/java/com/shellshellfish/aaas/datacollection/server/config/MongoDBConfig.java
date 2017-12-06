package com.shellshellfish.aaas.datacollection.server.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
@Configuration
public class MongoDBConfig {

  @Value("${spring.data.mongodb.host}")
  String host;

  @Value("${spring.data.mongodb.port}")
  int port;

  @Value("${spring.data.mongodb.database}")
  String database;
  @Bean
  public MongoTemplate mongoTemplate() throws Exception {

    MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
        database);
    return mongoTemplate;

  }

}
