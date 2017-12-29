package com.shellshellfish.datamanager;

import io.grpc.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication

public class DataManagerServiceApplication {

	private static Server server;

	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(DataManagerServiceApplication.class, args);
		server = (Server) configurableApplicationContext.getBean("server");
		server.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {

				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				server.shutdown();
				System.err.println("*** server shut down");
			}
		});
	}

	private static void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}

	}
	
     	
}
