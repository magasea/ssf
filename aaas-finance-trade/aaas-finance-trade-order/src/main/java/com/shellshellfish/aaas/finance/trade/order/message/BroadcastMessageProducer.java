package com.shellshellfish.aaas.finance.trade.order.message;

import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageProducer {


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

//    @Autowired
//    public BroadcastMessageProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }




    @Autowired
    private RabbitTemplate rabbitTemplate;



    public void sendMessages(PayDto payDto){
        rabbitTemplate.convertAndSend(topicExchangeName, topicPay, payDto);
        System.out.println("Send msg = " + payDto);
    }
}
