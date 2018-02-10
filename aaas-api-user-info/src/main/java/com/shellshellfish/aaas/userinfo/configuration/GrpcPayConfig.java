package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceBlockingStub;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.dao.service.impl.UserInfoRepoServiceImpl;
import io.grpc.BindableService;
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
@EntityScan("com.shellshellfish.aaas.datacollection.server.model")
public class GrpcPayConfig {




  @Value("${grpc.finance-trade-pay-rpc.host}")
  String payHost;

  @Value("${grpc.finance-trade-pay-rpc.port}")
  int payPort;

  @Bean
  ManagedChannelBuilder<?> grpcChannelPayBuilder(){
    return ManagedChannelBuilder.forAddress(payHost, payPort);
  }

  @Bean
  @PostConstruct
  ManagedChannel managedPayChannel(){
    ManagedChannel managedChannel = grpcChannelPayBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  PayRpcServiceGrpc.PayRpcServiceBlockingStub payRpcServiceBlockingStub() {
    return PayRpcServiceGrpc.newBlockingStub(managedPayChannel());
  }

}
