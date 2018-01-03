package com.shellshellfish.aaas.datacollection.server.config;

import com.shellshellfish.aaas.datacollection.server.service.impl.DataCollectionServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EntityScan("com.shellshellfish.aaas.datacollection.server.model")
@EnableMongoRepositories(basePackages = "com.shellshellfish.aaas.datacollection.server"
    + ".repositories")
public class DataCollectionServerConfig extends GRpcServerBuilderConfigurer {




  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(port);
  }

  @Bean
  DataCollectionServiceImpl dataCollectionService(){
    return new DataCollectionServiceImpl();
  }

  @Bean
  Server server(){
    return serverBuilder().addService(dataCollectionService()).build();
  }


}
