package com.shellshellfish.aaas.finance.trade.pay.config;

import org.springframework.beans.factory.annotation.Value;
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


//    @Bean
//    public List<Declarable> topicBindings() {
//        Queue topicQueuePay = new Queue(topicQueuePayName, false);
//        Queue topicQueueOrder = new Queue(topicQueueOrderName, false);
//
//        TopicExchange topicExchange = new TopicExchange(topicExchangeName);
//
//        return Arrays.asList(
//                topicQueuePay,
//                topicQueueOrder,
//                topicExchange,
//                BindingBuilder.bind(topicQueuePay).to(topicExchange).with(topicPay),
//                BindingBuilder.bind(topicQueueOrder).to(topicExchange).with(topicOrder)
//        );
//    }
//
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory container(ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//        return factory;
//    }

}
