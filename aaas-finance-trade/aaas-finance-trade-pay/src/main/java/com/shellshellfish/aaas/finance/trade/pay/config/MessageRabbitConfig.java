package com.shellshellfish.aaas.finance.trade.pay.config;

import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageConsumers;
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
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRabbitConfig {
    @Value("${spring.rabbitmq.host}")
    String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    int rabbitPort;

    @Value("${spring.rabbitmq.username}")
    String rabbitUN;

    @Value("${spring.rabbitmq.password}")
    String rabbitPW;

    @Value("${spring.rabbitmq.virtual-host}")
    String rabbitVH;

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
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setUsername(rabbitUN);
        connectionFactory.setPassword(rabbitPW);
        connectionFactory.setPort(rabbitPort);
        connectionFactory.setVirtualHost(rabbitVH);
        connectionFactory.setRequestedHeartBeat(60);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(topicOrder);
//    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
        SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }


//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }



//    @Bean
//    Queue queue() {
//        return new Queue(topicQueuePayName, false);
//    }

//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange("com.ssf.topic.exchange");
//	}

//	@Bean
//	Binding binding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(topicQueueOrderName);
//	}




//    @Bean
//    public List<Declarable> topicBindings() {
////		Queue topicQueue1 = new Queue(topicQueuePayName, false);
//
//
//        TopicExchange topicExchange = new TopicExchange(topicExchangeName);
//
//        return Arrays.asList(
//            queue(),
//            topicExchange,
//            BindingBuilder.bind(queue()).to(topicExchange).with(topicOrder)
//        );
//    }


//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange("com.ssf.topic.exchange");
//	}



//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//        MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(topicQueueOrderName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(BroadcastMessageConsumers receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
}