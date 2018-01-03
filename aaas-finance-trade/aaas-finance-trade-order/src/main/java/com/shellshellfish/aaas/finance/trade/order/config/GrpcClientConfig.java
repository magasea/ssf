package com.shellshellfish.aaas.finance.trade.order.config;

import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.impl.OrderServiceImpl;
import com.shellshellfish.aaas.finance.trade.order.service.impl.TrdOrderServiceImpl;
import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {




  @Value("${grpc.finance_prod_client.host}")
  String finHost;

  @Value("${grpc.finance_prod_client.port}")
  int finPort;

  @Value("${grpc.pay_client.host}")
  String payHost;

  @Value("${grpc.pay_client.port}")
  int payPort;

  @Value("${grpc.datacollection_client.host}")
  String dcHost;

  @Value("${grpc.datacollection_client.port}")
  int dcPort;

  @Bean
  ManagedChannelBuilder<?> grpcFINChannelBuilder(){
    return ManagedChannelBuilder.forAddress(finHost, finPort);
  }

  @Bean
  ManagedChannelBuilder<?> grpcPayChannelBuilder(){
    return ManagedChannelBuilder.forAddress(payHost, payPort);
  }

  @Bean
  ManagedChannelBuilder<?> grpcDCChannelBuilder(){
    return ManagedChannelBuilder.forAddress(dcHost, dcPort);
  }

  @Value("${grpc.userinfo_client.host}")
  String uiHost;

  @Value("${grpc.userinfo_client.port}")
  int uiPort;

  @Bean
  ManagedChannelBuilder<?> grpcUIChannelBuilder(){
    return ManagedChannelBuilder.forAddress(uiHost, uiPort);
  }

  @Bean
  @PostConstruct
  ManagedChannel managedFINChannel(){
    ManagedChannel managedChannel = grpcFINChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedUIChannel(){
    ManagedChannel managedChannel = grpcUIChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedPayChannel(){
    ManagedChannel managedChannel = grpcPayChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  ManagedChannel managedDCChannel(){
    ManagedChannel managedChannel = grpcDCChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Value("${grpc.order_server.port}")
  int orderServerPort;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(orderServerPort);
  }




  @Autowired
  TrdOrderServiceImpl trdOrderService;

  @Bean
  Server server(){
    return serverBuilder().addService(trdOrderService).build();
  }

}