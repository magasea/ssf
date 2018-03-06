package com.shellshellfish.aaas.userinfo.message;


import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.message.order.OrderStatusChangeDTO;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import com.shellshellfish.aaas.userinfo.service.PayGrpcService;
import com.shellshellfish.aaas.userinfo.service.impl.CalculateConfirmedAsset;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

                uiProductDetailRepo.save(uiProductDetail);
            }catch (Exception ex){
                logger.error("exception:",ex);

            }
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:",e);
        }

        
    }


    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_UITRDLOG, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_TRDLOG)
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
            if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.WAITPAY.getStatus() ||
                trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()){
                //等待支付金额就是下单请求时候的金额
                mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow.getTradeTargetSum()));
            }else if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus() ||
                trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus()){
                //等待赎回份额就是下单请求时候的份额
                mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow.getTradeTargetShare()));
                mongoUiTrdLog.setApplySerial(trdPayFlow.getApplySerial());
            }else if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()){
                if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.BUY.getOperation()){
                    mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow
                        .getTradeConfirmShare()));
                }else if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
                    mongoUiTrdLog.setAmount(TradeUtil.getBigDecimalNumWithDiv100(trdPayFlow
                        .getTradeConfirmSum()));
                }
                mongoUiTrdLog.setApplySerial(trdPayFlow.getApplySerial());
            }
            mongoUiTrdLog.setLastModifiedDate(TradeUtil.getUTCTime());
            mongoUiTrdLog.setFundCode(trdPayFlow.getFundCode());
            mongoUiTrdLog.setTradeDate(trdPayFlow.getUpdateDate());
            mongoUserTrdLogMsgRepo.save(mongoUiTrdLog);
        }catch (Exception ex){
            logger.error("exception:",ex);

        }
        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:",e);
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
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_REDEEM)
    )
    public void receiveAndCheckSell(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
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
        }catch (Exception ex){
            logger.error("Exception:", ex);
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:",e);
        }


    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_UIPRODQUANTITY, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_CFMLOG)
    )
    public void receiveConfirmInfo(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel
        channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo mongoUiTrdZZInfoInDb = mongoUiTrdZZInfoRepo
        .findByUserProdIdAndUserIdAndApplySerial(mongoUiTrdZZInfo
            .getUserProdId(), mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getApplySerial());
        try{
            if(mongoUiTrdZZInfoInDb == null){
                mongoUiTrdZZInfoInDb = new com.shellshellfish.aaas.userinfo.model.dao
                    .MongoUiTrdZZInfo();
                MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo, mongoUiTrdZZInfoInDb);
                mongoUiTrdZZInfoRepo.save(mongoUiTrdZZInfoInDb);
            }else{
                String idOrig = mongoUiTrdZZInfoInDb.getId();
                MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo, mongoUiTrdZZInfoInDb);
                mongoUiTrdZZInfoInDb.setTradeType(mongoUiTrdZZInfo.getTradeType());
                mongoUiTrdZZInfoInDb.setId(idOrig);
                mongoUiTrdZZInfoRepo.save(mongoUiTrdZZInfoInDb);
            }
        }catch (Exception ex){
            logger.error("exception:",ex);

        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:", e);
            logger.error("exception:",e);
        }


    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_UPDATE_UITRDCONFIRMINFO, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_UPDATEPROD)
    )
    public void receiveConfirmInfoUpdateProdQty(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel channel, @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            logger.info("received mongoUiTrdZZInfo to update assects with userProdId:{} "
                    + "userId:{} fundCode:{} ", mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
            if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation()) {
                updateBuyProductQty(mongoUiTrdZZInfo);
            } else if (mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.REDEEM
                .getOperation()) {
                updateRedeemProductQty(mongoUiTrdZZInfo);
            } else {
                logger.error("cannot handle this mongoUiTrdZZInfo with trdType:{}", mongoUiTrdZZInfo
                    .getTradeType());
            }
        }catch(Exception ex){
            logger.error("Exception:", ex);
        }

        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            logger.error("exception:", e);
        }
    }

    private boolean updateBuyProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo){
        UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
            (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
        String cardNumber = orderRpcService.getBankCardNumberByUserProdId(mongoUiTrdZZInfo
            .getUserProdId());
        List<UiBankcard> uiBankcards =  userInfoBankCardsRepository.findAllByUserIdAndCardNumber
            (mongoUiTrdZZInfo.getUserId(), cardNumber);
        String userPid = uiBankcards.get(0).getUserPid();


        if(productDetail.getFundQuantityTrade() != null && productDetail
            .getFundQuantityTrade() > 0){
            logger.error("abnormal situation appeared the userProdId:" + mongoUiTrdZZInfo
                .getUserProdId() + " initial quantity should be 0 or null but already "
                + "have number , need check why, but curent we reset the quantity "
                + "according to the confirm message ");
        }
        if(MonetaryFundEnum.containsCode(productDetail.getFundCode())){
            //monetary fund should caculate quantity by NetValue
            Long trdCfmSum = mongoUiTrdZZInfo.getTradeConfirmSum();
            List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfosFromZZ(userPid,
                mongoUiTrdZZInfo.getFundCode(), 10);
            Long fundUnitNet= getFundUnitNet(productDetail.getFundCode(), fundNetInfos,
                mongoUiTrdZZInfo.getApplyDate());
            Long caculatedFundQty = TradeUtil.getBigDecimalNumWithDivOfTwoLong(trdCfmSum,
                fundUnitNet).longValueExact();
            productDetail.setFundQuantity(caculatedFundQty.intValue());
            productDetail.setFundQuantityTrade(caculatedFundQty.intValue());
        }else{
            productDetail.setFundQuantityTrade(mongoUiTrdZZInfo.getTradeConfirmShare().intValue());
            productDetail.setFundQuantity(mongoUiTrdZZInfo.getTradeConfirmShare().intValue());
        }
        uiProductDetailRepo.save(productDetail);
        return true;
    }

    private boolean updateRedeemProductQty(MongoUiTrdZZInfo mongoUiTrdZZInfo){

        UiProductDetail productDetail = uiProductDetailRepo.findByUserProdIdAndFundCode
            (mongoUiTrdZZInfo.getUserProdId(), mongoUiTrdZZInfo.getFundCode());
        String cardNumber = orderRpcService.getBankCardNumberByUserProdId(mongoUiTrdZZInfo
            .getUserProdId());
        List<UiBankcard> uiBankcards =  userInfoBankCardsRepository.findAllByUserIdAndCardNumber
            (mongoUiTrdZZInfo.getUserId(), cardNumber);
        String userPid = uiBankcards.get(0).getUserPid();

        productDetail.setStatus(TrdOrderStatusEnum.SELLCONFIRMED.getStatus());
        if(productDetail.getFundQuantityTrade() != null && productDetail
            .getFundQuantityTrade() < 0){
            logger.error("abnormal situation appeared the userProdId:" + mongoUiTrdZZInfo
                .getUserProdId() + " initial quantity should be 0 or null but already "
                + "have number , need check why, but curent we reset the quantity "
                + "according to the confirm message ");
        }
        if(MonetaryFundEnum.containsCode(productDetail.getFundCode())){
            //monetary fund should caculate quantity by NetValue
            Long trdCfmSum = mongoUiTrdZZInfo.getTradeConfirmSum();
            List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfosFromZZ(userPid,
                mongoUiTrdZZInfo.getFundCode(), 10);
            Long fundUnitNet= getFundUnitNet(productDetail.getFundCode(), fundNetInfos,
                mongoUiTrdZZInfo.getApplyDate());
            Long caculatedFundQty = TradeUtil.getBigDecimalNumWithDivOfTwoLong(trdCfmSum,
                fundUnitNet).longValueExact();
            Long remainQty = productDetail.getFundQuantity() - caculatedFundQty;
            if(remainQty < 0 ){
                logger.error("abnormal situation appeared for the userProdId:{} current "
                    + "quantity:{} the redeem confirm quantity:{}", mongoUiTrdZZInfo
                    .getUserProdId(), productDetail.getFundQuantity(), mongoUiTrdZZInfo
                    .getTradeConfirmShare());
            }
            productDetail.setFundQuantity(remainQty.intValue());
            productDetail.setFundQuantityTrade(remainQty.intValue());

        }else{
            Long remainQty = productDetail.getFundQuantity() - mongoUiTrdZZInfo
                .getTradeConfirmShare();
            if(remainQty <= 0 ){
                logger.error("abnormal situation appeared for the userProdId:{} current "
                    + "quantity:{} the redeem confirm quantity:{}", mongoUiTrdZZInfo
                    .getUserProdId(), productDetail.getFundQuantity(), mongoUiTrdZZInfo
                    .getTradeConfirmShare());
                return false;
            }
        }
        if(StringUtils.isEmpty(mongoUiTrdZZInfo.getApplySerial())){
            logger.error("abnormal message of mongoUiTrdZZInfo, there is no applySerial in "
                + "mongoUiTrdZZInfo:" + mongoUiTrdZZInfo.getApplySerial());
            return false;
        }
        if( !StringUtils.isEmpty(productDetail.getLastestSerial()) && !productDetail
            .getLastestSerial().contains(mongoUiTrdZZInfo.getApplySerial())){
            logger.error("received repeated confirm message :" + mongoUiTrdZZInfo.getApplySerial());
            return false;
        }else if(StringUtils.isEmpty(productDetail.getLastestSerial())){
            logger.info("it is initial, let's handle this message ");
            uiProductDetailRepo.save(productDetail);
        }else if(!StringUtils.isEmpty(productDetail.getLastestSerial()) && productDetail
            .getLastestSerial().contains(mongoUiTrdZZInfo.getApplySerial())){
            String[] serials = productDetail.getLastestSerial().split("\\|");
            StringBuilder sb = new StringBuilder();
            for(String serial: serials){
                if(serial.equals(mongoUiTrdZZInfo.getApplySerial())){
                    logger.info("got the history stored serial:"+mongoUiTrdZZInfo.getApplySerial());
                    continue;
                }else{
                    sb.append(serial).append("|");
                }
            }
            productDetail.setLastestSerial(sb.toString());
        }
        uiProductDetailRepo.save(productDetail);
        return true;
    }

    private Long getFundUnitNet(String fundCode, List<FundNetInfo> fundNetInfos, String tradeDate){
        for(FundNetInfo fundNetInfo: fundNetInfos){
            if(fundNetInfo.getFundCode().equals(fundCode) && fundNetInfo.getTradedate().equals(tradeDate)){
                return TradeUtil.getLongNumWithMul100(fundNetInfo.getUnitNet());
            }
        }
        logger.error("Failed to find unit net for :{} and tradeDate:{}",fundCode, tradeDate);
        return -1L;
    }

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_USERINFO_BASE + RabbitMQConstants
            .OPERATION_TYPE_CACULATE_UIACCECTS, durable = "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_USERINFO_UPDATEPROD)
    )
    public void receiveConfirmInfoUpdateAssects(MongoUiTrdZZInfo mongoUiTrdZZInfo, Channel channel,
        @Header
        (AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            // now update correspond product_detail for quantity of fund
            logger.info("received mongoUiTrdZZInfo to update assects with userProdId:{} "
                    + "userId:{} fundCode:{} ", mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
            calculateConfirmedAsset.calculateConfirmedAsset(mongoUiTrdZZInfo.getUserProdId(),
                mongoUiTrdZZInfo.getUserId(), mongoUiTrdZZInfo.getFundCode());
        }catch(Exception ex){
            logger.error("Exception:", ex);
        }

    }
}
