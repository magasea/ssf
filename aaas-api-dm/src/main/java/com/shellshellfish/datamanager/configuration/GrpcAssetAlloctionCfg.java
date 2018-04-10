package com.shellshellfish.datamanager.configuration;

import com.shellshellfish.aaas.asset.allocation.AssetAllocationServiceGrpc;
import com.shellshellfish.aaas.asset.allocation.AssetAllocationServiceGrpc.AssetAllocationServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GrpcAssetAlloctionCfg {


    @Value("${grpc.asset-allocation.host}")
    String assetAllocationHost;

    @Value("${grpc.asset-allocation.port}")
    int assetAllocationPort;

    @Bean
    ManagedChannelBuilder<?> allocationChannelBuilder() {
        return ManagedChannelBuilder.forAddress(assetAllocationHost, assetAllocationPort);
    }

    @Bean
    @PostConstruct
    ManagedChannel allocationManagedChannel() {
        ManagedChannel managedChannel = allocationChannelBuilder().usePlaintext(true).build();
        return managedChannel;
    }

    @Bean
    @PostConstruct
    AssetAllocationServiceBlockingStub allocationBlockingStub() {
        return AssetAllocationServiceGrpc.newBlockingStub(allocationManagedChannel());
    }

}
