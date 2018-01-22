package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdPreOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdPreOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
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

  boolean useMsgToBuy = true;


  @PostConstruct
  public void init(){
    userInfoServiceFutureStub = UserInfoServiceGrpc.newFutureStub(managedUIChannel);

  }

  public void shutdown() throws InterruptedException {
    managedUIChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  @Override
  @Transactional( propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
  public TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdBuyInfo)
      throws Exception {
    ProductBaseInfo productBaseInfo = new ProductBaseInfo();
    BeanUtils.copyProperties(financeProdBuyInfo, productBaseInfo);
    List<ProductMakeUpInfo> productMakeUpInfos =  financeProdInfoService.getFinanceProdMakeUpInfo
        (productBaseInfo);
    if(productMakeUpInfos.size() <=0 ){
      logger.info("failed to get prod make up informations!");
      throw new Exception("failed to get prod make up informations!");
    }
    //在用户理财产品系统里面生成用户的理财产品, 这是日后《我的理财产品》模块的依据
    Long userProdId =  genUserProduct(financeProdBuyInfo, productMakeUpInfos);
    financeProdBuyInfo.setUserProdId(userProdId);
    return genOrderFromBuyInfoAndProdMakeUpInfo(financeProdBuyInfo, productMakeUpInfos);

  }

  private Long genUserProduct(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos) throws Exception {
    FinanceProdInfosQuery.Builder requestBuilder = FinanceProdInfosQuery.newBuilder();
    requestBuilder.setUserId(financeProdBuyInfo.getUserId());

    FinanceProdInfoCollection.Builder subReqBuilder = FinanceProdInfoCollection.newBuilder();
    FinanceProdInfo.Builder finProdInfoBuilder = FinanceProdInfo.newBuilder();
    for( ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      BeanUtils.copyProperties(productMakeUpInfo, finProdInfoBuilder);
      requestBuilder.addProdList(finProdInfoBuilder);
      finProdInfoBuilder.clear();
    }
    Long userProdId = userInfoServiceFutureStub.genUserProdsFromOrder(requestBuilder
        .build()).get().getUserProdId();
    if(userProdId == -1L){
      logger.error("userProdId is not greater than 0, means some error happened" + userProdId);
      throw new Exception("Failed to create userProd and userProdDetail");
    }
    return userProdId;
  }



  TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos) throws Exception {
    //generate order
//    TrdTradeBroker trdTradeBroker = trdBrokderRepository.findOne(1L);
    PayOrderDto payOrderDto = new PayOrderDto();
    Map brokerWithTradeAcco = getOrMakeTradeAcco(financeProdBuyInfo);
    if(CollectionUtils.isEmpty(brokerWithTradeAcco)){
      logger.error("Failed to make trade account for user:"+ financeProdBuyInfo.getUserId());
      throw new Exception("Failed to make trade account for user:"+ financeProdBuyInfo.getUserId());
    }
    //默认用第一组数据， 因为现在只有一个交易平台
    int trdBrokerId = -1;
    String trdAcco = null;
    trdBrokerId = (int) brokerWithTradeAcco.keySet().toArray()[0];
    String trdAccoOrig = (String) brokerWithTradeAcco.get(Integer.valueOf(trdBrokerId));
    String items[] = trdAccoOrig.split("\\|");
    trdAcco = items[0];
    payOrderDto.setUserPid(items[1]);
    String orderId = TradeUtil.generateOrderIdByBankCardNum(financeProdBuyInfo.getBankAcc(), trdBrokerId);
    payOrderDto.setTrdAccount(trdAcco);
    payOrderDto.setUserUuid(financeProdBuyInfo.getUuid());
    payOrderDto.setUserProdId(financeProdBuyInfo.getUserProdId());
    List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails =  new
        ArrayList<com.shellshellfish.aaas.common.message.order.TrdOrderDetail>();

    TrdOrder trdOrder = new TrdOrder();
    trdOrder.setBankCardNum(financeProdBuyInfo.getBankAcc());
    trdOrder.setOrderDate(TradeUtil.getUTCTime());
    trdOrder.setCreateDate(TradeUtil.getUTCTime());
    trdOrder.setOrderType(TrdOrderOpTypeEnum.BUY.getOperation());
    trdOrder.setProdId(financeProdBuyInfo.getProdId());
    trdOrder.setUserProdId(financeProdBuyInfo.getUserProdId());
    trdOrder.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.ordinal());
    trdOrder.setOrderId(orderId);
    trdOrder.setUserId(financeProdBuyInfo.getUserId());
    trdOrder.setCreateBy(financeProdBuyInfo.getUserId());
    trdOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdBuyInfo.getMoney()));
    trdOrderRepository.save(trdOrder);
    //generate sub order for each funds
    for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      trdOrderDetail.setOrderId(trdOrder.getOrderId());
      trdOrderDetail.setUserId(trdOrder.getUserId());
      //规定基金占比用百分比并且精确万分之一
      BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
          (BigDecimal.valueOf(10000));
      trdOrderDetail.setFundMoneyQuantity(fundRatio.multiply(financeProdBuyInfo.getMoney())
          .multiply(BigDecimal.valueOf(100)).toBigInteger()
          .longValue());
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
      com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderPay =  new com
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

    int trdBrokerId = -1 ;
    String bankCardNum = null;
    String trdAcco = null;
    String userPid = null;
    UserBankInfo userBankInfo = userInfoService.getUserBankInfo(financeProdBuyInfo.getUserId());
    for(CardInfo cardInfo:userBankInfo.getCardNumbersList()){
      if(cardInfo.getCardNumbers().equals(financeProdBuyInfo.getBankAcc())){
        userPid = cardInfo.getUserPid();
        break;
      }
    }


    if(StringUtils.isEmpty(userPid)){
      logger.error("this user: "+financeProdBuyInfo.getUserId()+" personal id is not in "
          + "ui_bankcard" );
      throw new Exception("this user: "+financeProdBuyInfo.getUserId()+" personal id is not in "
          + "ui_bankcard");
    }
    if(!CollectionUtils.isEmpty(trdBrokerUsers)){
      trdBrokerId = trdBrokerUsers.get(0).getTradeBrokerId().intValue();
      bankCardNum = financeProdBuyInfo.getBankAcc();
      trdAcco = trdBrokerUsers.get(0).getTradeAcco();
    }else if(CollectionUtils.isEmpty(trdBrokerUsers) || StringUtils.isEmpty(trdAcco)){

      logger.info("trdBrokerUsers.get(0).getTradeAcco() is empty, 坑货出现，需要生成交易账号再交易");


      BindBankCard bindBankCard = new BindBankCard();
      TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndAndBankCardNum
          (userBankInfo.getUserId(),financeProdBuyInfo.getBankAcc());
      bindBankCard.setBankCardNum(financeProdBuyInfo.getBankAcc());
      String bankName = BankUtil.getNameOfBank(financeProdBuyInfo.getBankAcc());
      bankName = bankName.split("银行")[0] + "银行";
      TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankNameAndTraderBrokerId
          (bankName, TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
      if(null == trdTradeBankDic){
        logger.error("this bank name:"+bankName+" with brokerId"+ TradeBrokerIdEnum
            .ZhongZhenCaifu.getTradeBrokerId()+" is not in table:");
        throw new Exception("this bank name:"+bankName
            + " with brokerId"+ TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId()+" is not in table:");
      }
//      userPid = bindBankCard.getUserPid();
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
      TrdBrokerUser trdBrokerUserNew = new TrdBrokerUser();
      trdBrokerUserNew.setBankCardNum(bindBankCard.getBankCardNum());
      trdBrokerUserNew.setCreateBy(financeProdBuyInfo.getUserId());
      trdBrokerUserNew.setCreateDate(TradeUtil.getUTCTime());
      trdBrokerUserNew.setTradeAcco(trdAcco);
      trdBrokerUserNew.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
      trdBrokerUserNew.setUserId(financeProdBuyInfo.getUserId());
      trdBrokerUserNew.setUpdateBy(financeProdBuyInfo.getUserId());
      trdBrokerUserNew.setUpdateDate(TradeUtil.getUTCTime());
      trdBrokerUserRepository.save(trdBrokerUserNew);

//      trdBrokerUserRepository.updateTradeAcco(trdAcco, TradeUtil.getUTCTime(), bindBankCard
//          .getUserId(),  bindBankCard.getUserId());

    }

    //因为要提取用户身份证号码，所以这里拼接回去，要优化吗？
    result.put(trdBrokerId, trdAcco+"|"+userPid);
    return result;
  }


  private void sendOutOrder(PayOrderDto payOrderDto){
    if(useMsgToBuy){
      logger.info("use message queue to send payOrderDto");
      broadcastMessageProducer.sendPayMessages(payOrderDto);
    }else{
      logger.info("use grpc to send payOrderDto");
      payService.order2Pay(payOrderDto);
    }
  }


//  private void sendOutOrder(PayPreOrderDto payPreOrderDto){
//
//    logger.info("use message queue to send payPreOrderDto");
////    broadcastMessageProducer.sendPayMessages(payPreOrderDto);
//
//  }

  @Override
  public Long getUserId(String userUuid) throws ExecutionException, InterruptedException {
    UserIdQuery.Builder builder = UserIdQuery.newBuilder();
    builder.setUuid(userUuid);
    com.shellshellfish.aaas.userinfo.grpc.UserId userId = userInfoServiceFutureStub.getUserId(builder
        .build()).get();
    return userId.getUserId();
  }

  @Override
  @Transactional
  public void updateByParam(String tradeApplySerial, Long updateDate, Long updateBy, Long id,
      int orderDetailStatus) {
    trdOrderDetailRepository.updateByParam(tradeApplySerial,orderDetailStatus, updateDate,
        updateBy,  id );
  }

  @Override
  public String getUserUUIDByUserId(Long userId){
    com.shellshellfish.aaas.userinfo.grpc.UserId.Builder builder = com.shellshellfish.aaas
        .userinfo.grpc.UserId.newBuilder();
    builder.setUserId(userId);
    try {
      return userInfoServiceFutureStub.getUerUUIDByUserId(builder.build()).get().getUserUUID();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    } catch (ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public TrdBrokerUser getBrokerUserByUserIdAndBandCard(Long userId, String bankCardNum) {
    TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndAndBankCardNum
        (userId,bankCardNum);
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

    List<ProductMakeUpInfo> productMakeUpInfos =  financeProdInfoService.getFinanceProdMakeUpInfo
        (productBaseInfo);
    if(productMakeUpInfos.size() <=0 ){
      logger.info("failed to get prod make up informations!");
      throw new Exception("failed to get prod make up informations!");
    }

    //需要先用preOrder去调中证接口去发起扣款交易
    FinanceProdInfoQuery.Builder fpqBuilder = FinanceProdInfoQuery.newBuilder();
    fpqBuilder.setGroupId(financeProdInfo.getGroupId());
    fpqBuilder.setProdId(financeProdInfo.getProdId());
    FinanceMoneyFundInfo fmfiResult = financeProdInfoService.getMoneyFund(fpqBuilder.build());
    String fundCode = fmfiResult.getFundCode();
    String fundName = fmfiResult.getFundName();
    logger.info("get fundCode:"+ fundCode+ " for groupId:" + financeProdInfo.getGroupId() + " "
        + "prodId:" + financeProdInfo.getProdId());
    PreOrderPayResult preOrderPayResult = genPreOrderFromFundCodeAndBuyInfo(financeProdInfo, fundCode);
    if(!StringUtils.isEmpty(preOrderPayResult.getApplySerial())){
      //说明该申购能够正常结束， 所以可以去生成初始化的产品申购流程足迹
      // 生成order 和 orderDetail 状态为等待支付
      //在用户理财产品系统里面生成用户的理财产品, 这是日后《我的理财产品》模块的依据
      Long userProdId =  genUserProduct(financeProdInfo, productMakeUpInfos);
      financeProdInfo.setUserProdId(userProdId);
      //这时候真正的订单已经生成
      TrdOrder trdOrder =  genOrderFromBuyInfoAndProdMakeUpInfo(financeProdInfo,
          productMakeUpInfos, preOrderPayResult.getPreOrderId() );
      return trdOrder;
    }else{
      throw new Exception("申购失败："+ preOrderPayResult.getErrMsg());
    }


  }

  @Override
  public TrdOrder buyPreOrderProduct(TrdPayFlow trdPayFlow) throws Exception {
    Long preOrderId = trdPayFlow.getOrderDetailId();
    logger.info("now start to order the preOrderId:"+preOrderId+" product");
    TrdOrder trdOrder = trdOrderRepository.findByPreOrderId(preOrderId);
    List<TrdPreOrder> trdPreOrders = trdPreOrderRepository.findAllById(preOrderId);

    if(trdOrder == null || trdPreOrders.size() <= 0){
      logger.error("Failed to precess preOrder order process, because there is no history "
          + "information there in trdOrderRepository with preOrderId:"+ preOrderId);
      throw new Exception("Failed to precess preOrder order process, because there is no history "
          + "information there in trdOrderRepository with preOrderId:"+ preOrderId);
    }else{
      //用事先保存的prod_id 和group_id去查询产品配比，然后去更新用户productDetail, 并且真正发起order
      Long prodId = trdOrder.getProdId();
      Long groupId = trdOrder.getGroupId();
      List<ProductMakeUpInfo> productMakeUpInfos = financeProdInfoService.getFinanceProdMakeUpInfo
          (prodId, groupId);
      Long preOrderFundNumber = trdPayFlow.getFundSumConfirmed();
      logger.info("preOrderFundNumber : " + preOrderFundNumber);
      PayPreOrderDto payPreOrderDto = new PayPreOrderDto();
      payPreOrderDto.setOriginFundCode(trdPayFlow.getFundCode());
      payPreOrderDto.setTrdBrokerId(trdPayFlow.getTradeBrokeId().intValue());
      payPreOrderDto.setTrdAccount(trdPayFlow.getTradeAcco());
      payPreOrderDto.setUserProdId(trdPayFlow.getUserProdId());
      payPreOrderDto.setUserPid(userInfoService.getUserBankInfo(trdOrder.getUserId()).getUserPid());
      payPreOrderDto.setUserUuid(""+trdOrder.getUserId());
      List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails = new
          ArrayList<>();
      List<TrdOrderDetail> trdOrderDetailFromDb = trdOrderDetailRepository.findAllByOrderId
          (trdOrder.getOrderId());
      Map<String, TrdOrderDetail> trdOrderDetailMap = new HashMap<>();
      for(TrdOrderDetail trdOrderDetail: trdOrderDetailFromDb){
        trdOrderDetailMap.put(trdOrderDetail.getFundCode(), trdOrderDetail);
      }
      for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
        TrdOrderDetail trdOrderDetailOrigin = trdOrderDetailMap.get(productMakeUpInfo.getFundCode
            ());
        com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderDetail = new com
            .shellshellfish.aaas.common.message.order.TrdOrderDetail();
        trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.CONVERTWAITCONFIRM.getStatus());

        Long result = TradeUtil.getBigDecimalNumWithDiv10000(productMakeUpInfo.getFundShare())
            .multiply(BigDecimal.valueOf(trdPreOrders.get(0).getFundShareConfirmed())).longValue();
        logger.info("origin total fund share:"+ trdPreOrders.get(0).getFundShareConfirmed() + " multipul "
            + "with:" + productMakeUpInfo.getFundShare() +" and got result:"+result);
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
   * @param financeProdInfo
   * @param productMakeUpInfos
   * @param preOrderId
   * @return
   */
  private TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdInfo,
      List<ProductMakeUpInfo> productMakeUpInfos, long preOrderId) throws Exception {
    PayOrderDto payOrderDto = new PayOrderDto();
    Map brokerWithTradeAcco = getOrMakeTradeAcco(financeProdInfo);
    if(CollectionUtils.isEmpty(brokerWithTradeAcco)){
      logger.error("Failed to make trade account for user:"+ financeProdInfo.getUserId());
      throw new Exception("Failed to make trade account for user:"+ financeProdInfo.getUserId());
    }
    //默认用第一组数据， 因为现在只有一个交易平台
    int trdBrokerId = -1;
    String trdAcco = null;
    trdBrokerId = (int) brokerWithTradeAcco.keySet().toArray()[0];
    String trdAccoOrig = (String) brokerWithTradeAcco.get(Integer.valueOf(trdBrokerId));
    String items[] = trdAccoOrig.split("\\|");
    trdAcco = items[0];
    payOrderDto.setUserPid(items[1]);
    String orderId = TradeUtil.generateOrderIdByBankCardNum(financeProdInfo.getBankAcc(),trdBrokerId);
    payOrderDto.setTrdAccount(trdAcco);
    payOrderDto.setUserUuid(financeProdInfo.getUuid());
    payOrderDto.setUserProdId(financeProdInfo.getUserProdId());
    List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails =  new
        ArrayList<com.shellshellfish.aaas.common.message.order.TrdOrderDetail>();

    TrdOrder trdOrder = new TrdOrder();
    trdOrder.setBankCardNum(financeProdInfo.getBankAcc());
    trdOrder.setOrderDate(TradeUtil.getUTCTime());
    trdOrder.setCreateDate(TradeUtil.getUTCTime());
    trdOrder.setOrderType(TrdOrderOpTypeEnum.BUY.getOperation());
    trdOrder.setProdId(financeProdInfo.getProdId());
    trdOrder.setUserProdId(financeProdInfo.getUserProdId());
    trdOrder.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.ordinal());
    trdOrder.setOrderId(orderId);
    trdOrder.setUserId(financeProdInfo.getUserId());
    trdOrder.setPreOrderId(preOrderId);
    trdOrder.setCreateBy(financeProdInfo.getUserId());
    trdOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdInfo.getMoney()));
    trdOrderRepository.save(trdOrder);
    //generate sub order for each funds
    for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      trdOrderDetail.setOrderId(trdOrder.getOrderId());
      trdOrderDetail.setUserId(trdOrder.getUserId());
      //规定基金占比用百分比并且精确万分之一
      BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
          (BigDecimal.valueOf(10000));
      trdOrderDetail.setFundMoneyQuantity(fundRatio.multiply(financeProdInfo.getMoney())
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
      com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderPay =  new com
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
    if( financeProdInfo.getUserId() == null ){
      userId = getUserId(financeProdInfo.getUuid());
    }
    if(null == userId){
      throw new IllegalArgumentException("userUUID:" + financeProdInfo.getUuid() + " cannot be "
          + "found for correspond userId in system");
    }
    trdPreOrder.setUserId(userId);
    trdPreOrderRepository.save(trdPreOrder);
    UserBankInfo userBankInfo =  userInfoService.getUserBankInfo(userId);
    PreOrderPayReq.Builder poprBuilder = PreOrderPayReq.newBuilder();

    poprBuilder.setBankCardNum(financeProdInfo.getBankAcc());
    poprBuilder.setOrderType(TrdOrderOpTypeEnum.PREORDER.getOperation());
    poprBuilder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdInfo.getMoney()));
    poprBuilder.setProdId(financeProdInfo.getProdId());
    poprBuilder.setUserId(userId);
    poprBuilder.setFundCode(fundCode);
    poprBuilder.setUserPid(userBankInfo.getUserPid());
    PreOrderPayResult result = payService.preOrder2Pay(poprBuilder.build());
    if(StringUtils.isEmpty(result.getApplySerial())){
      //说明中证支付接口调用失败没有生产流水号
      logger.error("failed to preOrder for :" + fundCode + "with error:" + result.getErrMsg());
      trdPreOrderRepository.updateByParam(TrdOrderStatusEnum.FAILED.getStatus(),result.getErrMsg(),TradeUtil
          .getUTCTime(),userId, trdPreOrder.getId());
    }else{
      trdPreOrderRepository.updateByParam(TrdOrderStatusEnum.WAITPAY.getStatus(),result.getErrMsg(),
          TradeUtil.getUTCTime(),userId, trdPreOrder.getId());
    }
    return result;
  }

  private TrdOrder firstStepBuySpecialFunds(FinanceProdBuyInfo financeProdBuyInfo) throws Exception{
    //Todo: add code
    return null;
  }



}
