package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.*;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.ZZRiskToSSFRiskUtils;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdPreOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdPreOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import com.shellshellfish.aaas.trade.finance.prod.FinanceMoneyFundInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.FinanceProdInfosQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserIdQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class TradeOpServiceImpl implements TradeOpService {

    Logger logger = LoggerFactory.getLogger(TradeOpServiceImpl.class);

    @Autowired
    FinanceProdInfoService financeProdInfoService;

    @Autowired
    TrdOrderRepository trdOrderRepository;

    @Autowired
    TrdBrokderRepository trdBrokderRepository;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;

    @Autowired
    TrdTradeBankDicRepository trdTradeBankDicRepository;

    @Autowired
    BroadcastMessageProducer broadcastMessageProducer;


    UserInfoServiceFutureStub userInfoServiceFutureStub;

    PayRpcServiceFutureStub payRpcServiceFutureStub;

    @Autowired
    TrdBrokerUserRepository trdBrokerUserRepository;

    @Autowired
    TrdPreOrderRepository trdPreOrderRepository;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    PayService payService;

    @Autowired
    ManagedChannel managedUIChannel;

    @Autowired
    TradeOpService tradeOpService;

    @Autowired
    OrderService orderService;

    boolean useMsgToBuy = true;


    @PostConstruct
    public void init() {
        userInfoServiceFutureStub = UserInfoServiceGrpc.newFutureStub(managedUIChannel);

    }

    public void shutdown() throws InterruptedException {
        managedUIChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdBuyInfo)
            throws Exception {
        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        BeanUtils.copyProperties(financeProdBuyInfo, productBaseInfo);
        logger.info("oemId:{}", productBaseInfo.getOemId());
        List<ProductMakeUpInfo> productMakeUpInfos = financeProdInfoService.getFinanceProdMakeUpInfo
                (productBaseInfo);
        if (CollectionUtils.isEmpty(productMakeUpInfos)) {
            logger.info("productMakeUpInfos:{}", productMakeUpInfos.get(0).getProdName());
        }
        if (productMakeUpInfos.size() <= 0) {
            logger.info("没有发现产品组成信息 prodId:{} groupId:{}", productBaseInfo.getProdId(), productBaseInfo.getGroupId());
            throw new Exception("没有发现产品组成信息 prodId:" + productBaseInfo.getProdId() + " groupId:" +
                    productBaseInfo.getGroupId());
        }
        //在用户理财产品系统里面生成用户的理财产品, 这是日后《我的理财产品》模块的依据
        Long userProdId = genUserProduct(financeProdBuyInfo, productMakeUpInfos);
        financeProdBuyInfo.setUserProdId(userProdId);
        return genOrderFromBuyInfoAndProdMakeUpInfo(financeProdBuyInfo, productMakeUpInfos);

    }

    private Long genUserProduct(FinanceProdBuyInfo financeProdBuyInfo,
                                List<ProductMakeUpInfo> productMakeUpInfos) throws Exception {
        FinanceProdInfosQuery.Builder requestBuilder = FinanceProdInfosQuery.newBuilder();
        requestBuilder.setUserId(financeProdBuyInfo.getUserId());
        requestBuilder.setBankCardNum(financeProdBuyInfo.getBankAcc());
        FinanceProdInfoCollection.Builder subReqBuilder = FinanceProdInfoCollection.newBuilder();
        FinanceProdInfo.Builder finProdInfoBuilder = FinanceProdInfo.newBuilder();
        for (ProductMakeUpInfo productMakeUpInfo : productMakeUpInfos) {
            BeanUtils.copyProperties(productMakeUpInfo, finProdInfoBuilder);
            requestBuilder.addProdList(finProdInfoBuilder);
            finProdInfoBuilder.clear();
        }
        Long userProdId = userInfoServiceFutureStub.genUserProdsFromOrder(requestBuilder
                .build()).get().getUserProdId();
        if (userProdId == -1L) {
            logger.error("userProdId is not greater than 0, means some error happened,userPord:{} ", userProdId);
            throw new Exception("Failed to create userProd and userProdDetail");
        }
        return userProdId;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
    public TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdBuyInfo,
                                                         List<ProductMakeUpInfo> productMakeUpInfos) throws Exception {
        //generate order
//    TrdTradeBroker trdTradeBroker = trdBrokderRepository.findOne(1L);
        PayOrderDto payOrderDto = new PayOrderDto();
        Map brokerWithTradeAcco = getOrMakeTradeAcco(financeProdBuyInfo);
        if (CollectionUtils.isEmpty(brokerWithTradeAcco)) {
            logger.error("Failed to make trade account for user:{} ", financeProdBuyInfo.getUserId());
            throw new Exception("Failed to make trade account for user:" + financeProdBuyInfo.getUserId());
        }
        //默认用第一组数据， 因为现在只有一个交易平台
        int trdBrokerId = -1;
        String trdAcco = null;
        trdBrokerId = (int) brokerWithTradeAcco.keySet().toArray()[0];
        String trdAccoOrig = (String) brokerWithTradeAcco.get(Integer.valueOf(trdBrokerId));
        String items[] = trdAccoOrig.split("\\|");
        trdAcco = items[0];
        payOrderDto.setUserPid(items[1]);
        payOrderDto.setRiskLevel(Integer.parseInt(items[2]));
        String orderId = TradeUtil.generateOrderIdByBankCardNum(financeProdBuyInfo.getBankAcc(), trdBrokerId);
        payOrderDto.setTrdAccount(trdAcco);
        payOrderDto.setUserUuid(financeProdBuyInfo.getUuid());
        payOrderDto.setUserProdId(financeProdBuyInfo.getUserProdId());
        List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails = new
                ArrayList<com.shellshellfish.aaas.common.message.order.TrdOrderDetail>();

        TrdOrder trdOrder = new TrdOrder();
        MyBeanUtils.mapEntityIntoDTO(financeProdBuyInfo, trdOrder);
        trdOrder.setBankCardNum(financeProdBuyInfo.getBankAcc());
        trdOrder.setOrderDate(TradeUtil.getUTCTime());
        trdOrder.setCreateDate(TradeUtil.getUTCTime());
        trdOrder.setOrderType(TrdOrderOpTypeEnum.BUY.getOperation());
        trdOrder.setProdId(financeProdBuyInfo.getProdId());
        trdOrder.setGroupId(financeProdBuyInfo.getGroupId());
        trdOrder.setUserProdId(financeProdBuyInfo.getUserProdId());
        trdOrder.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.ordinal());
        trdOrder.setOrderId(orderId);
        trdOrder.setUserId(financeProdBuyInfo.getUserId());
        trdOrder.setCreateBy(financeProdBuyInfo.getUserId());
        trdOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdBuyInfo.getMoney()));
        trdOrderRepository.save(trdOrder);
        //generate sub order for each funds
        for (ProductMakeUpInfo productMakeUpInfo : productMakeUpInfos) {
            TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
            trdOrderDetail.setOrderId(trdOrder.getOrderId());
            trdOrderDetail.setUserId(trdOrder.getUserId());
            //规定基金占比用百分比并且精确万分之一
            BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
                    (BigDecimal.valueOf(10000));
            trdOrderDetail.setFundSum(fundRatio.multiply(financeProdBuyInfo.getMoney())
                    .multiply(BigDecimal.valueOf(100)).toBigInteger().longValue());
            trdOrderDetail.setFundMoneyQuantity(fundRatio.multiply(financeProdBuyInfo.getMoney())
                    .multiply(BigDecimal.valueOf(100)).toBigInteger().longValue());
            trdOrderDetail.setBuysellDate(TradeUtil.getUTCTime());
            trdOrderDetail.setCreateBy(financeProdBuyInfo.getUserId());
            trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setUpdateBy(financeProdBuyInfo.getUserId());
            trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setUserId(financeProdBuyInfo.getUserId());
            trdOrderDetail.setFundCode(productMakeUpInfo.getFundCode());
            trdOrderDetail.setUserProdId(trdOrder.getUserProdId());
            trdOrderDetail.setTradeType(trdOrder.getOrderType());
            trdOrderDetail.setFundShare(productMakeUpInfo.getFundShare());
            trdOrderDetail = trdOrderDetailRepository.save(trdOrderDetail);
            com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderPay = new com
                    .shellshellfish.aaas.common.message.order.TrdOrderDetail();
            BeanUtils.copyProperties(trdOrderDetail, trdOrderPay);
            trdOrderPay.setOrderId(trdOrderDetail.getOrderId());
            trdOrderPay.setUserId(financeProdBuyInfo.getUserId());
//      GenericMessage<TrdOrderDetail> genericMessage = new GenericMessage<TrdOrderDetail>();
            trdOrderDetails.add(trdOrderPay);

        }
        payOrderDto.setOrderDetailList(trdOrderDetails);
        payOrderDto.setTrdBrokerId(trdBrokerId);

        sendOutOrder(payOrderDto);
        return trdOrder;
    }

    private Map<Integer, String> getOrMakeTradeAcco(FinanceProdBuyInfo financeProdBuyInfo)
            throws Exception {
        Map<Integer, String> result = new HashMap<>();
        List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository.findByUserId(financeProdBuyInfo.getUserId());

        int trdBrokerId = -1;
        String bankCardNum = null;
        String trdAcco = null;
        String userPid = null;
        int riskLevel;
        UserBankInfo userBankInfo = userInfoService.getUserBankInfo(financeProdBuyInfo.getUuid());
        if (CollectionUtils.isEmpty(userBankInfo.getCardNumbersList())) {
            logger.error("failed to find user:{} have binded cards", financeProdBuyInfo.getUserId());
            throw new Exception("用户未绑卡，请绑卡后再购买理财产品");
        }
        for (CardInfo cardInfo : userBankInfo.getCardNumbersList()) {
            if (cardInfo.getCardNumbers().equals(financeProdBuyInfo.getBankAcc())) {
                userPid = cardInfo.getUserPid();
                break;
            }
        }
        riskLevel = ZZRiskToSSFRiskUtils.getZZRiskAbilityFromSSFRisk(UserRiskLevelEnum.get(userBankInfo
                .getRiskLevel())).getRiskLevel();

        if (StringUtils.isEmpty(userPid)) {
            logger.error("this user:{} personal id is not in ui_bankcard", financeProdBuyInfo.getUserId());
            throw new Exception("用户: " + financeProdBuyInfo.getUserId() + " 的绑卡信息有误，没有身份证号");
        }
        if (!CollectionUtils.isEmpty(trdBrokerUsers)) {

            trdBrokerId = trdBrokerUsers.get(0).getTradeBrokerId().intValue();
            bankCardNum = financeProdBuyInfo.getBankAcc();
            for (TrdBrokerUser trdBrokerUser : trdBrokerUsers) {
                if (bankCardNum.equals(trdBrokerUser.getBankCardNum())) {
                    trdBrokerId = trdBrokerUser.getTradeBrokerId();
                    trdAcco = trdBrokerUser.getTradeAcco();
                }
            }
        } else if (CollectionUtils.isEmpty(trdBrokerUsers) || StringUtils.isEmpty(trdAcco)) {

            logger.info("trdBrokerUsers.get(0).getTradeAcco() is empty, 坑货出现，需要生成交易账号再交易");


            BindBankCard bindBankCard = new BindBankCard();
            TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndBankCardNum
                    (userBankInfo.getUserId(), financeProdBuyInfo.getBankAcc());
            bindBankCard.setBankCardNum(financeProdBuyInfo.getBankAcc());
//      String bankShortName = BankUtil.getCodeOfBank(financeProdBuyInfo.getBankAcc());
//      String bankName = BankUtil.getNameOfBank(financeProdBuyInfo.getBankAcc());
            String bankName = BankUtil.getZZBankNameFromOriginBankName(BankUtil.getNameOfBank
                    (financeProdBuyInfo.getBankAcc()));
            TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankNameAndTraderBrokerId
                    (bankName, TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());


            if (null == trdTradeBankDic) {
                logger.error("this bank name:{} with brokerId:{} is not in table:", bankName,
                        TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
                throw new Exception("this bank name:" + bankName
                        + " with brokerId" + TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId() + " is not in table:");
            }
            UserInfo userInfo = userInfoService.getUserInfoByUserId(financeProdBuyInfo.getUserId());
            bindBankCard.setBankCode(trdTradeBankDic.getBankCode().trim());
            bindBankCard.setCellphone(userBankInfo.getCellphone());
            bindBankCard.setBankCardNum(financeProdBuyInfo.getBankAcc());
            bindBankCard.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
            bindBankCard.setUserId(financeProdBuyInfo.getUserId());
            bindBankCard.setUserName(userBankInfo.getUserName());
            bindBankCard.setUserPid(userPid);
            bindBankCard.setRiskLevel(userInfo.getRiskLevel());
            trdAcco = payService.bindCard(bindBankCard);
        }
        //因为要提取用户身份证号码，所以这里拼接回去，要优化吗？
        result.put(trdBrokerId, trdAcco + "|" + userPid + "|" + riskLevel);
        return result;
    }


    private void sendOutOrder(PayOrderDto payOrderDto) {
        if (useMsgToBuy) {
            logger.info("use message queue to send payOrderDto");
            broadcastMessageProducer.sendPayMessages(payOrderDto);
        } else {
            logger.info("use grpc to send payOrderDto");
            payService.order2Pay(payOrderDto);
        }
    }


    @Override
    public Long getUserId(String userUuid) throws ExecutionException, InterruptedException {
        UserIdQuery.Builder builder = UserIdQuery.newBuilder();
        builder.setUuid(userUuid);
        com.shellshellfish.aaas.userinfo.grpc.UserId userId = userInfoServiceFutureStub.getUserId
                (builder.build()).get();
        return userId.getUserId();
    }

    @Override
    public UserInfo getUserInfoByUserUUID(String uuid) throws ExecutionException,
            InterruptedException {
        UserIdQuery.Builder builder = UserIdQuery.newBuilder();
        builder.setUuid(uuid);
        UserInfo userInfo = userInfoServiceFutureStub.getUserInfoByUserUUID(builder.build()).get();
        return userInfo;
    }

    @Override
    @Transactional
    public void updateByParam(String tradeApplySerial, Long fundSum, Long fundSumConfirmed, Long
            fundNum, Long fundNumConfirmed, Long updateDate, Long updateBy, Long id, int
                                      orderDetailStatus, Long buyFee)
            throws Exception {
        TrdOrderDetail trdOrderDetail;
        if (id > 0) {
            trdOrderDetail = trdOrderDetailRepository.findById(id).get();
        } else {
            trdOrderDetail = trdOrderDetailRepository.findByTradeApplySerial(tradeApplySerial);
        }
        if (trdOrderDetail == null) {
            logger.error("failed to find orderDetail by id:{} tradeApplySerial:{} ", id, tradeApplySerial);
            throw new Exception("failed to find orderDetail by id:" + id + " tradeApplySerial:" + tradeApplySerial);
        }
        trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setUpdateBy(updateBy);
        if (fundNum != null && fundNum > 0) {
            trdOrderDetail.setFundNum(fundNum);
        }
        if (fundSumConfirmed != null && fundSumConfirmed > 0) {
            trdOrderDetail.setFundSumConfirmed(fundSumConfirmed);
        }
        if (fundSum != null && fundSum > 0) {
            trdOrderDetail.setFundSum(fundSum);
        }
        if (fundNumConfirmed != null && fundNumConfirmed > 0) {
            trdOrderDetail.setFundNumConfirmed(fundNumConfirmed);
        }
        if (buyFee != null && buyFee >= 0) {
            trdOrderDetail.setBuyFee(buyFee);
        }
        trdOrderDetail.setTradeApplySerial(tradeApplySerial);
        trdOrderDetail.setOrderDetailStatus(orderDetailStatus);
        trdOrderDetailRepository.save(trdOrderDetail);
    }

    @Override
    public void updateByParamWithSerial(String tradeApplySerial, int orderDetailStatus,
                                        Long updateDate, Long updateBy, Long id) {
        trdOrderDetailRepository.updateByParamWithSerial(tradeApplySerial,
                orderDetailStatus, updateDate, updateBy, id);
    }

    @Override
    public String getUserUUIDByUserId(Long userId) {
        com.shellshellfish.aaas.userinfo.grpc.UserId.Builder builder = com.shellshellfish.aaas
                .userinfo.grpc.UserId.newBuilder();
        builder.setUserId(userId);
        try {
            return userInfoServiceFutureStub.getUerUUIDByUserId(builder.build()).get().getUserUUID();
        } catch (InterruptedException e) {
            logger.error("exception:", e);
            return null;
        } catch (ExecutionException e) {
            logger.error("exception:", e);
            return null;
        }
    }

    @Override
    public TrdBrokerUser getBrokerUserByUserIdAndBandCard(Long userId, String bankCardNum) {
        TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndBankCardNum
                (userId, bankCardNum);
        return trdBrokerUser;
    }


    @Override
    public TrdOrder buyFinanceProductWithPreOrder(FinanceProdBuyInfo financeProdInfo)
            throws Exception {
        /**
         * 第一步先购买货币基金，用同步方式调用
         * 在这时候把parent order 生成 对应交易流水生成
         * 返回给用户购买成功与否的结果
         * 第二步
         * 在定时任务里面发现份额确认后，触发正常购买http 但是调用的是中证赎回申购接口
         * @param financeProdInfo
         * @return
         * @throws Exception
         */

        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        BeanUtils.copyProperties(financeProdInfo, productBaseInfo);
        //查询现在默认的货币基金作为preOrder
        List<ProductMakeUpInfo> productMakeUpInfos = financeProdInfoService.getFinanceProdMakeUpInfo
                (productBaseInfo);
        if (productMakeUpInfos.size() <= 0) {
            logger.error("failed to get prod make up informations!");
            throw new Exception("failed to get prod make up informations!");
        }
        //需要先用preOrder去调中证接口去发起扣款交易
        FinanceProdInfoQuery.Builder fpqBuilder = FinanceProdInfoQuery.newBuilder();
        fpqBuilder.setGroupId(financeProdInfo.getGroupId());
        fpqBuilder.setProdId(financeProdInfo.getProdId());
        FinanceMoneyFundInfo fmfiResult = financeProdInfoService.getMoneyFund(fpqBuilder.build());
        String fundCode = fmfiResult.getFundCode();
        String fundName = fmfiResult.getFundName();
        logger.info("get fundCode:" + fundCode + " for groupId:" + financeProdInfo.getGroupId() + " "
                + "prodId:" + financeProdInfo.getProdId());
        PreOrderPayResult preOrderPayResult = genPreOrderFromFundCodeAndBuyInfo(financeProdInfo, fundCode);
        if (!StringUtils.isEmpty(preOrderPayResult.getApplySerial())) {
            //说明该申购能够正常结束， 所以可以去生成初始化的产品申购流程足迹
            // 生成order 和 orderDetail 状态为等待支付
            //在用户理财产品系统里面生成用户的理财产品, 这是日后《我的理财产品》模块的依据
            Long userProdId = genUserProduct(financeProdInfo, productMakeUpInfos);
            financeProdInfo.setUserProdId(userProdId);
            //这时候真正的订单已经生成
            TrdOrder trdOrder = genOrderFromBuyInfoAndProdMakeUpInfo(financeProdInfo,
                    productMakeUpInfos, preOrderPayResult.getPreOrderId());
            return trdOrder;
        } else {
            logger.error("申购失败：{} ", preOrderPayResult.getErrMsg());
            throw new Exception("申购失败：" + preOrderPayResult.getErrMsg());
        }


    }

    @Override
    public TrdOrder buyPreOrderProduct(TrdPayFlow trdPayFlow) throws Exception {
        Long preOrderId = trdPayFlow.getOrderDetailId();
        logger.info("now start to order the preOrderId:" + preOrderId + " product");
        TrdOrder trdOrder = trdOrderRepository.findByPreOrderId(preOrderId);
        List<TrdPreOrder> trdPreOrders = trdPreOrderRepository.findAllById(preOrderId);

        if (trdOrder == null || trdPreOrders.size() <= 0) {
            logger.error("Failed to precess preOrder order process, because there is no history "
                    + "information there in trdOrderRepository with preOrderId:{}", preOrderId);
            throw new Exception("Failed to precess preOrder order process, because there is no history "
                    + "information there in trdOrderRepository with preOrderId:" + preOrderId);
        } else {
            //用事先保存的prod_id 和group_id去查询产品配比，然后去更新用户productDetail, 并且真正发起order
            Long prodId = trdOrder.getProdId();
            Long groupId = trdOrder.getGroupId();
            List<ProductMakeUpInfo> productMakeUpInfos = financeProdInfoService.getFinanceProdMakeUpInfo
                    (prodId, groupId);
            Long preOrderFundShares = trdPayFlow.getTradeConfirmShare();
            logger.info("preOrderFundNumber : " + preOrderFundShares);
            PayPreOrderDto payPreOrderDto = new PayPreOrderDto();
            payPreOrderDto.setOriginFundCode(trdPayFlow.getFundCode());
            payPreOrderDto.setTrdBrokerId(trdPayFlow.getTradeBrokeId().intValue());
            payPreOrderDto.setTrdAccount(trdPayFlow.getTradeAcco());
            payPreOrderDto.setUserProdId(trdPayFlow.getUserProdId());
            payPreOrderDto.setUserPid(userInfoService.getUserBankInfo(trdOrder.getUserId()).getUserPid());
            payPreOrderDto.setUserUuid("" + trdOrder.getUserId());
            List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails = new
                    ArrayList<>();
            List<TrdOrderDetail> trdOrderDetailFromDb = trdOrderDetailRepository.findAllByOrderId
                    (trdOrder.getOrderId());
            Map<String, TrdOrderDetail> trdOrderDetailMap = new HashMap<>();
            for (TrdOrderDetail trdOrderDetail : trdOrderDetailFromDb) {
                trdOrderDetailMap.put(trdOrderDetail.getFundCode(), trdOrderDetail);
            }
            for (ProductMakeUpInfo productMakeUpInfo : productMakeUpInfos) {
                TrdOrderDetail trdOrderDetailOrigin = trdOrderDetailMap.get(productMakeUpInfo.getFundCode
                        ());
                com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderDetail = new com
                        .shellshellfish.aaas.common.message.order.TrdOrderDetail();
                trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.CONVERTWAITCONFIRM.getStatus());

                Long result = TradeUtil.getBigDecimalNumWithDiv10000(productMakeUpInfo.getFundShare())
                        .multiply(BigDecimal.valueOf(trdPreOrders.get(0).getFundShareConfirmed())).longValue();
                logger.info("origin total fund share:" + trdPreOrders.get(0).getFundShareConfirmed() + " multipul "
                        + "with:" + productMakeUpInfo.getFundShare() + " and got result:" + result);
                trdOrderDetail.setFundNum(result);
                trdOrderDetail.setId(trdOrderDetailOrigin.getId());
                trdOrderDetail.setFundCode(productMakeUpInfo.getFundCode());
                trdOrderDetail.setUserId(trdPayFlow.getUserId());
                trdOrderDetails.add(trdOrderDetail);
            }
            payPreOrderDto.setOrderDetailList(trdOrderDetails);
//      broadcastMessageProducer.sendPayMessages(payPreOrderDto);
        }

        return trdOrder;
    }

    /**
     * 用preOrderId 作为第三个参数参与到Order 和OrderDetail的记录创建中
     * 生成记录后不直接发起交易，而是等定时任务去触发交易
     *
     * @param financeProdInfo
     * @param productMakeUpInfos
     * @param preOrderId
     * @return
     */
    private TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdInfo,
                                                          List<ProductMakeUpInfo> productMakeUpInfos, long preOrderId) throws Exception {
        PayOrderDto payOrderDto = new PayOrderDto();
        Map brokerWithTradeAcco = getOrMakeTradeAcco(financeProdInfo);
        if (CollectionUtils.isEmpty(brokerWithTradeAcco)) {
            logger.error("Failed to make trade account for user:{}", financeProdInfo.getUserId());
            throw new Exception("Failed to make trade account for user:" + financeProdInfo.getUserId());
        }
        //默认用第一组数据， 因为现在只有一个交易平台
        int trdBrokerId = -1;
        String trdAcco = null;
        trdBrokerId = (int) brokerWithTradeAcco.keySet().toArray()[0];
        String trdAccoOrig = (String) brokerWithTradeAcco.get(Integer.valueOf(trdBrokerId));
        String items[] = trdAccoOrig.split("\\|");
        trdAcco = items[0];
        payOrderDto.setUserPid(items[1]);
        payOrderDto.setRiskLevel(Integer.parseInt(items[2]));
        String orderId = TradeUtil.generateOrderIdByBankCardNum(financeProdInfo.getBankAcc(), trdBrokerId);
        payOrderDto.setTrdAccount(trdAcco);
        payOrderDto.setUserUuid(financeProdInfo.getUuid());
        payOrderDto.setUserProdId(financeProdInfo.getUserProdId());
        List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails = new
                ArrayList<com.shellshellfish.aaas.common.message.order.TrdOrderDetail>();

        TrdOrder trdOrder = new TrdOrder();
        MyBeanUtils.mapEntityIntoDTO(financeProdInfo, trdOrder);
        trdOrder.setBankCardNum(financeProdInfo.getBankAcc());
        trdOrder.setOrderDate(TradeUtil.getUTCTime());
        trdOrder.setCreateDate(TradeUtil.getUTCTime());
        trdOrder.setOrderType(TrdOrderOpTypeEnum.BUY.getOperation());
        trdOrder.setProdId(financeProdInfo.getProdId());
        trdOrder.setGroupId(financeProdInfo.getGroupId());
        trdOrder.setUserProdId(financeProdInfo.getUserProdId());
        trdOrder.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.ordinal());
        trdOrder.setOrderId(orderId);
        trdOrder.setUserId(financeProdInfo.getUserId());
        trdOrder.setPreOrderId(preOrderId);
        trdOrder.setCreateBy(financeProdInfo.getUserId());
        trdOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdInfo.getMoney()));
        trdOrderRepository.save(trdOrder);
        //generate sub order for each funds
        for (ProductMakeUpInfo productMakeUpInfo : productMakeUpInfos) {
            TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
            trdOrderDetail.setOrderId(trdOrder.getOrderId());
            trdOrderDetail.setUserId(trdOrder.getUserId());
            //规定基金占比用百分比并且精确万分之一
            BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
                    (BigDecimal.valueOf(10000));
            trdOrderDetail.setFundSum(fundRatio.multiply(financeProdInfo.getMoney())
                    .multiply(BigDecimal.valueOf(100)).toBigInteger()
                    .longValue());
            trdOrderDetail.setBuysellDate(TradeUtil.getUTCTime());
            trdOrderDetail.setCreateBy(financeProdInfo.getUserId());
            trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setUpdateBy(financeProdInfo.getUserId());
            trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setUserId(financeProdInfo.getUserId());
            trdOrderDetail.setFundCode(productMakeUpInfo.getFundCode());
            trdOrderDetail.setUserProdId(trdOrder.getUserProdId());
            trdOrderDetail.setTradeType(trdOrder.getOrderType());
            trdOrderDetail.setFundShare(productMakeUpInfo.getFundShare());
            trdOrderDetail = trdOrderDetailRepository.save(trdOrderDetail);
            com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderPay = new com
                    .shellshellfish.aaas.common.message.order.TrdOrderDetail();
            BeanUtils.copyProperties(trdOrderDetail, trdOrderPay);
            trdOrderPay.setOrderId(trdOrderDetail.getOrderId());
            trdOrderPay.setUserId(financeProdInfo.getUserId());
//      GenericMessage<TrdOrderDetail> genericMessage = new GenericMessage<TrdOrderDetail>();
            trdOrderDetails.add(trdOrderPay);
        }

        return trdOrder;

    }

    private PreOrderPayResult genPreOrderFromFundCodeAndBuyInfo(FinanceProdBuyInfo financeProdInfo,
                                                                String fundCode) throws ExecutionException, InterruptedException {
        //第一步生成订单后插数据库
        //第二步生产grpc调用请求数据
        TrdPreOrder trdPreOrder = new TrdPreOrder();
        trdPreOrder.setBankCardNum(financeProdInfo.getBankAcc());
        trdPreOrder.setCreateBy(financeProdInfo.getUserId());
        trdPreOrder.setCreateDate(TradeUtil.getUTCTime());
        trdPreOrder.setOrderDate(TradeUtil.getUTCTime());
        trdPreOrder.setFundCode(fundCode);
        trdPreOrder.setOrderStatus(TrdOrderStatusEnum.WAITPAY.getStatus());
        trdPreOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdInfo.getMoney()));
        trdPreOrder.setProdId(financeProdInfo.getProdId());
        Long userId = null;
        if (financeProdInfo.getUserId() == null || financeProdInfo.getUserId() == 0) {
            userId = getUserId(financeProdInfo.getUuid());
        }
        if (null == userId) {
            throw new IllegalArgumentException("userUUID:" + financeProdInfo.getUuid() + " cannot be "
                    + "found for correspond userId in system");
        }
        trdPreOrder.setUserId(userId);
        trdPreOrderRepository.save(trdPreOrder);
        UserBankInfo userBankInfo = userInfoService.getUserBankInfo(userId);
        PreOrderPayReq.Builder poprBuilder = PreOrderPayReq.newBuilder();

        poprBuilder.setBankCardNum(financeProdInfo.getBankAcc());
        poprBuilder.setOrderType(TrdOrderOpTypeEnum.PREORDER.getOperation());
        poprBuilder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdInfo.getMoney()));
        poprBuilder.setProdId(financeProdInfo.getProdId());
        poprBuilder.setUserId(userId);
        poprBuilder.setFundCode(fundCode);
        poprBuilder.setUserPid(userBankInfo.getUserPid());
        PreOrderPayResult result = payService.preOrder2Pay(poprBuilder.build());
        if (StringUtils.isEmpty(result.getApplySerial())) {
            //说明中证支付接口调用失败没有生产流水号
            logger.error("failed to preOrder for :" + fundCode + "with error:" + result.getErrMsg());
            trdPreOrderRepository.updateByParam(TrdOrderStatusEnum.FAILED.getStatus(), result.getErrMsg(), TradeUtil
                    .getUTCTime(), userId, trdPreOrder.getId());
        } else {
            trdPreOrderRepository.updateByParam(TrdOrderStatusEnum.WAITPAY.getStatus(), result.getErrMsg(),
                    TradeUtil.getUTCTime(), userId, trdPreOrder.getId());
        }
        return result;
    }

    private TrdOrder firstStepBuySpecialFunds(FinanceProdBuyInfo financeProdBuyInfo) throws Exception {
        //Todo: add code
        return null;
    }

    @Override
    public Map<String, Object> buyDeatils(String orderId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> serialList = new ArrayList<String>();
        if (StringUtils.isEmpty(orderId)) {
            logger.error("详情信息数据不存在:{}", orderId);
            throw new Exception("详情信息数据不存在:" + orderId);
        }
        logger.info("详情信息数据为:" + orderId);
        TrdOrder trdOrder = orderService.getOrderByOrderId(orderId);
        List<TrdOrderDetail> trdOrderDetailList;
        logger.info("trdOrder ===>" + trdOrder);
        if (trdOrder != null && trdOrder.getOrderId() != null) {
            logger.info("trdOrder.getOrderId()===>" + trdOrder.getOrderId());
            result.put("prodId", trdOrder.getUserProdId());
            trdOrderDetailList = orderService.findOrderDetailByOrderId(orderId);
        } else {
            logger.error("详情信息数据不存在.");
            throw new Exception("详情信息数据不存在.");
        }
        //result.put("list", trdOrderDetailList);
        if (trdOrderDetailList == null || trdOrderDetailList.isEmpty()) {
            logger.error("详情信息数据不存在.");
            throw new Exception("详情信息数据不存在.");
        }

        result.put("orderType", TrdOrderOpTypeEnum.getComment(trdOrder.getOrderType()));
        //金额
        long amount = trdOrder.getPayAmount();
        BigDecimal bigDecimalAmount = BigDecimal.ZERO;
        if (amount != 0) {
            bigDecimalAmount = TradeUtil.getBigDecimalNumWithDiv100(amount);
        }

        result.put("amount", bigDecimalAmount);
        //手续费
        if (trdOrder.getPayFee() == null) {
            result.put("payfee", "");
        } else {
            result.put("payfee", TradeUtil.getBigDecimalNumWithDiv100(trdOrder.getPayFee()));
        }
        //状态详情
        List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
        Map<String, Object> detailMap;
        Map<String, String> statusMap = new HashMap<>();
        String status = "";
        for (int i = 0; i < trdOrderDetailList.size(); i++) {
            detailMap = new HashMap<>();
            TrdOrderDetail trdOrderDetail = trdOrderDetailList.get(i);
            int detailStatus = trdOrderDetail.getOrderDetailStatus();
            if (TrdOrderStatusEnum.CONFIRMED.getStatus() == detailStatus
                    || TrdOrderStatusEnum.SELLCONFIRMED.getStatus() == detailStatus) {
                status = CombinedStatusEnum.CONFIRMED.getComment();
            } else if (TrdOrderStatusEnum.FAILED.getStatus() == detailStatus
                    || TrdOrderStatusEnum.REDEEMFAILED.getStatus() == detailStatus) {
                status = CombinedStatusEnum.CONFIRMEDFAILED.getComment();
            } else {
                if(trdOrderDetail.getFundMoneyQuantity() > 0) {
                  status = CombinedStatusEnum.WAITCONFIRM.getComment();
                }
            }
            detailMap.put("fundstatus", status);
            statusMap.put(status, status);

            Long instanceLong = trdOrderDetail.getCreateDate();
            detailMap.put("fundCode", trdOrderDetail.getFundCode());
            //基金费用
            detailMap.put("fundbuyFee", trdOrderDetail.getBuyFee());
//			if(trdOrderDetail.getBuyFee() <= 0){
//			  detailMap.put("fundstatus", CombinedStatusEnum.CONFIRMED.getComment());
//			}
			//交易金额


      Long fundSum = 0L;
      if(trdOrderDetail.getFundSumConfirmed() != null && trdOrderDetail.getFundSumConfirmed() > 0){
        fundSum = trdOrderDetail.getFundSumConfirmed();
      }else if(trdOrderDetail.getFundSum() != null && trdOrderDetail.getFundSum() > 0){
        fundSum = trdOrderDetail.getFundSum();
      }
      if(fundSum <= 0){
        logger.info("trdOrderDetail.getFundSumConfirmed:{} trdOrderDetail.getFundSum:{} of "
                + "trdOrderDetail.getId:{}",
            trdOrderDetail.getFundSumConfirmed(), trdOrderDetail.getFundSum(), trdOrderDetail
                .getId() );
        continue;
      }
      detailMap.put("fundSum", TradeUtil.getBigDecimalNumWithDiv100(fundSum));
      detailMap.put("targetSellPercent", trdOrder.getSellPercent());

            //FIXME  交易日判断逻辑使用asset allocation 中的TradeUtils
            //QDII 基金　15个交易确认　其他　１个交易日确认
            String date = InstantDateUtil.getTplusNDayNWeekendOfWork(instanceLong, QDII.isQDII(trdOrderDetail
                    .getFundCode()) ? 15 : 1);
            String dayOfWeek = DayOfWeekZh.of(InstantDateUtil.format(date).getDayOfWeek()).toString();

            detailMap.put("funddate", date);
            if (status.equals(CombinedStatusEnum.WAITCONFIRM.getComment())) {
                detailMap.put("fundTitle", "将于" + date + "(" + dayOfWeek + ")确认");
            } else {
                detailMap.put("fundTitle", "已于" + date + "(" + dayOfWeek + ")确认");
            }
            detailMap.put("fundTradeType", TrdOrderOpTypeEnum.getComment(trdOrderDetail.getTradeType()));
            detailList.add(detailMap);

            String serial = trdOrderDetail.getTradeApplySerial();
            if (!StringUtils.isEmpty(serial)) {
                serialList.add(serial);
            }
        }

        if (statusMap != null && statusMap.size() > 0) {
            if (statusMap.size() != 1) {
                if (statusMap.containsKey(CombinedStatusEnum.CONFIRMEDFAILED.getComment())
                        && statusMap.containsKey(CombinedStatusEnum.CONFIRMED.getComment())) {
                    result.put("orderStatus", CombinedStatusEnum.SOMECONFIRMED.getComment());
                }
                if (statusMap.containsKey(CombinedStatusEnum.WAITCONFIRM.getComment())
                        && statusMap.containsKey(CombinedStatusEnum.CONFIRMED.getComment())) {
                    result.put("orderStatus", CombinedStatusEnum.WAITCONFIRM.getComment());
                }
            } else {
                for (String key : statusMap.keySet()) {
                    if (statusMap.size() == 1) {
                        result.put("orderStatus", key);
                    }
                }
            }
        }

        result.put("detailList", detailList);
        result.put("serialList", serialList);
        result.put("operation", trdOrder.getOrderStatus());
        result.put("orderDate", trdOrder.getOrderDate());
		return result;
	}

	@Override
	public Map<String, Object> sellDeatils(String orderId) throws Exception {
	  Map<String, Object> result = new HashMap<String, Object>();
	  List<String> serialList = new ArrayList<String>();
	  if (StringUtils.isEmpty(orderId)) {
	    logger.error("详情信息数据不存在:{}", orderId);
	    throw new Exception("详情信息数据不存在:" + orderId);
	  }
	  logger.info("详情信息数据为:" + orderId);
	  TrdOrder trdOrder = orderService.getOrderByOrderId(orderId);
	  List<TrdOrderDetail> trdOrderDetailList = new ArrayList<TrdOrderDetail>();
	  logger.info("trdOrder ===>"+trdOrder);
	  if (trdOrder != null && trdOrder.getOrderId() != null) {
	    logger.info("trdOrder.getOrderId()===>"+trdOrder.getOrderId());
	    result.put("prodId", trdOrder.getUserProdId());
	    trdOrderDetailList = orderService.findOrderDetailByOrderId(orderId);
	  } else {
	    logger.error("详情信息数据不存在.");
	    throw new Exception("详情信息数据不存在.");
	  }
	  if (trdOrderDetailList == null || trdOrderDetailList.isEmpty()) {
	    logger.error("详情信息数据不存在.");
	    throw new Exception("详情信息数据不存在.");
	  }

	  result.put("orderType", TrdOrderOpTypeEnum.getComment(trdOrder.getOrderType()));
	  if(trdOrder.getSellPercent() > 0) {
      result.put("sellTargetPercent", TradeUtil.getBigDecimalNumWithDiv100(trdOrder.getSellPercent
          ()));
    }else{
	    logger.error("The order:{} is not sell with percent, sell target money:{}", trdOrder
          .getOrderId(), trdOrder.getPayAmount());
    }
	  //金额
	  long amount = trdOrder.getPayAmount();
	  BigDecimal bigDecimalAmount = BigDecimal.ZERO;
	  if (amount != 0) {
	    bigDecimalAmount = TradeUtil.getBigDecimalNumWithDiv100(amount);
	  }

	  result.put("amount", bigDecimalAmount);
	  //手续费
	  if (trdOrder.getPayFee() == null) {
	    result.put("payfee", "");
	  } else {
	    result.put("payfee", TradeUtil.getBigDecimalNumWithDiv100(trdOrder.getPayFee()));
	  }
	  //状态详情
	  List<Map<String, Object>> detailList = new ArrayList<>();
	  Map<String, Object> detailMap;
	  Map<String, String> statusMap = new HashMap<>();

	  Long totalNum = 0L;
	  for (int i = 0; i < trdOrderDetailList.size(); i++) {
	    detailMap = new HashMap<>();
	    TrdOrderDetail trdOrderDetail = trdOrderDetailList.get(i);
	    int detailStatus = trdOrderDetail.getOrderDetailStatus();
	    String status;
	    if (TrdOrderStatusEnum.CONFIRMED.getStatus() == detailStatus
	        || TrdOrderStatusEnum.SELLCONFIRMED.getStatus() == detailStatus) {
	      status = CombinedStatusEnum.CONFIRMED.getComment();
	    } else if (TrdOrderStatusEnum.FAILED.getStatus() == detailStatus
	        || TrdOrderStatusEnum.REDEEMFAILED.getStatus() == detailStatus) {
	      status = CombinedStatusEnum.CONFIRMEDFAILED.getComment();
	    } else {
	      status = CombinedStatusEnum.WAITCONFIRM.getComment();
	    }
	    detailMap.put("fundstatus", status);
	    statusMap.put(status, status);
	    Long instanceLong = trdOrderDetail.getCreateDate();
	    detailMap.put("fundCode", trdOrderDetail.getFundCode());
//	    //基金费用
//	    detailMap.put("fundbuyFee", trdOrderDetail.getBuyFee());
	    //基金份额
      //交易金额
      Long fundNum = 0L;
      if(trdOrderDetail.getFundNumConfirmed() != null && trdOrderDetail.getFundNumConfirmed() > 0){
        fundNum = trdOrderDetail.getFundNumConfirmed();
      }else if(trdOrderDetail.getFundNum() != null && trdOrderDetail.getFundNum() > 0){
        fundNum = trdOrderDetail.getFundNum();
      }else{
        logger.error("trdOrderDetail id:{} have no fundNum or fundNumConfirmed",trdOrderDetail
            .getId());
      }

      if(fundNum <= 0){
        logger.info("trdOrderDetail.getFundNum:{} trdOrderDetail.getFundNumConfirmed:{} of "
                + "trdOrderDetail.getId:{}",
            trdOrderDetail.getFundNum(), trdOrderDetail.getFundNumConfirmed(), trdOrderDetail
                .getId() );
        continue;
      }
	    totalNum = totalNum + fundNum;
	    detailMap.put("fundnum", TradeUtil.getBigDecimalNumWithDiv100(fundNum));

            //交易金额
            Long fundSum;
            if (trdOrderDetail.getFundSumConfirmed() != null && trdOrderDetail.getFundSumConfirmed() > 0) {
                fundSum = trdOrderDetail.getFundSumConfirmed();
            } else if (trdOrderDetail.getFundSum() != null && trdOrderDetail.getFundSum() > 0) {
                fundSum = trdOrderDetail.getFundSum();
            } else {
                fundSum = trdOrderDetail.getFundMoneyQuantity();
            }

            detailMap.put("fundSum", TradeUtil.getBigDecimalNumWithDiv100(fundSum));

            //FIXME  交易日判断逻辑使用asset allocation 中的TradeUtils
            //QDII 基金　15个交易确认　其他　１个交易日确认
            String date = InstantDateUtil.getTplusNDayNWeekendOfWork(instanceLong, QDII.isQDII(trdOrderDetail
                    .getFundCode()) ? 15 : 1);
            String dayOfWeek = DayOfWeekZh.of(InstantDateUtil.format(date).getDayOfWeek()).toString();

            detailMap.put("funddate", date);
            if (status.equals(CombinedStatusEnum.WAITCONFIRM.getComment())) {
                detailMap.put("fundTitle", "将于" + date + "(" + dayOfWeek + ")确认");
            } else {
                detailMap.put("fundTitle", "已于" + date + "(" + dayOfWeek + ")确认");
            }
            detailMap.put("fundTradeType", TrdOrderOpTypeEnum.getComment(trdOrderDetail.getTradeType()));
            detailList.add(detailMap);

            String serial = trdOrderDetail.getTradeApplySerial();
            if (!StringUtils.isEmpty(serial)) {
                serialList.add(serial);
            }
        }

        result.put("totalNum", TradeUtil.getBigDecimalNumWithDiv100(totalNum));

        if (statusMap != null && statusMap.size() > 0) {
            if (statusMap.size() != 1) {
                if (statusMap.containsKey(CombinedStatusEnum.CONFIRMEDFAILED.getComment())
                        && statusMap.containsKey(CombinedStatusEnum.CONFIRMED.getComment())) {
                    result.put("orderStatus", CombinedStatusEnum.SOMECONFIRMED.getComment());
                }
                if (statusMap.containsKey(CombinedStatusEnum.WAITCONFIRM.getComment())
                        && statusMap.containsKey(CombinedStatusEnum.CONFIRMED.getComment())) {
                    result.put("orderStatus", CombinedStatusEnum.WAITCONFIRM.getComment());
                }
            } else {
                for (String key : statusMap.keySet()) {
                    if (statusMap.size() == 1) {
                        result.put("orderStatus", key);
                    }
                }
            }
        }

        result.put("detailList", detailList);
        result.put("serialList", serialList);
        result.put("operation", trdOrder.getOrderStatus());
        result.put("orderDate", trdOrder.getCreateDate());
        return result;
    }

    @Override
    public Map<String, Object> getOrderInfos(String uuid, Long prodId, int orderType) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        UserInfo userInfo = tradeOpService.getUserInfoByUserUUID(uuid);

        Long userId = userInfo.getId();
        TrdOrder trdOrder = orderService.findOrderByUserProdIdAndUserIdAndorderType(prodId, userId, orderType);

        String bankNum = trdOrder.getBankCardNum();
        String orderId = trdOrder.getOrderId();
        result.put("bankNum", bankNum);
        result.put("orderId", orderId);
        Long buyFee = new Long(0);
        if (!StringUtils.isEmpty(orderId)) {
            List<TrdOrderDetail> trdOrderDetailsList = orderService.findOrderDetailByOrderId(orderId);
            if (trdOrderDetailsList != null && trdOrderDetailsList.size() > 0) {
                for (int i = 0; i < trdOrderDetailsList.size(); i++) {
                    TrdOrderDetail trdOrderDetail = trdOrderDetailsList.get(i);
                    if (trdOrderDetail.getBuyFee() != null) {
                        buyFee = buyFee + trdOrderDetail.getBuyFee();
                    }
                }
            }
        }

        result.put("buyFee", buyFee);
        return result;
    }


}
