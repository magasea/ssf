package com.shellshellfish.aaas.userinfo.message;


import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.OrderStatusChangeDTO;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.Transactional;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    MongoUserTrdLogMsgRepo mongoUserTrdLogMsgRepo;

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants.OPERATION_TYPE_UPDATE_UIPROD,
            durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO)
    )
    public void receiveMessage(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        logger.info("this consumer only controll buy payFlow message");
        if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.BUY.getOperation()){
            logger.info("get buy update payFlow msg");
            try{
                UiProductDetail uiProductDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                    (trdPayFlow.getUserProdId(), trdPayFlow.getFundCode());
                uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
                uiProductDetail.setStatus(trdPayFlow.getTrdStatus());
                if(trdPayFlow.getTradeConfirmShare() != null && trdPayFlow.getTradeConfirmShare()
                    > 0){
                    uiProductDetail.setFundQuantityTrade(trdPayFlow.getTradeConfirmShare().intValue());
                    uiProductDetail.setFundQuantity(trdPayFlow.getTradeConfirmShare().intValue());
                }
                uiProductDetailRepo.save(uiProductDetail);
            }catch (Exception ex){
                ex.printStackTrace();
                logger.error(ex.getMessage());
            }
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
        
    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_UITRDLOG, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO)
    )
    public void receiveTradeMessage(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        MongoUiTrdLog  mongoUiTrdLog = new MongoUiTrdLog();
        try{
            mongoUiTrdLog.setTradeConfirmSum(trdPayFlow.getTradeConfirmSum());
            mongoUiTrdLog.setTradeConfirmShare(trdPayFlow.getTradeConfirmShare());
            mongoUiTrdLog.setTradeTargetShare(trdPayFlow.getTradeTargetShare());
            mongoUiTrdLog.setTradeTargetSum(trdPayFlow.getTradeTargetSum());
            mongoUiTrdLog.setOperations(trdPayFlow.getTrdType());
            mongoUiTrdLog.setUserProdId(trdPayFlow.getUserProdId());
            mongoUiTrdLog.setUserId(trdPayFlow.getUserId());
            mongoUiTrdLog.setTradeStatus(trdPayFlow.getTrdStatus());
            mongoUiTrdLog.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoUiTrdLog.setFundCode(trdPayFlow.getFundCode());
            mongoUiTrdLog.setTradeDate(trdPayFlow.getUpdateDate());
            mongoUserTrdLogMsgRepo.save(mongoUiTrdLog);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();

    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_UITRDLOG, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO)
    )
    public void receiveOrderStatusChangeMessage(OrderStatusChangeDTO orderStatusChangeDTO, Channel channel, @Header
        (AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + orderStatusChangeDTO);
        //update ui_products 和 ui_product_details
        MongoUiTrdLog  mongoUiTrdLog = new MongoUiTrdLog();
        try{
            mongoUiTrdLog.setOperations(orderStatusChangeDTO.getOrderType());
            mongoUiTrdLog.setUserProdId(orderStatusChangeDTO.getUserProdId());
            mongoUiTrdLog.setUserId(orderStatusChangeDTO.getUserId());
            mongoUiTrdLog.setTradeStatus(orderStatusChangeDTO.getOrderStatus());
            mongoUiTrdLog.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoUiTrdLog.setTradeDate(orderStatusChangeDTO.getOrderDate());
            mongoUserTrdLogMsgRepo.save(mongoUiTrdLog);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();

    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_CHECKSELL_ROLLBACK, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO)
    )
    public void receiveAndCheckSell(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
        logger.info("receiveAndCheckSell Received fanout 1 message: " + trdPayFlow);
        logger.info("this consumer only controll redeem payFlow message");
        //if sell failed then update ui_product_details product number back
        if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.FAILED.getStatus() && trdPayFlow
            .getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
            //记住 要和payService里面sellProd的做法一致，发送方也得用这个字段存储赎回基金数量
            Long fundQuantity = trdPayFlow.getTradeTargetShare();
            logger.info("now set the fund quantity back with userProdId:" + trdPayFlow.getUserProdId
                () + " fundQuantity:" + fundQuantity);
            uiProductDetailRepo.updateByAddBackQuantity(fundQuantity, TradeUtil.getUTCTime(),
                SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdPayFlow.getUserProdId(),
                trdPayFlow.getFundCode(), trdPayFlow.getTrdStatus());
        }else if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus() && trdPayFlow
            .getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
            logger.info("now update the product status to SELLWAITCONFIRM");
            uiProductDetailRepo.updateByParamForStatus(TradeUtil.getUTCTime(),
                SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdPayFlow.getUserProdId(),
                trdPayFlow.getFundCode(), trdPayFlow.getTrdStatus());
        }else if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus() && trdPayFlow
            .getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
            uiProductDetailRepo.updateByReedemConfirm(trdPayFlow.getTradeConfirmShare(),
                TradeUtil.getUTCTime(),SystemUserEnum.SYSTEM_USER_ENUM.getUserId(),trdPayFlow
                .getUserProdId(),trdPayFlow.getFundCode(),trdPayFlow.getTrdStatus());
        }else{
            logger.error("havent handling this kind of trdPayflow: of trdType:"+ trdPayFlow
                .getTrdType() + " status:" + trdPayFlow.getTrdStatus());
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();

    }
}
