package com.shellshellfish.datamanager.configuration;

import com.shellshellfish.datamanager.service.impl.DayIndicatorServiceImpl;
import com.shellshellfish.datamanager.service.impl.YearIndicatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerCfg {

	@Autowired
	DayIndicatorServiceImpl dayIndicatorService;

	@Autowired
	YearIndicatorServiceImpl yearIndicatorService;

	@Value("${grpc.host}")
	String host;

	@Value("${grpc.port}")
	int port;


	@Bean
	ServerBuilder serverBuilder() {
		return ServerBuilder.forPort(port);
	}


	@Bean
	Server server() {
		return serverBuilder().addService(dayIndicatorService).addService(yearIndicatorService).build();
	}

}
