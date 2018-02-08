package com.shellshellfish.aaas.userinfo.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GrpcDataManagerConfig {

  @Value("${grpc.data-manager-rpc.host}")
  String dataManagerHost;

  @Value("${grpc.data-manager-rpc.port}")
  int dataManagerPort;



  @Bean
  ManagedChannelBuilder<?> grpcDMChannelBuilder(){
    return ManagedChannelBuilder.forAddress(dataManagerHost, dataManagerPort);
  }


  @Bean
  @PostConstruct
  ManagedChannel managedDMChannel(){
    ManagedChannel managedChannel = grpcDMChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }




}
