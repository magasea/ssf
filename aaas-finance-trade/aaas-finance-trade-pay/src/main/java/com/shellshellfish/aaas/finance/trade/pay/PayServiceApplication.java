package com.shellshellfish.aaas.finance.trade.pay;

import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageConsumers;
import io.grpc.Server;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
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
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
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


	public static void main(String[] args) throws IOException, InterruptedException {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
				(PayServiceApplication.class, args);

	}


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

	@Bean
	public List<Declarable> topicBindings() {
		Queue topicPayQueue = new Queue(topicQueuePayName, false);
		Queue topicOrderQueue = new Queue(topicQueueOrderName, false);
		TopicExchange topicExchange = new TopicExchange(topicExchangeName);

		return Arrays.asList(
				topicPayQueue,
				topicOrderQueue,
				topicExchange,
				BindingBuilder.bind(topicPayQueue).to(topicExchange).with(topicPay),
				BindingBuilder.bind(topicOrderQueue).to(topicExchange).with(topicOrder)
		);
	}


	@Bean
	Queue queue() {
		return new Queue(topicQueuePayName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("com.ssf.topic.exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(topicQueuePayName);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	}

}
