package com.shellshellfish.aaas.userinfo.message;


import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.PendingRecordStatusEnum;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellPercentMsg;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.service.DataCollectionService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import com.shellshellfish.aaas.userinfo.service.PayGrpcService;
import com.shellshellfish.aaas.userinfo.service.impl.CalculateConfirmedAsset;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);


    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    MongoUserTrdLogMsgRepo mongoUserTrdLogMsgRepo;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    @Autowired
    OrderRpcService orderRpcService;

    @Autowired
    UserInfoBankCardsRepository userInfoBankCardsRepository;

    @Autowired
    PayGrpcService payGrpcService;

    @Autowired
    CalculateConfirmedAsset calculateConfirmedAsset;

    @Autowired
    DataCollectionService dataCollectionService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants.OPERATION_TYPE_UPDATE_UIPROD,
                    durable = "false"),
            exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
                    durable = "true"), key = RabbitMQConstants.ROUTING_KEY_USERINFO)
    )
    public void receiveMessage(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
            .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        logger.info("this consumer only controll buy payFlow message");
        if (trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.BUY.getOperation()) {
            logger.info("get buy update payFlow msg");
            try {
                UiProductDetail uiProductDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                        (trdPayFlow.getUserProdId(), trdPayFlow.getFundCode());
                uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
                uiProductDetail.setStatus(trdPayFlow.getTrdStatus());

                uiProductDetailRepo.save(uiProductDetail);
            } catch (Exception ex) {
                logger.error("exception:", ex);

            }
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:", e);
        }


    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
                    .OPERATION_TYPE_UPDATE_UITRDLOG, durable = "false"),
            exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
                    durable = "true"), key = RabbitMQConstants.ROUTING_KEY_USERINFO_TRDLOG)
    )
    public void receiveTradeMessage(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
            .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        MongoUiTrdLog mongoUiTrdLog = new MongoUiTrdLog();
        try {
            mongoUiTrdLog.setTradeConfirmSum(trdPayFlow.getTradeConfirmSum());
            mongoUiTrdLog.setTradeConfirmShare(trdPayFlow.getTradeConfirmShare());
            mongoUiTrdLog.setTradeTargetShare(trdPayFlow.getTradeTargetShare());
            mongoUiTrdLog.setTradeTargetSum(trdPayFlow.getTradeTargetSum());
            mongoUiTrdLog.setOperations(trdPayFlow.getTrdType());
            mongoUiTrdLog.setUserProdId(trdPayFlow.getUserProdId());
            mongoUiTrdLog.setUserId(trdPayFlow.getUserId());
            mongoUiTrdLog.setTradeStatus(trdPayFlow.getTrdStatus());

            mongoUiTrdLog.setOrderId(TradeUtil.getOrderIdByOutsideOrderNo(trdPayFlow
                .getOutsideOrderno(), trdPayFlow.getOrderDetailId()));


            if (trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.WAITPAY.getStatus() ||
                    trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()) {
                //等待支付金额就是下单请求时候的金额
                mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow.getTradeTargetSum()));
            } else if (trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus() ||
                    trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus()) {
                //等待赎回份额就是下单请求时候的份额
                mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow.getTradeTargetShare()));
                mongoUiTrdLog.setApplySerial(trdPayFlow.getApplySerial());
            } else if (trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()) {
                if (trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.BUY.getOperation()) {
                    mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow
                            .getTradeConfirmShare()));
                } else if (trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()) {
                    mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow
                            .getTradeConfirmSum()));
                }
                mongoUiTrdLog.setApplySerial(trdPayFlow.getApplySerial());
            }
            mongoUiTrdLog.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoUiTrdLog.setFundCode(trdPayFlow.getFundCode());
            mongoUiTrdLog.setTradeDate(trdPayFlow.getUpdateDate());
            mongoUserTrdLogMsgRepo.save(mongoUiTrdLog);
        } catch (Exception ex) {
            logger.error("exception:", ex);

        }
        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:", e);
        }


    }

//    @Transactional
//    @RabbitListener(bindings = @QueueBinding(
//        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
//            .OPERATION_TYPE_UPDATE_UITRDLOG, durable = "false"),
//        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
//            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_ORDSTATCHG)
//    )
//    public void receiveOrderStatusChangeMessage(OrderStatusChangeDTO orderStatusChangeDTO, Channel channel, @Header
//        (AmqpHeaders
//        .DELIVERY_TAG) long tag) throws Exception {
//        logger.info("Received fanout 1 message: " + orderStatusChangeDTO);
//        //update ui_products 和 ui_product_details
//        MongoUiTrdLog  mongoUiTrdLog = new MongoUiTrdLog();
//        try{
//            mongoUiTrdLog.setOperations(orderStatusChangeDTO.getOrderType());
//            mongoUiTrdLog.setUserProdId(orderStatusChangeDTO.getUserProdId());
//            mongoUiTrdLog.setUserId(orderStatusChangeDTO.getUserId());
//            mongoUiTrdLog.setTradeStatus(orderStatusChangeDTO.getOrderStatus());
//            mongoUiTrdLog.setLastModifiedDate(TradeUtil.getUTCTime());
//            mongoUiTrdLog.setTradeDate(orderStatusChangeDTO.getOrderDate());
//            mongoUserTrdLogMsgRepo.save(mongoUiTrdLog);
//        }catch (Exception ex){
//            logger.error("exception:",ex);
//
//        }
//        try {
//            channel.basicAck(tag, true);
//        } catch (IOException e) {
//            logger.error("exception:",ex);
//        }
//        latch.countDown();
//
//    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
                    .OPERATION_TYPE_CHECKSELL_ROLLBACK, durable = "false"),
            exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
                    durable = "true"), key = RabbitMQConstants.ROUTING_KEY_USERINFO_REDEEM)
    )
    public void receiveAndCheckSell(TrdPayFlow trdPayFlow) throws Exception {
        logger.info("receiveAndCheckSell Received fanout 1 message: " + trdPayFlow);
        logger.info("this consumer only controll redeem payFlow message");
        //if sell failed then update ui_product_details product number back
        try {
            UiProductDetail uiProductDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                    (trdPayFlow.getUserProdId(), trdPayFlow.getFundCode());
            String cardNumber = orderRpcService
                    .getBankCardNumberByUserProdId(trdPayFlow.getUserProdId());
            List<UiBankcard> uiBankcards = null;
            if (trdPayFlow.getUserId() == SystemUserEnum.SYSTEM_USER_ENUM.getUserId()) {
                uiBankcards = userInfoBankCardsRepository.findAllByCardNumber(cardNumber);
            } else {
                uiBankcards = userInfoBankCardsRepository.findAllByUserIdAndCardNumber
                        (trdPayFlow.getUserId(), cardNumber);
            }
            if (CollectionUtils.isEmpty(uiBankcards)) {
                logger.error(
                        "failed to find bankCard for this trdPayFlow message with trdPayFlow.getUserProdId():"
                                + trdPayFlow
                                .getUserProdId());
                return;
            }

            String userPid = uiBankcards.get(0).getUserPid();
            if (!StringUtils.isEmpty(trdPayFlow.getApplySerial()) && trdPayFlow.getApplySerial()
                    .equals
                            (uiProductDetail.getLastestSerial())) {
                logger.error("repeated trdPayFlow message received, just ignore it");
            } else if (trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.REDEEMFAILED.getStatus() &&
                    trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation() &&
                    uiProductDetail.getStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()) {
                //记住 要和payService里面sellProd的做法一致，发送方也得用这个字段存储赎回基金数量

                //赎回失败情况下把数量加回去，前提是状态已经是等待赎回， 否则作为重复请求忽略掉这个信息
                Long fundQuantity = trdPayFlow.getTradeTargetShare();
                Long caculatedFundQty = fundQuantity;
                if (MonetaryFundEnum.containsCode(trdPayFlow.getFundCode())) {
                    //monetary fund should caculate quantity by NetValue
                    if(trdPayFlow.getApplydateUnitvalue() == -1L){
                        logger.error("trdPayFlow.getApplydateUnitvalue() is not vaild value!");
                        //now try to get applyDate adjustedAdj if not save to PendingRecords in
                        // mongodb
                        getMoneyCodeNavAdjByDate(trdPayFlow.getFundCode(), trdPayFlow
                            .getTrdApplyDate());
                    }
                    List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfosFromZZ(userPid,
                            trdPayFlow.getFundCode(), 10);
                    Long fundUnitNet = getFundUnitNet(trdPayFlow.getFundCode(), fundNetInfos,
                            TradeUtil.getReadableDateTime(trdPayFlow.getCreateDate()).split("T")[0]
                                    .replace("-", ""));
                    caculatedFundQty = TradeUtil.getBigDecimalNumWithDivOfTwoLong(fundQuantity,
                            fundUnitNet).longValueExact();
                }

                logger.info(
                        "now set the fund quantity back with userProdId:" + trdPayFlow.getUserProdId
                                () + " fundQuantity:" + fundQuantity);
                uiProductDetail.setFundQuantityTrade(uiProductDetail.getFundQuantityTrade() +
                        caculatedFundQty.intValue());
                uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
                uiProductDetail.setStatus(trdPayFlow.getTrdStatus());
                uiProductDetailRepo.save(uiProductDetail);
//            uiProductDetailRepo.updateByAddBackQuantity(fundQuantity, TradeUtil.getUTCTime(),
//                SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdPayFlow.getUserProdId(),
//                trdPayFlow.getFundCode(), trdPayFlow.getTrdStatus(), TrdOrderStatusEnum.WAITSELL.getStatus());
            } else if (trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus()
                    && trdPayFlow
                    .getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()) {
                logger.info("now update the product status to SELLWAITCONFIRM");
                uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
                uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                uiProductDetail.setStatus(trdPayFlow.getTrdStatus());
                boolean haveSerialInPayFlow = true;
                if (StringUtils.isEmpty(trdPayFlow.getApplySerial())) {
                    logger.error("the apply serial is empty so it is an error ");
                    haveSerialInPayFlow = false;
                }
                if (StringUtils.isEmpty(uiProductDetail.getLastestSerial())) {
                    if (haveSerialInPayFlow) {
                        uiProductDetail.setLastestSerial(trdPayFlow.getApplySerial());
                    }
                } else {
                    if (haveSerialInPayFlow && !uiProductDetail.getLastestSerial()
                            .contains(trdPayFlow
                                    .getApplySerial())) {
                        Set<String> resultSet = TradeUtil.getSetFromString(uiProductDetail
                                .getLastestSerial(), "\\|");
                        StringBuilder sb = new StringBuilder();
                        resultSet.forEach(item -> sb.append(item).append("|"));
                        if (!resultSet.contains(trdPayFlow.getApplySerial())) {
                            sb.append(trdPayFlow.getApplySerial());
                        }
                        uiProductDetail.setLastestSerial(sb.toString());
                    }
                }
                uiProductDetailRepo.save(uiProductDetail);
//            uiProductDetailRepo.updateByParamForStatus(TradeUtil.getUTCTime(),
//                SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdPayFlow.getUserProdId(),
//                trdPayFlow.getFundCode(), trdPayFlow.getTrdStatus());
            }//这段逻辑已经实现在receiveConfirmInfo里面，
//        else if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus() && trdPayFlow
//            .getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
//            int status = trdPayFlow.getTrdStatus();
//            if(uiProductDetail.getStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()){
//                logger.info("because uiProductDetail.getStatus() is:" + uiProductDetail.getStatus
//                    () + " so the status should be kept for reject current redeem operation");
//                status = uiProductDetail.getStatus();
//            }
//            Long delta = trdPayFlow.getTradeTargetShare() - trdPayFlow.getTradeConfirmShare();
//            //delta need to be add back to the origin trade quantity
//            int remainQuantity = uiProductDetail.getFundQuantity() - trdPayFlow
//                .getTradeConfirmShare().intValue();
//            if (remainQuantity < 0 ){
//                logger.error("super super error! current fundQuantity is :" + uiProductDetail
//                    .getFundQuantity() + " will deduct confirmed redeem of quantity:" +
//                    trdPayFlow.getTradeConfirmShare());
//            }
//            uiProductDetail.setFundQuantity(uiProductDetail.getFundQuantity() - trdPayFlow
//                .getTradeConfirmShare().intValue());
//            uiProductDetail.setStatus(status);
//            uiProductDetail.setFundQuantityTrade(uiProductDetail.getFundQuantityTrade() + delta.intValue());
//            uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
//            uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
//            if( !StringUtils.isEmpty(uiProductDetail.getLastestSerial()) && !uiProductDetail
//                .getLastestSerial().contains(trdPayFlow.getApplySerial())){
//                logger.error("received repeated confirm message :" + trdPayFlow.getApplySerial());
//            }else if(StringUtils.isEmpty(uiProductDetail.getLastestSerial())){
//                logger.info("it is initial, let's handle this message ");
//                uiProductDetailRepo.save(uiProductDetail);
//            }else if(!StringUtils.isEmpty(uiProductDetail.getLastestSerial()) && uiProductDetail
//                .getLastestSerial().contains(trdPayFlow.getApplySerial())){
//                String[] serials = uiProductDetail.getLastestSerial().split("|");
//                StringBuilder sb = new StringBuilder();
//                for(String serial: serials){
//                    if(serial.equals(trdPayFlow.getApplySerial())){
//                        logger.info("got the history stored serial:"+trdPayFlow.getApplySerial());
//                        continue;
//                    }else{
//                        sb.append(serial).append("|");
//                    }
//                }
//                uiProductDetail.setLastestSerial(sb.toString());
//                uiProductDetailRepo.save(uiProductDetail);
//            }
//
//        }
            else {
                logger.error("havent handling this kind of trdPayflow: of trdType:" + trdPayFlow
                        .getTrdType() + " status:" + trdPayFlow.getTrdStatus());
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
                    .OPERATION_TYPE_UPDATE_UIPRODQUANTITY, durable = "false"),
            exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
                    durable = "true"), key = RabbitMQConstants.ROUTING_KEY_USERINFO_CFMLOG)
    )
    public void receiveConfirmInfo(MongoUiTrdZZInfo mongoUiTrdZZInfo) throws Exception {
        com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo mongoUiTrdZZInfoInDb = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndUserIdAndApplySerial(mongoUiTrdZZInfo
                        .getUserProdId(), mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getApplySerial());
        try {
            if (mongoUiTrdZZInfoInDb == null) {
                mongoUiTrdZZInfoInDb = new com.shellshellfish.aaas.userinfo.model.dao
                        .MongoUiTrdZZInfo();
                MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo, mongoUiTrdZZInfoInDb);
                mongoUiTrdZZInfoRepo.save(mongoUiTrdZZInfoInDb);
            } else {
                String idOrig = mongoUiTrdZZInfoInDb.getId();
                MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo, mongoUiTrdZZInfoInDb);
                mongoUiTrdZZInfoInDb.setTradeType(mongoUiTrdZZInfo.getTradeType());
                mongoUiTrdZZInfoInDb.setId(idOrig);
                mongoUiTrdZZInfoRepo.save(mongoUiTrdZZInfoInDb);
            }
        } catch (Exception ex) {
            logger.error("exception:", ex);
        }

    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
                    .OPERATION_TYPE_UPDATE_UITRDCONFIRMINFO, durable = "false"),
            exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
                    durable = "true"), key = RabbitMQConstants.ROUTING_KEY_USERINFO_UPDATEPROD)
    )
    public void receiveConfirmInfo(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel channel, @Header
            (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            logger.info("received mongoUiTrdZZInfo to update assects with userProdId:{} "
                            + "userId:{} fundCode:{} ", mongoUiTrdZZInfo.getUserProdId(),
                    mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
//            if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation()) {
//                logger.info("updateBuyProductQty is deprecated");
////                updateBuyProductQty(mongoUiTrdZZInfo);
//            } else if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.REDEEM
//                    .getOperation()) {
//                logger.info("updateRedeemProductQty is deprecated");
////                updateRedeemProductQty(mongoUiTrdZZInfo);
//            } else {
//                logger.error("cannot handle this mongoUiTrdZZInfo with trdType:{}", mongoUiTrdZZInfo
//                        .getTradeType());
//            }

            calculateConfirmedAsset.calculateConfirmedAsset(mongoUiTrdZZInfo);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_CONFIRM_PENDINGRECORDS, durable = "false"),
        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_CONFIRM_PENDINGRECORDS)
    )
    public void receiveConfirmInfoUpdateProdQty(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            logger.info("received mongoUiTrdZZInfo to update assects with userProdId:{} "
                    + "userId:{} fundCode:{} ", mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
            if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation()) {
                logger.info("updateBuyProductQty is deprecated");
                caculateBuyProductQty(mongoUiTrdZZInfo);
            } else if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.REDEEM
                .getOperation()) {
                logger.info("updateRedeemProductQty is deprecated");
                caculateRedeemProductQty(mongoUiTrdZZInfo);
            } else {
                logger.error("cannot handle this mongoUiTrdZZInfo with trdType:{}", mongoUiTrdZZInfo
                    .getTradeType());
            }

            calculateConfirmedAsset.calculateConfirmedAsset(mongoUiTrdZZInfo);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    private void caculateRedeemProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_prod_id").is(mongoUiTrdZZInfo.getUserProdId()).and
            ("fund_code").is(mongoUiTrdZZInfo.getFundCode()).and("outside_order_id").is
            (mongoUiTrdZZInfo.getOutSideOrderNo()));
        List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
        MongoPendingRecords mongoPendingRecordsRemain = null;
        Long lastModifyDate = null;
        if(CollectionUtils.isEmpty(mongoPendingRecords)){

            mongoPendingRecordsRemain = getPatchMongoPendingRecordFromZZInfo(mongoUiTrdZZInfo);

        }

        if(mongoPendingRecords.size() > 1){
            mongoPendingRecordsRemain = mongoPendingRecords.get(0);
            for(MongoPendingRecords mongoPendingRecordsItem: mongoPendingRecords){
                if(mongoPendingRecordsItem.getLastModifiedDate() != null && lastModifyDate <
                    mongoPendingRecordsItem.getLastModifiedDate()){
                    mongoPendingRecordsRemain = mongoPendingRecordsItem;
                }
            }
            mongoTemplate.remove(query, "ui_pending_records");
        }else if(mongoPendingRecordsRemain == null &&  mongoPendingRecords.size() == 1){
            mongoPendingRecordsRemain = mongoPendingRecords.get(0);
        }else if(mongoPendingRecordsRemain == null){
            logger.error("This is a dirty data because there is no order info for it");
            return;
        }
        if(mongoPendingRecordsRemain.getProcessStatus() == PendingRecordStatusEnum.HANDLED
            .getStatus()){
            logger.error("received dupliated mongoUiTrdZZInfo for user_prod_id:{} "
                    + "fund_code:{} outside_order_id:{}",mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getFundCode(),  mongoUiTrdZZInfo.getOutSideOrderNo() );
            return;
        }else{
            UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
            //开始处理货币基金
            if(MonetaryFundEnum.containsCode(mongoUiTrdZZInfo.getFundCode())){
                Long navadj = mongoPendingRecordsRemain.getApplyDateNavadj();
                if(navadj == null || navadj < 0){
                    navadj = getMoneyCodeNavAdjByDate(mongoUiTrdZZInfo.getFundCode(),
                        mongoPendingRecordsRemain.getApplyDateStr());
                }
                if(navadj == null || navadj< 0){
                    logger.error("cannot get navadj value for:{} of :{}", mongoPendingRecordsRemain
                        .getFundCode(), mongoPendingRecordsRemain.getApplyDateStr());
                    mongoPendingRecordsRemain.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED
                        .getStatus());
                    mongoPendingRecordsRemain.setLastModifiedDate(TradeUtil.getUTCTime());
                    mongoTemplate.save(mongoPendingRecordsRemain,"ui_pending_records");
                    return;
                }else{
                    mongoPendingRecordsRemain.setApplyDateNavadj(navadj);
                }

                Long confirmedQuantity = mongoUiTrdZZInfo.getTradeConfirmShare();
                BigDecimal abstractShares = TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundDown(confirmedQuantity*1000000L, navadj);
                Integer valueCaculated =  productDetail.getFundQuantity() - abstractShares
                    .intValue();
                logger.info("origin quantity is:{} now:{}", productDetail.getFundQuantity(),
                    valueCaculated);
                productDetail.setFundQuantity(valueCaculated);
            }
            //非货币基金
            else{
                Integer valueCaculated = productDetail.getFundQuantity() - mongoUiTrdZZInfo
                    .getTradeConfirmShare().intValue();
                    logger.info("origin quantity is:{} now:{}", productDetail.getFundQuantity(),
                    valueCaculated);
                productDetail.setFundQuantity(valueCaculated);
            }
            productDetail.setUpdateDate(TradeUtil.getUTCTime());
            //表示该记录需要重新生成证据链标签
            productDetail.setLastestSerial("");
            uiProductDetailRepo.save(productDetail);
            mongoPendingRecordsRemain.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
            mongoPendingRecordsRemain.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoTemplate.save(mongoPendingRecordsRemain,"ui_pending_records");
        }

    }

    private void caculateBuyProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo) {

        Query query = new Query();
        query.addCriteria(Criteria.where("user_prod_id").is(mongoUiTrdZZInfo.getUserProdId()).and
            ("fund_code").is(mongoUiTrdZZInfo.getFundCode()).and("outside_order_id").is
            (mongoUiTrdZZInfo.getOutSideOrderNo()));
        List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
        MongoPendingRecords mongoPendingRecordsRemain = null;
        if(CollectionUtils.isEmpty(mongoPendingRecords)){
            logger.info("use old style method to update quantity, this style will only last 1 "
                + "month from 2018-06-05");
            mongoPendingRecordsRemain = getPatchMongoPendingRecordFromZZInfo(mongoUiTrdZZInfo);
//            updateBuyProductQty(mongoUiTrdZZInfo);
        }

        //pendingRecords 对每一个outsideOrderId应该只有一条记录，取lastModifiedDate 不为空，
        //而且值比较大的那条记录为准
        if(mongoPendingRecords.size() > 1){

            Long lastModifiedDate = 0L;
            for(MongoPendingRecords mongoPendingRecordsCheck: mongoPendingRecords){
                if(mongoPendingRecordsCheck.getLastModifiedDate() != null && lastModifiedDate <
                    mongoPendingRecordsCheck.getLastModifiedDate()){
                    lastModifiedDate = mongoPendingRecordsCheck.getLastModifiedDate();
                    mongoPendingRecordsRemain = mongoPendingRecordsCheck;
                }
            }
            //清空冗余数据
            mongoTemplate.remove(query, "ui_pending_records");
            MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo,mongoPendingRecordsRemain );

        }else if(mongoPendingRecordsRemain == null &&  mongoPendingRecords.size() == 1){
            mongoPendingRecordsRemain = mongoPendingRecords.get(0);
        }else if(mongoPendingRecordsRemain == null){
            logger.error("This is a dirty data because there is no order info for it");
            return;
        }

        if(mongoPendingRecordsRemain.getProcessStatus() == PendingRecordStatusEnum.HANDLED
            .getStatus()){
            logger.error("received dupliated mongoUiTrdZZInfo for user_prod_id:{} "
                + "fund_code:{} outside_order_id:{}",mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getFundCode(),  mongoUiTrdZZInfo.getOutSideOrderNo() );
            return;
        }else{


            UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
            //开始处理货币基金
            if(MonetaryFundEnum.containsCode(mongoUiTrdZZInfo.getFundCode())){
                Long navadj = mongoPendingRecordsRemain.getApplyDateNavadj();
                if(navadj == null || navadj < 0){
                    navadj = getMoneyCodeNavAdjByDate(mongoUiTrdZZInfo.getFundCode(),
                        mongoPendingRecordsRemain.getApplyDateStr());
                }
                if(navadj == null || navadj< 0){
                    logger.error("cannot get navadj value for:{} of :{}", mongoPendingRecordsRemain
                        .getFundCode(), mongoPendingRecordsRemain.getApplyDateStr());
                    mongoPendingRecordsRemain.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED
                        .getStatus());
                    mongoPendingRecordsRemain.setLastModifiedDate(TradeUtil.getUTCTime());
                    mongoTemplate.save(mongoPendingRecordsRemain,"ui_pending_records");
                    return;
                }else{
                    mongoPendingRecordsRemain.setApplyDateNavadj(navadj);
                }

                Long confirmedQuantity = mongoUiTrdZZInfo.getTradeConfirmShare();
                BigDecimal abstractShares = TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundDown(confirmedQuantity*1000000L, navadj);
                Integer valueCaculated =  productDetail.getFundQuantity() + abstractShares.intValue();
                logger.info("origin quantity is:{} now:{}", productDetail.getFundQuantity(),
                    valueCaculated);
                productDetail.setFundQuantity(valueCaculated);

            }
            //非货币基金
            else{
                Integer valueCaculated = 0;
                if(productDetail.getFundQuantity() != null){
                    valueCaculated =  mongoUiTrdZZInfo.getTradeConfirmShare().intValue() +
                        productDetail.getFundQuantity();
                }else{
                    valueCaculated = mongoUiTrdZZInfo.getTradeConfirmShare().intValue();
                }

                logger.info("origin quantity is:{} now:{}", productDetail.getFundQuantity(),
                    valueCaculated);
                productDetail.setFundQuantity(valueCaculated);
            }
            productDetail.setUpdateDate(TradeUtil.getUTCTime());
            //表示该记录需要重新生成证据链标签
            productDetail.setLastestSerial("");
            uiProductDetailRepo.save(productDetail);
            mongoPendingRecordsRemain.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
            mongoPendingRecordsRemain.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoTemplate.save(mongoPendingRecordsRemain,"ui_pending_records");
        }

    }

    /**
     * 这段程序要被 用pendingRecords 里面的记录的算法替代
     * @param mongoUiTrdZZInfo
     * @return
     */
    @Deprecated
    private boolean updateBuyProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo) {
        UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
        String cardNumber = orderRpcService.getBankCardNumberByUserProdId(mongoUiTrdZZInfo
                .getUserProdId());
        List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findAllByUserIdAndCardNumber
                (mongoUiTrdZZInfo.getUserId(), cardNumber);
        String userPid = uiBankcards.get(0).getUserPid();


        if (productDetail.getFundQuantityTrade() != null && productDetail
                .getFundQuantityTrade() > 0) {
            logger.error("abnormal situation appeared the userProdId:" + mongoUiTrdZZInfo
                    .getUserProdId() + " initial quantity should be 0 or null but already "
                    + "have number , need check why, but curent we reset the quantity "
                    + "according to the confirm message ");
        }
        //1. 普通基金 当前份额 + PendingRecords （未处理，同一个类型交易的，同一个outsideOrderId的记录
        // 里面的份额)
        //2 货币基金 当前份额 + PendingRecords (未处理， 同一个类型交易的， 同一个outsideOrderId的记录
        // 里的原始份额/申请日的navadj )


        if (MonetaryFundEnum.containsCode(productDetail.getFundCode())) {
            //monetary fund should caculate quantity by NetValue
            Long trdCfmShare = mongoUiTrdZZInfo.getTradeConfirmShare();
//            List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfosFromZZ(userPid,
//                    mongoUiTrdZZInfo.getFundCode(), 10);
//            Long fundUnitNet = getFundUnitNet(productDetail.getFundCode(), fundNetInfos,
//                    mongoUiTrdZZInfo.getApplyDate());
            Long navadj = getMoneyCodeNavAdjByDate(mongoUiTrdZZInfo.getFundCode(), mongoUiTrdZZInfo
                .getApplyDate());
            Long caculatedFundQty = TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundUp
                (trdCfmShare*1000000L, navadj).longValueExact();
            productDetail.setFundQuantity(caculatedFundQty.intValue());
            productDetail.setFundQuantityTrade(caculatedFundQty.intValue());
        } else {
            productDetail.setFundQuantityTrade(mongoUiTrdZZInfo.getTradeConfirmShare().intValue());
            productDetail.setFundQuantity(mongoUiTrdZZInfo.getTradeConfirmShare().intValue());
        }
        productDetail.setUpdateDate(TradeUtil.getUTCTime());
        uiProductDetailRepo.save(productDetail);
        return true;
    }

    /**
     * 这段程序要被 用pendingRecords 里面的记录的算法替代
     * @param mongoUiTrdZZInfo
     * @return
     */
    @Deprecated
    private boolean updateRedeemProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo) {

        UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
                (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
        String cardNumber = orderRpcService.getBankCardNumberByUserProdId(mongoUiTrdZZInfo
                .getUserProdId());
        List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findAllByUserIdAndCardNumber
                (mongoUiTrdZZInfo.getUserId(), cardNumber);
        String userPid = uiBankcards.get(0).getUserPid();

        productDetail.setStatus(TrdOrderStatusEnum.SELLCONFIRMED.getStatus());
        if (productDetail.getFundQuantityTrade() != null && productDetail
                .getFundQuantityTrade() < 0) {
            logger.error("abnormal situation appeared the userProdId:" + mongoUiTrdZZInfo
                    .getUserProdId() + " initial quantity should be 0 or null but already "
                    + "have number , need check why, but curent we reset the quantity "
                    + "according to the confirm message ");
        }
        if (MonetaryFundEnum.containsCode(productDetail.getFundCode())) {
            //monetary fund should caculate quantity by NetValue
            Long trdCfmShare = mongoUiTrdZZInfo.getTradeConfirmShare();
//            List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfosFromZZ(userPid,
//                    mongoUiTrdZZInfo.getFundCode(), 10);
//            Long fundUnitNet = getFundUnitNet(productDetail.getFundCode(), fundNetInfos,
//                    mongoUiTrdZZInfo.getApplyDate());
            Long navadj = getMoneyCodeNavAdjByDate(mongoUiTrdZZInfo.getFundCode(), mongoUiTrdZZInfo
                .getApplyDate());
            Long caculatedFundQty = TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundUp
                (trdCfmShare*1000000L,
                    navadj).longValueExact();
            Long remainQty = productDetail.getFundQuantity() - caculatedFundQty;
            if (remainQty < 0) {
                logger.error("abnormal situation appeared for the userProdId:{} current "
                        + "quantity:{} the redeem confirm quantity:{}", mongoUiTrdZZInfo
                        .getUserProdId(), productDetail.getFundQuantity(), mongoUiTrdZZInfo
                        .getTradeConfirmShare());
                return false;
            }
            productDetail.setFundQuantity(Math.toIntExact(remainQty));
            productDetail.setFundQuantityTrade(Math.toIntExact(remainQty));

        } else {
            Long remainQty = productDetail.getFundQuantity() - mongoUiTrdZZInfo
                    .getTradeConfirmShare();
            if (remainQty < 0) {
                logger.error("abnormal situation appeared for the userProdId:{} current "
                        + "quantity:{} the redeem confirm quantity:{}", mongoUiTrdZZInfo
                        .getUserProdId(), productDetail.getFundQuantity(), mongoUiTrdZZInfo
                        .getTradeConfirmShare());
                return false;
            }
            productDetail.setFundQuantityTrade(Math.toIntExact(remainQty));
            productDetail.setFundQuantity(Math.toIntExact(remainQty));
        }
        if (StringUtils.isEmpty(mongoUiTrdZZInfo.getApplySerial())) {
            logger.error("abnormal message of mongoUiTrdZZInfo, there is no applySerial in "
                    + "mongoUiTrdZZInfo:" + mongoUiTrdZZInfo.getApplySerial());
            return false;
        }
        if (!StringUtils.isEmpty(productDetail.getLastestSerial()) && !productDetail
                .getLastestSerial().contains(mongoUiTrdZZInfo.getApplySerial())) {
            logger.error("received repeated confirm message :" + mongoUiTrdZZInfo.getApplySerial());
            return false;
        } else if (StringUtils.isEmpty(productDetail.getLastestSerial())) {
            logger.info("this serial:{} already handled, let's ignore this message ",
                    mongoUiTrdZZInfo.getApplySerial());
            return false;
//            uiProductDetailRepo.save(productDetail);
        } else if (!StringUtils.isEmpty(productDetail.getLastestSerial()) && productDetail
                .getLastestSerial().contains(mongoUiTrdZZInfo.getApplySerial())) {
            String[] serials = productDetail.getLastestSerial().split("\\|");
            StringBuilder sb = new StringBuilder();
            for (String serial : serials) {
                if (serial.equals(mongoUiTrdZZInfo.getApplySerial())) {
                    logger.info("got the history stored serial:" + mongoUiTrdZZInfo.getApplySerial());
                    continue;
                } else {
                    sb.append(serial).append("|");
                }
            }
            productDetail.setLastestSerial(sb.toString());
        }
        productDetail.setUpdateDate(TradeUtil.getUTCTime());
        uiProductDetailRepo.save(productDetail);
        return true;
    }

    private Long getFundUnitNet(String fundCode, List<FundNetInfo> fundNetInfos, String tradeDate) {
        for (FundNetInfo fundNetInfo : fundNetInfos) {
            if (fundNetInfo.getFundCode().equals(fundCode) && fundNetInfo.getTradedate().equals(tradeDate)) {
                return TradeUtil.getLongNumWithMul100(fundNetInfo.getUnitNet());
            }
        }
        logger.error("Failed to find unit net for :{} and tradeDate:{}", fundCode, tradeDate);
        return -1L;
    }

//    @Transactional
//    @RabbitListener(bindings = @QueueBinding(
//        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
//            .OPERATION_TYPE_CACULATE_UIACCECTS, durable = "false"),
//        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
//            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_UPDATEPROD)
//    )
//    public void receiveConfirmInfoUpdateAssects(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel channel,
//        @Header
//        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
//        try {
//            // now update correspond product_detail for quantity of fund
//            logger.info("received mongoUiTrdZZInfo to update assects with userProdId:{} "
//                    + "userId:{} fundCode:{} ", mongoUiTrdZZInfo.getUserProdId(),
//                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
//            calculateConfirmedAsset.calculateConfirmedAsset(mongoUiTrdZZInfo.getUserProdId(),
//                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
//        }catch(Exception ex){
//            logger.error("Exception:", ex);
//        }
//
//    }



    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_BUY_PENDINGRECORDS, durable = "false"),
        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_UPDATE_BUY_PENDINGRECORDS)
    )
    public void receiveBuyOrderMsg(PayOrderDto payOrderDto, Channel channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        //目的是设置orderid
        logger.info("received message in :{}", RabbitMQConstants.OPERATION_TYPE_UPDATE_BUY_PENDINGRECORDS);
        List<TrdOrderDetail> trdOrderDetails =  payOrderDto.getOrderDetailList();
        for(TrdOrderDetail trdOrderDetail: trdOrderDetails){
            Query query = new Query();
            query.addCriteria(Criteria.where("user_prod_id").is(trdOrderDetail.getUserProdId()).and
                ("fund_code").is(trdOrderDetail.getFundCode()).and("outside_order_id").is(trdOrderDetail
                .getOrderId()+trdOrderDetail.getId()));
            List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
            if(CollectionUtils.isEmpty(mongoPendingRecords)){
                //no record found, so we should add the pendingRecord based on trdOrderDetail
                logger.info("it is normal that in current stage there should be no pendingRecord "
                    + "there find pendingRecord based on user_prod_id:{} fundCode:{} and "
                    + "outsideOrderNo:{} we can try use empty orderId",  trdOrderDetail
                    .getUserProdId(), trdOrderDetail.getFundCode(),(trdOrderDetail.getOrderId()+trdOrderDetail.getId()));
                Query queryWithEmptyOrderId = new Query();
                queryWithEmptyOrderId.addCriteria(Criteria.where("user_prod_id").is(trdOrderDetail.getUserProdId()).and
                    ("fund_code").is(trdOrderDetail.getFundCode()).and("outside_order_id").is
                    (null));
                List<MongoPendingRecords> mongoPendingRecordsWithEmptyOrderIds = mongoTemplate.find
                    (queryWithEmptyOrderId, MongoPendingRecords.class);
                if(!CollectionUtils.isEmpty(mongoPendingRecordsWithEmptyOrderIds)){
                    MongoPendingRecords mongoPendingRecordsHistory = null;

                    if(mongoPendingRecordsWithEmptyOrderIds.size() == 1){
                        mongoPendingRecordsHistory =
                            mongoPendingRecordsWithEmptyOrderIds.get(0);
                    }
                    if(mongoPendingRecordsWithEmptyOrderIds.size() > 1){
                        logger.error("there are redundunt records there ");
                        for(MongoPendingRecords item: mongoPendingRecordsWithEmptyOrderIds){
                            if( item.getTradeType() == Integer.MIN_VALUE || item.getTradeType() ==
                                trdOrderDetail.getTradeType()){
                                logger.info("there is uninitiallized type");
                                mongoPendingRecordsHistory = item;
                                break;
                            }
                        }
                    }
                    if(mongoPendingRecordsHistory != null){
                        updateHistoryBuyPendRecordByOrderDetail(mongoPendingRecordsHistory, trdOrderDetail);
                        mongoTemplate.save(mongoPendingRecordsHistory, "ui_pending_records");
                    }
                }else{
                    MongoPendingRecords mongoPendingRecordsPatched =  getPathMongoPendingRecord(trdOrderDetail);
                    mongoTemplate.save(mongoPendingRecordsPatched, "ui_pending_records");
                }
            }
            else{
                MongoPendingRecords mongoPendingRecordsHistory = null;
                if(mongoPendingRecords.size() >= 1){
                    logger.error("There should be only 1 pendingRecord there, but there is more "
                        + "than 1:{}", mongoPendingRecords.size());
                    for(MongoPendingRecords item: mongoPendingRecords){
                        if(item.getProcessStatus() == PendingRecordStatusEnum.HANDLED.getStatus()){
                            continue;
                        }else{
                            mongoPendingRecordsHistory = item;
                            break;
                        }
                    }
                }
                if(mongoPendingRecordsHistory != null){
                    updateHistoryBuyPendRecordByOrderDetail(mongoPendingRecordsHistory, trdOrderDetail);
                    mongoTemplate.save(mongoPendingRecordsHistory, "ui_pending_records");
                }else{
                    logger.error("All pendingRecord with outside_order_id:{} is handled",trdOrderDetail.getOrderId()+trdOrderDetail.getId() );
                }
            }

        }
    }

    private void updateHistoryBuyPendRecordByOrderDetail(MongoPendingRecords mongoPendingRecords,
        TrdOrderDetail trdOrderDetail){
        mongoPendingRecords.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED
            .getStatus());
        mongoPendingRecords.setTradeStatus(trdOrderDetail.getOrderDetailStatus());
        mongoPendingRecords.setTradeType(trdOrderDetail.getTradeType());
        mongoPendingRecords.setOutsideOrderId(trdOrderDetail.getOrderId()+trdOrderDetail.getId());
        mongoPendingRecords.setOrderId(trdOrderDetail.getOrderId());
        mongoPendingRecords.setTradeTargetShare(trdOrderDetail.getFundNum());
        mongoPendingRecords.setTradeTargetSum(trdOrderDetail.getFundSum());
        mongoPendingRecords.setCreatedDate(TradeUtil.getUTCTime());
    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_SELL_PENDINGRECORDS, durable = "false"),
        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_UPDATE_SELL_PENDINGRECORDS)
    )
    public void receiveSellOrderMsg(ProdSellPercentMsg sellPercentMsg, Channel channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        //目的是设置orderId
        logger.info("received message in :{}", RabbitMQConstants
            .OPERATION_TYPE_UPDATE_SELL_PENDINGRECORDS);
        List<ProdDtlSellDTO> prodDtlSellDTOS =  sellPercentMsg.getProdDtlSellDTOList();
        String orderId = sellPercentMsg.getOrderId();
        for(ProdDtlSellDTO prodDtlSellDTO: prodDtlSellDTOS){
            Query query = new Query();
            query.addCriteria(Criteria.where("user_prod_id").is(prodDtlSellDTO.getUserProdId()).and
                ("fund_code").is(prodDtlSellDTO.getFundCode()).and("outside_order_id").is
                (orderId +prodDtlSellDTO.getOrderDetailId()));
            List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
            if(CollectionUtils.isEmpty(mongoPendingRecords)){
                //no record found, so we should 1. check if there is empty orderId record
                // 2. add the pendingRecord based on trdOrderDetail
                logger.error("failed to find pendingRecord based on user_prod_id:{} fundCode:{} "
                    + "and outsideOrderNo:{}", prodDtlSellDTO.getUserProdId(), prodDtlSellDTO
                    .getFundCode(),(sellPercentMsg.getOrderId()+prodDtlSellDTO.getOrderDetailId()));
                Query queryWithEmptyOrderId = new Query();
                query.addCriteria(Criteria.where("user_prod_id").is(prodDtlSellDTO.getUserProdId()).and
                    ("fund_code").is(prodDtlSellDTO.getFundCode()).and("outside_order_id").is
                    (null));
                List<MongoPendingRecords> mongoPendingRecordsWithEmptyOrderIds = mongoTemplate.find
                    (query, MongoPendingRecords.class);
                if(!CollectionUtils.isEmpty(mongoPendingRecordsWithEmptyOrderIds)){
                    MongoPendingRecords mongoPendingRecordsHistory = null;
                    if(mongoPendingRecordsWithEmptyOrderIds.size() == 1){
                        mongoPendingRecordsHistory =
                            mongoPendingRecordsWithEmptyOrderIds.get(0);
                    }
                    if(mongoPendingRecordsWithEmptyOrderIds.size() > 1){
                        logger.error("there are redundunt records there ");
                        for(MongoPendingRecords item: mongoPendingRecordsWithEmptyOrderIds){
                            if( item.getTradeType() == Integer.MIN_VALUE || item.getTradeType() ==
                                TrdOrderOpTypeEnum.REDEEM.getOperation()){
                                logger.info("there is uninitiallized type");
                                mongoPendingRecordsHistory = item;
                                break;
                            }
                        }
                    }
                    if(mongoPendingRecordsHistory != null && mongoPendingRecordsHistory
                        .getProcessStatus() != PendingRecordStatusEnum.HANDLED.getStatus()){
                        updateHistorySellPendRecordByProdDtlSellDTO(mongoPendingRecordsHistory,
                            prodDtlSellDTO, orderId);
                        mongoTemplate.save(mongoPendingRecordsHistory, "ui_pending_records");
                    }
                }else{
                    MongoPendingRecords mongoPendingRecordsPatched =  getPathMongoPendingRecord
                        (prodDtlSellDTO, orderId);
                    mongoTemplate.save(mongoPendingRecordsPatched, "ui_pending_records");
                }
            }else{
                MongoPendingRecords mongoPendingRecordsHistory = null;
                if(mongoPendingRecords.size() >= 1){
                    logger.error("There should be only 1 pendingRecord there, but there is more "
                        + "than 1:{}", mongoPendingRecords.size());
                    for(MongoPendingRecords item: mongoPendingRecords){
                        if(item.getProcessStatus() == PendingRecordStatusEnum.HANDLED.getStatus()){
                            continue;
                        }else{
                            mongoPendingRecordsHistory = item;
                            break;
                        }
                    }
                }
                if(mongoPendingRecordsHistory != null){
                    updateHistorySellPendRecordByProdDtlSellDTO(mongoPendingRecordsHistory,
                        prodDtlSellDTO, orderId);
                    mongoTemplate.save(mongoPendingRecordsHistory, "ui_pending_records");
                }else{
                    logger.error("All pendRecord with outside_order_id:{} is handled", orderId+prodDtlSellDTO.getId());
                }

            }
        }
    }

    private void updateHistorySellPendRecordByProdDtlSellDTO(MongoPendingRecords mongoPendingRecordsHistory,
        ProdDtlSellDTO prodDtlSellDTO, String orderId) {
        mongoPendingRecordsHistory.setOrderId(orderId);
        mongoPendingRecordsHistory.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        mongoPendingRecordsHistory.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsHistory.setTradeStatus(TrdOrderStatusEnum.WAITSELL.getStatus());
        mongoPendingRecordsHistory.setOutsideOrderId(orderId+prodDtlSellDTO.getId());
        mongoPendingRecordsHistory.setOrderId(orderId);
        mongoPendingRecordsHistory.setTradeTargetShare(new Long(prodDtlSellDTO.getFundQuantity()));
        mongoPendingRecordsHistory.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO.getTargetSellAmount
            ()));
    }


    private MongoPendingRecords getMongoPendingRecordWithEmptyOrderId(List<MongoPendingRecords>
        mongoPendingRecords){

        if(CollectionUtils.isEmpty(mongoPendingRecords)){
            return null;
        }else{
            for(MongoPendingRecords mongoPendingRecords2: mongoPendingRecords){
                if(StringUtils.isEmpty(mongoPendingRecords2.getOrderId())){

                    return mongoPendingRecords2;
                }
            }
        }
        return null;
    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_FAILED_PENDINGRECORDS, durable = "false"),
        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_FAILED_PENDINGRECORDS)
    )
    public void receiveFailedMsg(TrdPayFlow trdPayFlow, Channel channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        //目的是设置orderId
        logger.info("received message in :{}", RabbitMQConstants
            .OPERATION_TYPE_FAILED_PENDINGRECORDS);
        Query query = new Query();
        query.addCriteria(Criteria.where("user_prod_id").is(trdPayFlow.getUserProdId()).and
            ("fund_code").is(trdPayFlow.getFundCode()).and("order_id").is
            (trdPayFlow.getOutsideOrderno()));
        List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
        if(CollectionUtils.isEmpty(mongoPendingRecords)){
            logger.error("There is no pendingRecords with user_prod_id:{} and order_id:{} and "
                + "fund_code:{}", trdPayFlow.getUserProdId(), trdPayFlow.getOutsideOrderno(),
                trdPayFlow.getFundCode() );
            MongoPendingRecords mongoPendingRecordsPatched = getPatchMongoPendingRecord(trdPayFlow);
            mongoPendingRecordsPatched.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
            mongoTemplate.save(mongoPendingRecordsPatched, "ui_pending_records");
            return;
        }
        else{
            if(mongoPendingRecords.get(0).getProcessStatus() == PendingRecordStatusEnum.HANDLED.getStatus()){
                logger.error("The pendingRecords with user_prod_id:{} and order_id:{} and "
                    + "fund_code:{} already in handled status", trdPayFlow.getUserProdId(),
                    trdPayFlow.getOutsideOrderno(), trdPayFlow.getFundCode());
            }else{
                mongoPendingRecords.get(0).setTradeStatus(trdPayFlow.getTrdStatus());
                mongoPendingRecords.get(0).setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
                mongoTemplate.save(mongoPendingRecords, "ui_pending_records");
            }

        }
    }

//    @Transactional
//    @RabbitListener(bindings = @QueueBinding(
//        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
//            .OPERATION_TYPE_FAILED_SELL_PENDINGRECORDS, durable = "false"),
//        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
//            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_FAILED_SELL_PENDINGRECORDS)
//    )
//    public void receiveSellFailedMsg(TrdPayFlow trdPayFlow, Channel channel, @Header
//        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
//        //目的是设置orderId
//        logger.info("received message in :{}", RabbitMQConstants
//            .OPERATION_TYPE_FAILED_SELL_PENDINGRECORDS);
//        Query query = new Query();
//        query.addCriteria(Criteria.where("user_prod_id").is(trdPayFlow.getUserProdId()).and
//            ("fund_code").is(trdPayFlow.getFundCode()).and("order_id").is
//            (trdPayFlow.getOutsideOrderno()));
//        List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
//        if(CollectionUtils.isEmpty(mongoPendingRecords)){
//            logger.error("There is no pendingRecords with user_prod_id:{} and order_id:{} and "
//                    + "fund_code:{}", trdPayFlow.getUserProdId(), trdPayFlow.getOutsideOrderno(),
//                trdPayFlow.getFundCode() );
//            MongoPendingRecords mongoPendingRecordsPatched = getPatchMongoPendingRecord(trdPayFlow);
//            mongoPendingRecordsPatched.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
//            mongoTemplate.save(mongoPendingRecordsPatched, "ui_pending_records");
//            return;
//        }
//        else{
//            if(mongoPendingRecords.get(0).getProcessStatus() == PendingRecordStatusEnum.HANDLED.getStatus()){
//                logger.error("The pendingRecords with user_prod_id:{} and order_id:{} and "
//                        + "fund_code:{} already in handled status", trdPayFlow.getUserProdId(),
//                    trdPayFlow.getOutsideOrderno(), trdPayFlow.getFundCode());
//            }else{
//                mongoPendingRecords.get(0).setTradeStatus(trdPayFlow.getTrdStatus());
//                mongoPendingRecords.get(0).setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
//                mongoTemplate.save(mongoPendingRecords.get(0), "ui_pending_records");
//            }
//        }
//    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS, durable = "false"),
        exchange = @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"), key = RabbitMQConstants.OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS)
    )
    public void receivePreConfirmInfo(TrdPayFlow trdPayFlow) throws Exception {
        //如果是货币基金，把消息结构里面的apply_date和applydate_unitvalue的值保存到pendingRedords里面
        logger.info("received message in :{}", RabbitMQConstants
            .OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS);
        long navadj = -1L;
        if(StringUtils.isEmpty(trdPayFlow.getTrdApplyDate())||trdPayFlow.getTrdApplyDate().equals
            ("-1")){
            logger.error("received trdPayFlow without  trdApplyDate:{}", trdPayFlow.getTrdApplyDate());
        }else if(MonetaryFundEnum.containsCode(trdPayFlow.getFundCode())&& trdPayFlow.
            getApplydateUnitvalue() <= -1L) {
            logger.error("the trdPayFlow.getApplydateUnitvalue is invalid");
            navadj = getMoneyCodeNavAdjByDate(trdPayFlow.getFundCode(), trdPayFlow
                .getTrdApplyDate());
            if (navadj < 0) {
                logger.error("please check funds database why the fundCode:{} have no "
                    + "data in date:{}", trdPayFlow.getFundCode(), trdPayFlow.getTrdApplyDate());
            }
        }else{
            navadj = trdPayFlow.getApplydateUnitvalue();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("user_prod_id").is(trdPayFlow.getUserProdId()).and
            ("fund_code").is(trdPayFlow.getFundCode()).and("outside_order_id").is
            (trdPayFlow.getOutsideOrderno()));
        List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
        if(CollectionUtils.isEmpty(mongoPendingRecords)){
            logger.error("There is no pendingRecords with user_prod_id:{} and order_id:{} and "
                    + "fund_code:{}", trdPayFlow.getUserProdId(), trdPayFlow.getOutsideOrderno(),
                trdPayFlow.getFundCode() );
            MongoPendingRecords mongoPendingRecordsPatch = getPatchMongoPendingRecord(trdPayFlow);
            if(mongoPendingRecordsPatch == null){
                logger.error("Failed to generate patch mongoPendingRecoresPatch");
                return;
            }
            mongoPendingRecordsPatch.setApplyDateNavadj(navadj);
            mongoTemplate.save(mongoPendingRecordsPatch,"ui_pending_records");
            if(mongoPendingRecordsPatch == null){
                logger.error("failed to make mongoPendingRecordsPatch");
            }

        }
        else{
            if(mongoPendingRecords.get(0).getProcessStatus() == PendingRecordStatusEnum.HANDLED.getStatus()){
                logger.error("The pendingRecords with user_prod_id:{} and order_id:{} and "
                        + "fund_code:{} already in handled status", trdPayFlow.getUserProdId(),
                    trdPayFlow.getOutsideOrderno(), trdPayFlow.getFundCode());
                return;
            }else{
                mongoPendingRecords.get(0).setApplyDateNavadj(navadj);
                if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.BUY.getOperation()){
                    mongoPendingRecords.get(0).setTradeTargetShare(trdPayFlow.getTradeTargetShare());
                }else if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
                    mongoPendingRecords.get(0).setTradeTargetSum(trdPayFlow.getTradeTargetSum());
                }
                mongoPendingRecords.get(0).setTradeStatus( trdPayFlow.getTrdStatus() );
                mongoTemplate.save(mongoPendingRecords.get(0), "ui_pending_records");
            }
        }
    }




    private Long getMoneyCodeNavAdjByDate(String fundCode, String applyDate){
        if(!applyDate.contains("-")){
            StringBuilder sb = new StringBuilder();
            sb.append(applyDate.substring(0,4)).append("-").append(applyDate.substring(4,6)).append
                ("-").append(applyDate.substring(6,8));
            applyDate = sb.toString();
        }
        if(StringUtils.isEmpty(applyDate) || applyDate.equals("-1")){
            logger.error("cannot make navadj without applyDate:{}", applyDate);
            return -1L;
        }
        if(MonetaryFundEnum.containsCode(fundCode)){
            String beginDate = TradeUtil.getDayBefore(applyDate, 1);
            List<String> codes = new ArrayList<>();
            codes.add(fundCode);
            List<DCDailyFunds> dcDailyFunds = dataCollectionService.getFundDataOfDay(codes,
                beginDate, applyDate);
            if(!CollectionUtils.isEmpty(dcDailyFunds)){
                Double navadj = dcDailyFunds.get(0).getNavadj();
                return TradeUtil.getLongNumWithMul1000000(navadj);
            }
        }
        return -1L;
    }


    private MongoPendingRecords getPathMongoPendingRecord(TrdOrderDetail trdOrderDetail){
        logger.info("make patch PendingRecords info into database");
        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        mongoPendingRecordsPatch.setUserProdId(trdOrderDetail.getUserProdId());
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsPatch.setTradeStatus(trdOrderDetail.getOrderDetailStatus());
        mongoPendingRecordsPatch.setTradeType(trdOrderDetail.getTradeType());
        mongoPendingRecordsPatch.setOutsideOrderId(trdOrderDetail.getOrderId()+trdOrderDetail.getId());
        mongoPendingRecordsPatch.setOrderId(trdOrderDetail.getOrderId());
        mongoPendingRecordsPatch.setTradeTargetShare(trdOrderDetail.getFundNum());
        mongoPendingRecordsPatch.setTradeTargetSum(trdOrderDetail.getFundSum());
        mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());

        mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
        return mongoPendingRecordsPatch;
    }
    private MongoPendingRecords getPathMongoPendingRecord(ProdDtlSellDTO prodDtlSellDTO, String
        orderId) {
        logger.info("make patch PendingRecords info into database");
        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        mongoPendingRecordsPatch.setUserProdId(prodDtlSellDTO.getUserProdId());
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsPatch.setTradeStatus(TrdOrderStatusEnum.WAITSELL.getStatus());
        mongoPendingRecordsPatch.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        mongoPendingRecordsPatch.setOutsideOrderId(orderId+prodDtlSellDTO.getId());
        mongoPendingRecordsPatch.setOrderId(orderId);
        mongoPendingRecordsPatch.setTradeTargetShare(new Long(prodDtlSellDTO.getFundQuantity()));
        mongoPendingRecordsPatch.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO.getTargetSellAmount
            ()));
        mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());

        mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
        return mongoPendingRecordsPatch;
    }



    private MongoPendingRecords getPatchMongoPendingRecord(TrdPayFlow trdPayFlow){

        Query query = new Query();
        query.addCriteria(Criteria.where("user_prod_id").is(trdPayFlow.getUserProdId()).and
            ("fund_code").is(trdPayFlow.getFundCode()).and("outside_order_id").is(null).and
            ("process_status").is(PendingRecordStatusEnum.NOTHANDLED.getStatus()).and
            ("trade_type").is(trdPayFlow.getTrdType()));
        List<MongoPendingRecords> unhandledRecords =  mongoTemplate.find(query, MongoPendingRecords
                .class, "ui_pending_records");
        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        if(!CollectionUtils.isEmpty(unhandledRecords)){
            logger.error("because there is a unhandled record, we need use this record to handle "
                + "records for this trade");
            mongoPendingRecordsPatch = unhandledRecords.get(0);
        }
        //if there is no pendingRecord for this order, we need to generate to make sure this
        // task can be done
        //ToDo: grpc trdOrder to get the orderDetail info to fill the MongoPendingRecords
        List<com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail> trdOrderDetails = null;
        String orderId = null;
        try {
            orderId = TradeUtil.getOrderIdByOutsideOrderNo(trdPayFlow.getOutsideOrderno(), trdPayFlow
                .getOrderDetailId());
            trdOrderDetails = orderRpcService
                .getOrderDetailByGenOrderIdAndFundCode(orderId, trdPayFlow.getFundCode());
        } catch (Exception e) {
            logger.error("failed to get corresponding orderDetail info for this trdPayFlow "
                + "with fundCode:{} orderId:{}", trdPayFlow.getFundCode(), orderId, e);

        }
        if(CollectionUtils.isEmpty(trdOrderDetails)){
            logger.error("Failed to find corresponding orderDetail info with user_prod_id:{} and order_id:{} and "
                    + "fund_code:{}", trdPayFlow.getUserProdId(), trdPayFlow.getOutsideOrderno(),
                trdPayFlow.getFundCode());
            return null;
        }else{
            logger.info("make patch PendingRecords info into database");

            mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
            mongoPendingRecordsPatch.setTradeStatus(trdPayFlow.getTrdStatus());
            mongoPendingRecordsPatch.setTradeType(trdPayFlow.getTrdType());
            mongoPendingRecordsPatch.setOutsideOrderId(trdPayFlow.getOutsideOrderno());
            mongoPendingRecordsPatch.setOrderId(orderId);
            mongoPendingRecordsPatch.setUserProdId(trdPayFlow.getUserProdId());
            mongoPendingRecordsPatch.setTradeTargetShare(trdPayFlow.getTradeTargetShare());
            mongoPendingRecordsPatch.setTradeTargetSum(trdPayFlow.getTradeTargetSum());
            mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());
            mongoPendingRecordsPatch.setApplyDateStr(trdPayFlow.getTrdApplyDate());
            if(trdPayFlow.getApplydateUnitvalue() == -1L){
                logger.error("the trdPayFlow.getApplydateUnitvalue is invalid");
                long navadj =  getMoneyCodeNavAdjByDate(trdPayFlow.getFundCode(), trdPayFlow
                    .getTrdApplyDate());
                if(navadj < 0){
                    logger.error("please check funds database why the fundCode:{} have no "
                        + "data in date:{}", trdPayFlow.getFundCode(), trdPayFlow.getTrdApplyDate());
                }
                mongoPendingRecordsPatch.setApplyDateNavadj(navadj);
            }else{
                mongoPendingRecordsPatch.setApplyDateNavadj(trdPayFlow.getApplydateUnitvalue());
            }
            mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
            return mongoPendingRecordsPatch;
        }
    }

    private MongoPendingRecords getPatchMongoPendingRecordFromZZInfo(
        MongoUiTrdZZInfo mongoUiTrdZZInfo){
        logger.info("make patch PendingRecords info into database");
        List<com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail> trdOrderDetails = null;
        String orderId = null;
        try {
            orderId = TradeUtil.getOrderIdByOutsideOrderNo(mongoUiTrdZZInfo.getOutSideOrderNo(), mongoUiTrdZZInfo
                .getOrderDetailId());
            trdOrderDetails = orderRpcService
                .getOrderDetailByGenOrderIdAndFundCode(orderId, mongoUiTrdZZInfo.getFundCode());
        } catch (Exception e) {
            logger.error("failed to get corresponding orderDetail info for this trdPayFlow "
                + "with fundCode:{} orderId:{}", mongoUiTrdZZInfo.getFundCode(), orderId, e);

        }
        if(CollectionUtils.isEmpty(trdOrderDetails)){
            logger.error("Failed to find corresponding orderDetail info with user_prod_id:{} and order_id:{} and "
                    + "fund_code:{}", mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getOutSideOrderNo(),
                mongoUiTrdZZInfo.getFundCode());
            trdOrderDetails = orderRpcService.getOrderDetailByApplySerial(mongoUiTrdZZInfo
                .getApplySerial());
            if(CollectionUtils.isEmpty(trdOrderDetails)) {
                logger.error("Failed to find corresponding orderDetail info with applySerial:{}",
                    mongoUiTrdZZInfo.getApplySerial());
                return null;
            }
        }
        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        mongoPendingRecordsPatch.setUserProdId(mongoUiTrdZZInfo.getUserProdId());
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsPatch.setTradeStatus(mongoUiTrdZZInfo.getTradeStatus());
        mongoPendingRecordsPatch.setTradeType(mongoUiTrdZZInfo.getTradeType());
        mongoPendingRecordsPatch.setOutsideOrderId(mongoUiTrdZZInfo.getOutSideOrderNo());
        mongoPendingRecordsPatch.setApplyDateStr(mongoUiTrdZZInfo.getApplyDate());
        mongoPendingRecordsPatch.setApplySerial(mongoUiTrdZZInfo.getApplySerial());
        mongoPendingRecordsPatch.setTradeTargetShare(mongoUiTrdZZInfo.getTradeTargetShare());
        mongoPendingRecordsPatch.setTradeTargetSum(mongoUiTrdZZInfo.getTradeTargetSum());
        mongoPendingRecordsPatch.setTradeConfirmSum(mongoUiTrdZZInfo.getTradeConfirmSum());
        mongoPendingRecordsPatch.setTradeConfirmShare(mongoUiTrdZZInfo.getTradeConfirmShare());
        mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());

        mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
        return mongoPendingRecordsPatch;
    }
}
