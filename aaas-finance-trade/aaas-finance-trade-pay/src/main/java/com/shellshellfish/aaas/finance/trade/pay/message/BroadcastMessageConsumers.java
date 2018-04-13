package com.shellshellfish.aaas.finance.trade.pay.message;



import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellPercentMsg;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void receiveMessage(PayOrderDto message) {
        try {
            PayOrderDto payOrderDto = payService.payOrder(message);
        }catch (Exception ex){
            logger.error("exception:",ex);

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
            logger.error("exception:",ex);

        }

    }


    @RabbitListener( containerFactory = "jsaFactory",bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_PAY_BASE+ "-"+ RabbitMQConstants
            .OPERATION_TYPE_SELPERCENT_PROD, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_SELLPERCENT)
    )
    public void receiveSellPercentMessage(ProdSellPercentMsg prodSellPercentMsg) {
        try {
            boolean payDto = payService.sellPercentProd(prodSellPercentMsg);
        }catch (Exception ex){
            logger.error("exception:",ex);

        }

    }

//    @RabbitListener( containerFactory = "jsaFactory",bindings = @QueueBinding(
//        value = @Queue(value = RabbitMQConstants.QUEUE_PAY_BASE+ "-"+ RabbitMQConstants
//            .OPERATION_TYPE_BUY_PREORDER_PROD, durable = "false"),
//        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
//            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_PAY)
//    )
//    public void receiveMessage(PayPreOrderDto message) {
//        try {
//            PayPreOrderDto payPreOrderDto = payService.payPreOrder(message);
//        }catch (Exception ex){
//            logger.error("exception:",ex);
//
//        }
//
//    }


}
