package com.shellshellfish.aaas.finance.trade.order.message;

import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.message.order.OrderStatusChangeDTO;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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



    public void sendPayMessages(PayOrderDto payOrderDto){
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_PAY,
            payOrderDto);
        System.out.println("Send msg = " + payOrderDto);
    }

//    public void sendPayMessages(PayPreOrderDto payPreOrderDto){
//        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_PAY,
//            payPreOrderDto);
//        System.out.println("Send msg = " + payPreOrderDto);
//    }

    public void sendSellMessages(ProdSellDTO prodSellDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .ROUTING_KEY_SELL, prodSellDTO);
    }

    public void sendOrderStatusChangeMessages(OrderStatusChangeDTO orderStatusChangeDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .ROUTING_KEY_ORDER, orderStatusChangeDTO);
    }
}
