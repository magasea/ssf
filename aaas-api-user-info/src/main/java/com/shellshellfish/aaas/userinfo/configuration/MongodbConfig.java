package com.shellshellfish.aaas.userinfo.configuration;

import com.mongodb.MongoClient;
import com.oracle.webservices.internal.api.message.PropertySet.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongodbConfig {
//  spring:
//  profiles: prod
//  data:
//  mongodb:
//  database: test1
//  host: 192.168.1.10
//  port: 27017
  @Value("${spring.data.mongodb.host}")
  String host;

  @Value("${spring.data.mongodb.port}")
  int port;

  @Value("${spring.data.mongodb.database}")
  String database;

  public @Bean
  MongoTemplate mongoTemplate() throws Exception {

    MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
        database);
    return mongoTemplate;

  }
}
