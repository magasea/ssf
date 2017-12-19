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
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class OrderServiceApplication {

	private static Server server;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(OrderServiceApplication.class, args);
		server = (Server) configurableApplicationContext.getBean("server");
		server.start();
		blockUntilShutdown();
	}

	private static void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}

	}

	@Value("${spring.rabbitmq.topicQueuePayName}")
	String topicQueuePayName;

	@Value("${spring.rabbitmq.topicQueueOrderName}")
	String topicQueueOrderName;

	@Value("${spring.rabbitmq.topicExchangeName}")
	String topicExchangeName;

	@Value("${spring.rabbitmq.topicPay}")
	String topicPay;

	@Value("${spring.rabbitmq.topicOrder}")
	String topicOrder;


	@Bean
	Queue queue() {
		return new Queue(topicQueueOrderName, false);
	}

//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange("com.ssf.topic.exchange");
//	}

//	@Bean
//	Binding binding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(topicQueueOrderName);
//	}




	@Bean
	public List<Declarable> topicBindings() {
		Queue topicQueue1 = new Queue(topicQueuePayName, false);
		Queue topicQueue2 = new Queue(topicQueueOrderName, false);

		TopicExchange topicExchange = new TopicExchange(topicExchangeName);

		return Arrays.asList(
				topicQueue1,
				topicQueue2,
				topicExchange,
				BindingBuilder.bind(topicQueue1).to(topicExchange).with(topicPay),
				BindingBuilder.bind(topicQueue2).to(topicExchange).with(topicOrder)
		);
	}


//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange("com.ssf.topic.exchange");
//	}



	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(topicQueuePayName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(BroadcastMessageConsumers receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

}
