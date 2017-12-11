package com.shellshellfish.aaas.assetallocation;



import io.grpc.Server;
import java.io.IOException;


import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.shellshellfish.aaas.assetallocation.configuration.Properties;


@ComponentScan
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.assetallocation.repository.dummy"})
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableConfigurationProperties({ Properties.class })


public class AssetAllocationApp {

	private static Server server;

	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(AssetAllocationApp.class, args);
		server = (Server) configurableApplicationContext.getBean("server");
		server.start();
		blockUntilShutdown();
	}

	private static void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}

@MapperScan("com.shellshellfish.aaas.assetallocation.neo.mapper")
public class AssetAllocationApp {
	public static void main(String[] args) {
		SpringApplication.run(AssetAllocationApp.class, args);

	}
}
