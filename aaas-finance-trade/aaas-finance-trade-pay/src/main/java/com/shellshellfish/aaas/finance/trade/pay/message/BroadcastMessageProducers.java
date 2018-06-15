package com.shellshellfish.aaas.finance.trade.pay.message;


import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageProducers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageProducers.class);
    @Autowired
    RabbitTemplate rabbitTemplate;




    public void sendMessage(TrdPayFlow trdPayFlow) {
        logger.info("send message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_ORDER,
            trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_USERINFO,
            trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
                .ROUTING_KEY_USERINFO_TRDLOG,
            trdPayFlow);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS,
            trdPayFlow);
    }

    public void sendFailedMsgToPendingRecord(TrdPayFlow trdPayFlow){
        logger.info("send message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.OPERATION_TYPE_FAILED_PENDINGRECORDS,
            trdPayFlow);
    }

    public void sendFailedMsgToOrderDetail(TrdPayFlow trdPayFlow){
        logger.info("send message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.OPERATION_TYPE_FAILED_TRADE,
            trdPayFlow);
    }
    public void sendPreOrderMessage(TrdPayFlow trdPayFlow) {
        logger.info("send preOrder message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
                .ROUTING_KEY_PREORDER, trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_USERINFO,
            trdPayFlow);
    }

    public void sendConfirmMessage(MongoUiTrdZZInfo mongoUiTrdZZInfo){
        logger.info("sending  mongoUiTrdZZInfo" + mongoUiTrdZZInfo.getApplySerial());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .ROUTING_KEY_USERINFO_CFMLOG, mongoUiTrdZZInfo);
//        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
//            .ROUTING_KEY_USERINFO_UPDATEPROD, mongoUiTrdZZInfo);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .OPERATION_TYPE_CONFIRM_PENDINGRECORDS, mongoUiTrdZZInfo);

    }

    public void sendSellMessage(TrdPayFlow trdPayFlow) {
        logger.info("send message: " + trdPayFlow.getOrderDetailId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY_ORDER,
            trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
                .ROUTING_KEY_USERINFO_REDEEM, trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants
            .ROUTING_KEY_USERINFO_TRDLOG, trdPayFlow);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS,
            trdPayFlow);
    }
}
