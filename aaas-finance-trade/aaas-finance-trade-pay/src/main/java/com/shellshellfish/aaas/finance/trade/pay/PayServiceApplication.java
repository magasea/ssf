package com.shellshellfish.aaas.finance.trade.pay;

import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageConsumers;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
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
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication

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


}
