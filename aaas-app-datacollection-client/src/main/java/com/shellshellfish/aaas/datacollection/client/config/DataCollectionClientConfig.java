package com.shellshellfish.aaas.datacollection.client.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataCollectionClientConfig {




  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Bean
  ManagedChannelBuilder<?> grpcChannelBuilder(){
    return ManagedChannelBuilder.forAddress(host, port);
  }

  @Bean
  @PostConstruct
  ManagedChannel managedChannel(){
    ManagedChannel managedChannel = grpcChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }
}
