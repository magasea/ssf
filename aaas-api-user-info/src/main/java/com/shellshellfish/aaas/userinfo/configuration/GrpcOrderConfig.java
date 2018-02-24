package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	OrderRpcServiceBlockingStub orderRpcServiceBlockingStub() {
		return OrderRpcServiceGrpc.newBlockingStub(managedOrderChannel());
	}


}
