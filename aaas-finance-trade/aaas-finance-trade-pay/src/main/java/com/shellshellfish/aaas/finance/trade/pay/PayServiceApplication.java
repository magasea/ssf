package com.shellshellfish.aaas.finance.trade.pay;

import io.grpc.Server;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EntityScan("com.shellshellfish.aaas.finance.trade.pay")
public class PayServiceApplication {

	@Value("${spring.rabbitmq.topicQueuePayName}")
	String topicQueuePayName;

	@Value("${spring.rabbitmq.topicPay}")
	String topicPay;

	@Value("${spring.rabbitmq.topicOrder}")
	String topicOrder;

	@Value("${spring.rabbitmq.topicQueueOrderName}")
	String topicQueueOrderName;

	@Value("${spring.rabbitmq.topicExchangeName}")
	String topicExchangeName;

	private static Server server;
	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(PayServiceApplication.class, args);
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
