package com.shellshellfish.aaas.oeminfo.configuration;

import com.shellshellfish.aaas.oeminfo.service.OemInfoService;
import com.shellshellfish.aaas.oeminfo.service.impl.OemInfoServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by chenwei on 2018- 四月 - 04
 */

public class GrpcServerCfg {

  @Value("${grpc.port}")
  int port;


  @Bean
  ServerBuilder serverBuilder() {
    return ServerBuilder.forPort(port);
  }

  @Autowired
  OemInfoServiceImpl oemInfoService;

  @Bean
  @PostConstruct
  Server server(){
    return serverBuilder().forPort(port).addService(oemInfoService).build();
  }

}
