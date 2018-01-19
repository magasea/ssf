package com.shellshellfish.aaas.userinfo.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DataCollectionServerConfig {


	@Value("${grpc.datacollection-server-rpc.host}")
	String dataCollectionServerHost;

	@Value("${grpc.datacollection-server-rpc.port}")
	int dataCollectionServerPort;

	@Bean
	ManagedChannelBuilder<?> grpcChannelDCSBuilder() {
		return ManagedChannelBuilder.forAddress(dataCollectionServerHost, dataCollectionServerPort);
	}

	@Bean
	@PostConstruct
	ManagedChannel managedDCSChannel() {
		ManagedChannel managedChannel = grpcChannelDCSBuilder().usePlaintext(true).build();
		return managedChannel;
	}


}
