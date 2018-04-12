package com.shellshellfish.aaas.assetallocation.configuration;

import com.shellshellfish.aaas.assetallocation.service.impl.FinanceProductServiceImpl;
import com.shellshellfish.aaas.assetallocation.service.impl.GrpcFundGroupIndexService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EntityScan("com.shellshellfish.aaas.assectallocation.model")
public class GrpcServerCfg {


    @Value("${grpc.port}")
    int port;

    @Value("${grpc.daily-fund-client.host}")
    String dailyHost;

    @Value("${grpc.daily-fund-client.port}")
    int dailyPort;

    @Bean
    ManagedChannelBuilder<?> grpcDFChannelBuilder() {
        return ManagedChannelBuilder.forAddress(dailyHost, dailyPort);
    }

    @Bean
    @PostConstruct
    ManagedChannel managedDFChannel() {
        ManagedChannel managedChannel = grpcDFChannelBuilder().usePlaintext(true).build();
        return managedChannel;
    }


    @Bean
    ServerBuilder serverBuilder() {
        return ServerBuilder.forPort(port);
    }

    @Bean
    FinanceProductServiceImpl financeProductService() {
        return new FinanceProductServiceImpl();
    }

    @Bean
    GrpcFundGroupIndexService fundGroupIndexService() {
        return new GrpcFundGroupIndexService();
    }

    @Bean
    Server server() {
        return serverBuilder().addService(financeProductService()).addService(fundGroupIndexService()).build();
    }

}
