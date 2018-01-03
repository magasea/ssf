package com.shellshellfish.aaas.finance.trade.pay.message;



import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    PayService payService;




    @RabbitListener( containerFactory = "jsaFactory",bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_PAY_BASE+ "-"+ RabbitMQConstants
            .OPERATION_TYPE_BUY_PROD, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_PAY)
    )
    public void receiveMessage(PayDto message) {
        try {
            PayDto payDto = payService.payOrder(message);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }

    }


    @RabbitListener( containerFactory = "jsaFactory",bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_PAY_BASE+ "-"+ RabbitMQConstants
            .OPERATION_TYPE_SEL_PROD, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_SELL)
    )
    public void receiveMessage(ProdSellDTO prodSellDTO) {
        try {
            boolean payDto = payService.sellProd(prodSellDTO);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }

    }

}
