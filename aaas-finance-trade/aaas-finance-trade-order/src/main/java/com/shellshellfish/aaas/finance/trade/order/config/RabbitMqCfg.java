package com.shellshellfish.aaas.finance.trade.order.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqCfg {

  @Value("${spring.rabbitmq.topicQueueName}")
  String topicQueueName;

  @Value("${spring.rabbitmq.topicExchangeName}")
  String topicExchangeName;

  @Bean
  public List<Declarable> topicBindings() {
    Queue topicQueue = new Queue(topicQueueName, false);
    TopicExchange topicExchange = new TopicExchange(topicExchangeName);

    return Arrays.asList(
        topicQueue,
        topicExchange,
        BindingBuilder.bind(topicQueue).to(topicExchange).with("order")
    );
  }

}
