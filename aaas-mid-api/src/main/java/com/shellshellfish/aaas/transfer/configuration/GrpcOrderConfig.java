package com.shellshellfish.aaas.transfer.configuration;

import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author pierre.chen
 * @Date 2018-05-10
 */
@Configuration
public class GrpcOrderConfig {

    @Value("${grpc.order.host}")
    String oemInfoHost;

    @Value("${grpc.order.port}")
    int oemInfoPort;


    @Bean
    ManagedChannelBuilder<?> grpcOrderChannelBuilder() {
        return ManagedChannelBuilder.forAddress(oemInfoHost, oemInfoPort);
    }

    @Bean
    @PostConstruct
    ManagedChannel managedOrderChannel() {
        ManagedChannel managedChannel = grpcOrderChannelBuilder().usePlaintext().build();
        return managedChannel;
    }

    @Bean
    @PostConstruct
    OrderRpcServiceGrpc.OrderRpcServiceBlockingStub orderRpcServiceBlockingStub() {
        return OrderRpcServiceGrpc.newBlockingStub(managedOrderChannel());
    }

}
