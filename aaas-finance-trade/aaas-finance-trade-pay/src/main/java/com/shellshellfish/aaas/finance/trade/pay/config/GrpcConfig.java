package com.shellshellfish.aaas.finance.trade.pay.config;

import com.shellshellfish.aaas.finance.trade.pay.service.impl.PayServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.Server;
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


  @Value("${grpc.userinfo_client.host}")
  String UIhost;

  @Value("${grpc.userinfo_client.port}")
  int UIport;

  @Value("${grpc.order_client.host}")
  String ordHost;

  @Value("${grpc.order_client.port}")
  int ordPort;

  @Value("${grpc.date_collection_client.host}")
  String dccHost;

  @Value("${grpc.date_collection_client.port}")
  int dccPort;

  @Bean
  ManagedChannelBuilder<?> grpcFPChannelBuilder(){
    return ManagedChannelBuilder.forAddress(FPhost, FPport);
  }

  @Bean
  ManagedChannelBuilder<?> grpcUIChannelBuilder(){
    return ManagedChannelBuilder.forAddress(UIhost, UIport);
  }

  @Bean
  ManagedChannelBuilder<?> grpcOrderChannelBuilder(){
    return ManagedChannelBuilder.forAddress(ordHost, ordPort);
  }

  @Bean
  ManagedChannelBuilder<?> grpcDccChannelBuilder(){
    return ManagedChannelBuilder.forAddress(dccHost, dccPort);
  }


  @Bean
  @PostConstruct
  ManagedChannel managedUIChannel(){
    ManagedChannel managedUIChannel = grpcUIChannelBuilder().usePlaintext(true).build();
    return managedUIChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedChannel(){
    ManagedChannel managedChannel = grpcFPChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedOrderChannel(){
    ManagedChannel managedChannel = grpcOrderChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedDccChannel(){
    ManagedChannel managedChannel = grpcDccChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Value("${grpc.port}")
  int payServerPort;

  @Autowired
  PayServiceImpl payServiceImpl;

  @Bean
  Server server(){
    return ServerBuilder.forPort(payServerPort).addService(payServiceImpl).build();
  }
}
