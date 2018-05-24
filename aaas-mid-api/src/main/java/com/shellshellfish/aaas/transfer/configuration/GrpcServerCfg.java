package com.shellshellfish.aaas.transfer.configuration;

import com.shellshellfish.aaas.GrpcService.GrpcGetAfficientFrontierDataService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerCfg {

    @Value("${grpc.afficient-frontier-client.port}")
    private int port; //10888

    @Bean
    GrpcGetAfficientFrontierDataService grpcGetAfficientFrontierDataService(){
        return new GrpcGetAfficientFrontierDataService();
    }

    @Bean
    ServerBuilder serverBuilder() {
        return ServerBuilder.forPort(port);
    }

    @Bean
    Server server() {
        return serverBuilder().addService(grpcGetAfficientFrontierDataService()).build();
    }

}
