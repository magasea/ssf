package com.shellshellfish.aaas.transfer.configuration;

import com.shellshellfish.aaas.tools.oeminfo.OemInfoServiceGrpc;
import com.shellshellfish.aaas.tools.oeminfo.OemInfoServiceGrpc.OemInfoServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenwei on 2018- 四月 - 04
 */
@Configuration
public class GrpcOemInfoConfig {

  @Value("${grpc.oeminfo.host}")
  String oemInfoHost;

  @Value("${grpc.oeminfo.port}")
  int oemInfoPort;


  @Bean
  ManagedChannelBuilder<?> grpcOIChannelBuilder() {
    return ManagedChannelBuilder.forAddress(oemInfoHost, oemInfoPort);
  }

  @Bean
  @PostConstruct
  ManagedChannel managedOIChannel() {
    ManagedChannel managedChannel = grpcOIChannelBuilder().usePlaintext(true).build();
    return managedChannel;
  }

  @Bean
  @PostConstruct
  OemInfoServiceBlockingStub oemInfoServiceBlockStub() {
    return OemInfoServiceGrpc.newBlockingStub(managedOIChannel());
  }

}
