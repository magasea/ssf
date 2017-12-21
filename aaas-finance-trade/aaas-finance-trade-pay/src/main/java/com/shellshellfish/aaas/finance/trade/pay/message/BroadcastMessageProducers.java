package com.shellshellfish.aaas.finance.trade.pay.message;


import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageProducers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageProducers.class);
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Value("${spring.rabbitmq.topicPay}")
    String topicPay;

    @Value("${spring.rabbitmq.topicOrder}")
    String topicOrder;

    @Value("${spring.rabbitmq.topicQueuePayName}")
    String topicQueuePayName;

    @Value("${spring.rabbitmq.topicExchangeName}")
    String exchange;

    public void sendMessage(TrdPayFlow trdPayFlow) {
        logger.info("send message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(exchange, topicOrder, trdPayFlow);
    }

}
