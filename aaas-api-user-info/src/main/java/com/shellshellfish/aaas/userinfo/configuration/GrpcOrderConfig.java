package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.finance.trade.grpc.TradeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GrpcOrderConfig {


	@Value("${grpc.finance-trade-order-rpc.host}")
	String financeTradeOrderHost;

	@Value("${grpc.finance-trade-order-rpc.port}")
	int financeTradeOrderPort;

	@Bean
	ManagedChannelBuilder<?> grpcOrderChannelBuilder() {
		return ManagedChannelBuilder.forAddress(financeTradeOrderHost, financeTradeOrderPort);
	}


	@Bean
	@PostConstruct
	ManagedChannel managedOrderChannel() {
		ManagedChannel managedChannel = grpcOrderChannelBuilder().usePlaintext(true).build();
		return managedChannel;
	}

	@Bean
	@PostConstruct
	TradeServiceGrpc.TradeServiceBlockingStub tradeOrderServiceBlockingStub() {
		return TradeServiceGrpc.newBlockingStub(managedOrderChannel());
	}


}
