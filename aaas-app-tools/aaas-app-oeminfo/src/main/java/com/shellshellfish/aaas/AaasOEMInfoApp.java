package com.shellshellfish.aaas;

import com.shellshellfish.aaas.oeminfo.configuration.JpaConfiguration;
import io.grpc.Server;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;


@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.oeminfo"})// same as
// @Configuration @EnableAutoConfiguration @ComponentScan
public class AaasOEMInfoApp {



	@Autowired
	private static Server server;

	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(AaasOEMInfoApp.class, args);
		server = (Server) configurableApplicationContext.getBean("server");
		server.start();
		blockUntilShutdown();
	}

	private static void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}

	}
}
