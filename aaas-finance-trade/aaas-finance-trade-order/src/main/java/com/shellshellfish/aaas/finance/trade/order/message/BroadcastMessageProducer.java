package com.shellshellfish.aaas.finance.trade.order.message;

import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageProducer {


//    @Value("${spring.rabbitmq.topicQueuePayName}")
//    String topicQueuePayName;
//
//    @Value("${spring.rabbitmq.topicQueueOrderName}")
//    String topicQueueOrderName;
//
//    @Value("${spring.rabbitmq.topicExchangeName}")
//    String topicExchangeName;
//
//    @Value("${spring.rabbitmq.topicPay}")
//    String topicPay;
//
//    @Value("${spring.rabbitmq.topicOrder}")
//    String topicOrder;
//



    @Autowired
    private RabbitTemplate rabbitTemplate;



    public void sendPayMessages(PayDto payDto){
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_PAY, payDto);
        System.out.println("Send msg = " + payDto);
    }

    public void sendPayMessages(ProdSellDTO prodSellDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .ROUTING_KEY_SELL, prodSellDTO);
    }
}
