package com.shellshellfish.datamanager.configuration;


import com.shellshellfish.datamanager.service.impl.FundInfoGrpcServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.shellshellfish.aaas.assectallocation.model")
public class GrpcServerCfg {

  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(port);
  }

  @Bean
  FundInfoGrpcServiceImpl fundInfoGrpcService(){
    return new FundInfoGrpcServiceImpl();
  }

  @Bean
  Server server(){
    return serverBuilder().addService(fundInfoGrpcService()).build();
  }

}
