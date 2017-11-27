package com.shellshellfish.aaas.config;

import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceStub;
import com.shellshellfish.aaas.service.DataCollectionService;
import com.shellshellfish.aaas.service.DataCollectionServiceImpl;
import io.grpc.ManagedChannel;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DataCollectionClientConfig {




  @Value("${grpc.host}")
  String host;

  @Value("${grpc.port}")
  int port;

  @Bean
  DataCollectionService dataCollectionService(){
    return new DataCollectionServiceImpl(host, port);
  }

}
