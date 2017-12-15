package com.shellshellfish.aaas.assetallocation.configuration;

import com.shellshellfish.aaas.assetallocation.service.FinanceProductService;
import com.shellshellfish.aaas.assetallocation.service.impl.FinanceProductServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

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
  FinanceProductServiceImpl financeProductService(){
    return new FinanceProductServiceImpl();
  }

  @Bean
  Server server(){
    return serverBuilder().addService(financeProductService()).build();
  }

}
