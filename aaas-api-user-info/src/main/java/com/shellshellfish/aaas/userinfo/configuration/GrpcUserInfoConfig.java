package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.dao.service.impl.UserInfoRepoServiceImpl;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.shellshellfish.aaas.datacollection.server.model")
public class GrpcUserInfoConfig  {




  @Value("${grpc.userinfo.host}")
  String host;

  @Value("${grpc.userinfo.port}")
  int port;

//  @Bean
//  ManagedChannelBuilder<?> grpcChannelBuilder(){
//    return ManagedChannelBuilder.forAddress(host, port);
//  }
//
//  @Bean
//  @PostConstruct
//  ManagedChannel managedChannel(){
//    ManagedChannel managedChannel = grpcChannelBuilder().usePlaintext(true).build();
//    return managedChannel;
//  }

//  @Value("${grpc.order_server.port}")
//  int orderServerPort;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(port);
  }

  @Bean
  UserInfoRepoService userInfoRepoService(){return new UserInfoRepoServiceImpl();}

  @Bean
  Server server(){
    return serverBuilder().addService((BindableService) userInfoRepoService()).build();
  }

}
