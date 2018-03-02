package com.shellshellfish.aaas.userinfo;


import io.grpc.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.TimeZone;


@EnableAutoConfiguration
@EntityScan(basePackages = {"com.shellshellfish.aaas.userinfo"})
@SpringBootApplication(scanBasePackages = {"com.shellshellfish.aaas.userinfo"})// same as
public class UserInfoApp {


	private static Server server;

	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication
				.run(UserInfoApp.class, args);
		server = (Server) configurableApplicationContext.getBean("server");
		server.start();
		blockUntilShutdown();
	}

	private static void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}

	}


	@PostConstruct
	void started() {
//		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	}
}
