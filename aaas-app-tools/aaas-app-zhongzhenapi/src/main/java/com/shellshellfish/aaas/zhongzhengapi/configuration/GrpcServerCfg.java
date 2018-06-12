package com.shellshellfish.aaas.zhongzhengapi.configuration;

import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import com.shellshellfish.aaas.zhongzhengapi.service.impl.ZZGrpcServiceImpl;
import com.shellshellfish.aaas.zhongzhengapi.service.impl.ZhongzhengApiServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenwei on 2018- 四月 - 04
 */
@Configuration
public class GrpcServerCfg {

  @Value("${grpc.port}")
  int port;


  @Bean
  ServerBuilder serverBuilder() {
    return ServerBuilder.forPort(port);
  }

  @Autowired
  ZZGrpcServiceImpl zzGrpcService;

  @Bean
  @PostConstruct
  Server server(){
    return serverBuilder().forPort(port).addService(zzGrpcService).build();
  }

}
