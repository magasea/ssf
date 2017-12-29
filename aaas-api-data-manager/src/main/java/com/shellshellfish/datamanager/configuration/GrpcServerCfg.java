package com.shellshellfish.datamanager.configuration;



import com.shellshellfish.datamanager.service.FundInfoGrpcService;
import com.shellshellfish.datamanager.service.impl.FundInfoGrpcServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.shellshellfish.datamanager.service.impl.DayIndicatorServiceImpl;
import com.shellshellfish.datamanager.service.impl.YearIndicatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@EntityScan("com.shellshellfish.aaas.assectallocation.model")
public class GrpcServerCfg {

  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Autowired
  DayIndicatorServiceImpl dayIndicatorService;

  @Autowired
  YearIndicatorServiceImpl yearIndicatorService;

  @Autowired
  FundInfoGrpcServiceImpl fundInfoGrpcService;

  @Bean
  FundInfoGrpcServiceImpl fundInfoGrpcService(){
    return new FundInfoGrpcServiceImpl();
  }

//  @Bean
//  Server server(){
//    return serverBuilder().addService(fundInfoGrpcService()).build();
//  }

  @Bean
  ServerBuilder serverBuilder() {
    return ServerBuilder.forPort(port);
  }


  @Bean
  Server server() {
    return serverBuilder().addService(dayIndicatorService).addService(yearIndicatorService)
        .addService(fundInfoGrpcService).build();
  }


}
