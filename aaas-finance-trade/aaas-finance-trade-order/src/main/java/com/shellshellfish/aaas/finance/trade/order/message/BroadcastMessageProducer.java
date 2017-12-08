package com.shellshellfish.aaas.finance.trade.order.message;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.topicQueueName}")
    String topicQueueName;

    @Value("${spring.rabbitmq.topicExchangeName}")
    String topicExchangeName;

    @Autowired
    public BroadcastMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessages(TrdOrderPay message) {
        rabbitTemplate.convertAndSend(topicExchangeName, "order", message);
    }
}
