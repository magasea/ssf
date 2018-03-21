package com.shellshellfish.aaas.tools.fundcheck.configuration;


import com.shellshellfish.aaas.tools.checkfunds.FundCheckerServiceGrpc;
import com.shellshellfish.aaas.tools.checkfunds.FundCheckerServiceGrpc.FundCheckerServiceBlockingStub;
import com.shellshellfish.aaas.tools.checkfunds.FundCheckerServiceGrpc.FundCheckerServiceFutureStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcPyFundServerConfig {


	@Value("${grpc.pythonServer.ip}")
	String pyServerIp;

	@Value("${grpc.pythonServer.port}")
	int pyServerPort;

	@Bean
	ManagedChannelBuilder<?> grpcPyServerChannelBuilder() {
		return ManagedChannelBuilder.forAddress(pyServerIp, pyServerPort);
	}


	@Bean
	@PostConstruct
	ManagedChannel managedPyServerChannel() {
		ManagedChannel managedChannel = grpcPyServerChannelBuilder().usePlaintext(true).build();
		return managedChannel;
	}


	@Bean
	@PostConstruct
	FundCheckerServiceBlockingStub fundCheckRpcServiceBlockingStub() {
		return FundCheckerServiceGrpc.newBlockingStub(managedPyServerChannel());
	}


}
