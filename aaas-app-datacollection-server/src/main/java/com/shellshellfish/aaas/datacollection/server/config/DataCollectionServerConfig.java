package com.shellshellfish.aaas.datacollection.server.config;

import com.shellshellfish.aaas.datacollection.server.service.DataCollectionServiceImpl;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class DataCollectionServerConfig extends GRpcServerBuilderConfigurer {




  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Bean
  ServerBuilder serverBuilder(){
    return ServerBuilder.forPort(port);
  }

  @Bean
  DataCollectionServiceImpl dataCollectionService(){
    return new DataCollectionServiceImpl();
  }

  @Bean
  Server server(){
    return serverBuilder().addService(dataCollectionService()).build();
  }


}
