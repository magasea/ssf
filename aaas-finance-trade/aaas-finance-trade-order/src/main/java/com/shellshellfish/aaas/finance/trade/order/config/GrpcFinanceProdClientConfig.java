package com.shellshellfish.aaas.finance.trade.order.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
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
public class GrpcFinanceProdClientConfig extends GRpcServerBuilderConfigurer {




  @Value("${grpc.finance_prod_client.host}")
  String host;

  @Value("${grpc.finance_prod_client.port}")
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
