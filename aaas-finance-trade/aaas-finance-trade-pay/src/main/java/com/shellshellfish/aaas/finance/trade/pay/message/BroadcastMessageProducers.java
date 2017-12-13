package com.shellshellfish.aaas.finance.trade.pay.message;


import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
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

    @Value("${spring.rabbitmq.topicQueuePayName}")
    String topicQueuePayName;

    public void sendMessage(TrdPayFlow trdPayFlow) {
        logger.info("send message: " + trdPayFlow.getOrderId());
        rabbitTemplate.convertAndSend(topicQueuePayName, topicPay, trdPayFlow);
    }

}
