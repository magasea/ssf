package com.shellshellfish.aaas.finance.trade.pay.config;

import com.shellshellfish.aaas.finance.trade.pay.service.impl.PayServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@EntityScan("com.shellshellfish.aaas.datacollection.server.model")
public class GrpcConfig extends GRpcServerBuilderConfigurer {




  @Value("${grpc.finance_prod_client.host}")
  String FPhost;

  @Value("${grpc.finance_prod_client.port}")
  int FPport;

  @Bean
  ManagedChannelBuilder<?> grpcFPChannelBuilder(){
    return ManagedChannelBuilder.forAddress(FPhost, FPport);
  }

  @Bean
  @PostConstruct
  ManagedChannel managedChannel(){
    ManagedChannel managedChannel = grpcFPChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Value("${grpc.port}")
  int payServerPort;

  @Autowired
  PayServiceImpl payServiceImpl;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(payServerPort).addService(payServiceImpl);
  }
}
