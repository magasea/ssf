package com.shellshellfish.aaas.finance.trade.order.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MessageRabbitConfig {
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





//    @Bean
//    public SimpleRabbitListenerContainerFactory container(ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//        return factory;
//    }

}
