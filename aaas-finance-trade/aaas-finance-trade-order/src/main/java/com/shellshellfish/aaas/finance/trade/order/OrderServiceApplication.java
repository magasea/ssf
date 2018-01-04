package com.shellshellfish.aaas.finance.trade.order;

import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageConsumers;
import io.grpc.Server;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class OrderServiceApplication {

	private static Server server;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(OrderServiceApplication.class, args);
//		server = (Server) configurableApplicationContext.getBean("server");
//		server.start();
//		blockUntilShutdown();
	}

//	private static void blockUntilShutdown() throws InterruptedException {
//		if (server != null) {
//			server.awaitTermination();
//		}

//	}



}
