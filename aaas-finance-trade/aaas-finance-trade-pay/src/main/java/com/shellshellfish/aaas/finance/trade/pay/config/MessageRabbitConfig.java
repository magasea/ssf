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
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(topicQueueOrderName);
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
            topicOrderQueue,
            topicPayQueue,
            topicExchange,
            BindingBuilder.bind(topicPayQueue).to(topicExchange).with(topicPay),
            BindingBuilder.bind(topicPayQueue).to(topicExchange).with(topicOrder)
        );
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(topicPay);
    }

    @Bean
    Queue queue() {
        return new Queue(topicQueuePayName, false);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }

//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
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


}
