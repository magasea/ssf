package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.common.enums.OrderJobPayRltEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.ZZKKStatusEnum;
import com.shellshellfish.aaas.common.enums.ZZRiskAbilityEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.MathUtil;
import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.ZZRiskToSSFRiskUtils;
import com.shellshellfish.aaas.common.utils.ZZStatsToOrdStatsUtils;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayResult;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayResultDetail;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceImplBase;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.FundConvertResult;
import com.shellshellfish.aaas.finance.trade.pay.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.pay.model.SellFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.UserBank;
import com.shellshellfish.aaas.finance.trade.pay.model.ZZBuyFund;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class PayServiceImpl extends PayRpcServiceImplBase implements PayService {

  Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

  @Autowired
  BroadcastMessageProducers broadcastMessageProducers;

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Autowired
  TrdPayFlowRepository trdPayFlowRepository;

  @Autowired
  UserInfoService userInfoService;

  @Autowired
  MultiThreadTaskHandler multiThreadTaskHandler;


  @Override
  public PayOrderDto payOrder(PayOrderDto payOrderDto) throws Exception {

    List<TrdOrderDetail> orderDetailList = payOrderDto.getOrderDetailList();
    PayOrderDto payOrderDtoResult = null;
    List<TrdPayFlow> trdPayFlows =  trdPayFlowRepository.findAllByUserProdId(payOrderDto.getUserProdId());
    if(CollectionUtils.isEmpty(trdPayFlows)){
      //说明是新的订单
      payOrderDtoResult = payNewOrder(payOrderDto);
    }else{
      //Todo
      //说明是重复请求
    }
    return payOrderDtoResult;
  }

  private com.shellshellfish.aaas.common.message.order.TrdPayFlow payNewOrderDetail(TrdOrderDetail trdOrderDetail, String trdAcco, Long
      userProdId, String userUUID, int trdBrokerId, String userPid ) throws Exception {
    List<Exception > errs = new ArrayList<>();
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());

//      trdOrderDetail.getOrderDetailId();
      //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundMoneyQuantity());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(SSFDateUtils.getCurrentDateInLong());
      trdPayFlow.setCreateBy(0L);

      trdPayFlow.setTrdMoneyAmount(trdOrderDetail.getFundMoneyQuantity());
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(userProdId);
      trdPayFlow.setOrderDetailId(trdOrderDetail.getId());
      //注意outsideOrderNo 用主订单的OrderID+子订单ID 来拼接
      trdPayFlow.setOutsideOrderno(trdOrderDetail.getOrderId()+""+trdOrderDetail.getId());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.BUY.getOperation());
      BuyFundResult fundResult = null;
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
        .shellshellfish.aaas.common.message.order.TrdPayFlow();
      try {
        String userId4Pay = TradeUtil.getZZOpenId(userPid);
//        if(!StringUtils.isEmpty(userUUID) && userUUID.equals("shellshellfish")){
//          logger.info("use original uuid for pay because it is a test data");
//          userId4Pay = "shellshellfish";
//        }else{
//          userId4Pay = String.valueOf(trdOrderDetail.getUserId());
//        }
        String outSideTradeNo = trdPayFlow.getOutsideOrderno();
        fundResult = fundTradeApiService.buyFund(userId4Pay, trdAcco, payAmount,
            outSideTradeNo,trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }
      //ToDo: 如果有真实数据， 则删除下面if代码
      if(null == fundResult){
        trdPayFlowMsg.setTrdStatus(TrdZZCheckStatusEnum.NOTHANDLED.getStatus());
        StringBuilder errMsg = new StringBuilder();
        for(Exception ex: errs){
          errMsg.append(ex.getMessage());
          errMsg.append("|");
        }
        trdPayFlowMsg.setErrMsg(errMsg.toString());
        trdPayFlowMsg.setUserId(trdOrderDetail.getUserId());
        trdPayFlowMsg.setOrderDetailId(trdOrderDetail.getId());
        notifyPay(trdPayFlowMsg);
        errs.clear();

      }
      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setTrdStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setUserId(trdOrderDetail.getUserId());
        trdPayFlow.setTradeBrokeId(trdBrokerId);
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
        notifyPay(trdPayFlowMsg);
      }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return trdPayFlowMsg;
  }

  private PayOrderDto payNewOrder(PayOrderDto payOrderDto) throws Exception{
    List<Exception > errs = new ArrayList<>();
    String trdAcco = payOrderDto.getTrdAccount();
    List<TrdOrderDetail> orderDetailList = payOrderDto.getOrderDetailList();
    StringBuilder sbOutsideOrderno = new StringBuilder();
    for(TrdOrderDetail trdOrderDetail: orderDetailList){
      sbOutsideOrderno.setLength(0);
      sbOutsideOrderno.append(trdOrderDetail.getOrderId()).append(trdOrderDetail.getId());
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());
      if(null == trdOrderDetail.getOrderId()){
        logger.error("input pay request is not correct: OrderId is:"+trdOrderDetail.getOrderId());
        sbOutsideOrderno.setLength(0);
        continue;
      }else{
        if(
          //我们目前是用主订单OrderId和子订单ID拼接成OutsideOrderno
          trdPayFlowRepository.findAllByOutsideOrderno(sbOutsideOrderno.toString()).size() > 0){
            logger.error("repay request for outsideOrderno:"+sbOutsideOrderno+" we will ignore it ");
          sbOutsideOrderno.setLength(0);
          continue;
        }
      }

      //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundMoneyQuantity());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(SSFDateUtils.getCurrentDateInLong());
      trdPayFlow.setCreateBy(0L);
      //重要。。。。。
      trdPayFlow.setOutsideOrderno(sbOutsideOrderno.toString());
      trdPayFlow.setTrdMoneyAmount(trdOrderDetail.getFundMoneyQuantity());
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(payOrderDto.getUserProdId());
      trdPayFlow.setOrderDetailId(trdOrderDetail.getId());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.BUY.getOperation());
      BuyFundResult fundResult = null;
      try {
        String userId4Pay = TradeUtil.getZZOpenId(payOrderDto.getUserPid());


        fundResult = fundTradeApiService.buyFund(userId4Pay, trdAcco, payAmount,
            sbOutsideOrderno.toString(),trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }
      //ToDo: 如果有真实数据， 则删除下面if代码
      if(null == fundResult){
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        trdPayFlowMsg.setTrdStatus(TrdZZCheckStatusEnum.NOTHANDLED.getStatus());
        StringBuilder errMsg = new StringBuilder();
        for(Exception ex: errs){
          errMsg.append(ex.getMessage());
          errMsg.append("|");
        }
        trdPayFlowMsg.setErrMsg(errMsg.toString());
        trdPayFlowMsg.setUserId(trdOrderDetail.getUserId());
        trdPayFlowMsg.setOrderDetailId(trdOrderDetail.getId());
        notifyPay(trdPayFlowMsg);
        errs.clear();

      }
      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setTrdStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setUserId(trdOrderDetail.getUserId());
        trdPayFlow.setTradeBrokeId(payOrderDto.getTrdBrokerId());
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
        notifyPay(trdPayFlowMsg);
      }

    }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return payOrderDto;
  }

  @Override
  public com.shellshellfish.aaas.common.message.order.TrdPayFlow notifyPay(com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlow) {
    logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());

    broadcastMessageProducers.sendMessage(trdPayFlow);
    return trdPayFlow;
  }

  @Override
  public com.shellshellfish.aaas.common.message.order.TrdPayFlow notifySell(
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlow) {
    return null;
  }

  @Override
  public String bindCard(BindBankCard bindBankCard)
      throws ExecutionException, InterruptedException, JsonProcessingException {
    String tradeAcco = null;
    try {
      OpenAccountResult openAccountResult = fundTradeApiService.openAccount("" +TradeUtil
              .getZZOpenId(bindBankCard.getUserPid()), bindBankCard.getUserName(),bindBankCard
                      .getCellphone(), bindBankCard.getUserPid(), bindBankCard.getBankCardNum(),
          bindBankCard.getBankCode());
      tradeAcco =  openAccountResult.getTradeAcco();
    } catch (Exception e) {
      e.printStackTrace();
      if(e.getMessage().contains("该商户下已有其他openid绑定该身份证") || e.getMessage().contains("此卡已存在")){
        //尝试用当前的userId去获取tradeAccount
        try {
          List<UserBank> userBanks =  fundTradeApiService.getUserBank(""+ TradeUtil.getZZOpenId(bindBankCard
                  .getUserPid()),"001120");
          if(CollectionUtils.isEmpty(userBanks)){
            logger.error("failed to find userBanks by userId:"+ bindBankCard.getUserId() + " and "
                + "fundCode: 001120");
          }else{
            for(UserBank userBank: userBanks){
              if(bindBankCard.getBankCardNum().equals(userBank.getBankAcco())){
                logger.info("found tradeAcco by userId:" + bindBankCard.getUserId() + " "
                    + "tradeAcco:" + userBank.getTradeAcco());
                tradeAcco =  userBank.getTradeAcco();
              }
            }
          }
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    }
    //风险评估再设置一下
//    UserInfo userInfo = userInfoService.getUserInfoByUserId(bindBankCard.getUserId());
    ZZRiskAbilityEnum zzRiskAbilityEnum = ZZRiskToSSFRiskUtils.getZZRiskAbilityFromSSFRisk(
        UserRiskLevelEnum.get(bindBankCard.getRiskLevel()));
    logger.info("now set the user:"+ bindBankCard.getUserPid() + " risk level:" +
        zzRiskAbilityEnum.getRiskLevel());
    fundTradeApiService.commitRisk(TradeUtil.getZZOpenId(bindBankCard.getUserPid()),
        zzRiskAbilityEnum.getRiskLevel());
    return tradeAcco;
  }

  @Override
  public boolean sellProd(ProdSellDTO prodSellDTO) throws Exception {
    logger.info("sell prod with: userId:" + prodSellDTO.getUserId()+ "" + prodSellDTO.getUserUuid
        ()+ prodSellDTO.getTrdAcco());
    String userUuid = prodSellDTO.getUserUuid();
    if(CollectionUtils.isEmpty(prodSellDTO.getProdDtlSellDTOList())){
      logger.error("empty sellProd list");
      return false;
    }

    for(ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
      int sellNum = prodDtlSellDTO.getFundQuantity();
      String code = prodDtlSellDTO.getFundCode();
      String tradeAcco = prodSellDTO.getTrdAcco();
      String outsideOrderNo = Long.toString(prodDtlSellDTO.getOrderDetailId());

      logger.info("sell prod with fundCode :"+code
          +"sell fund quantity:"+ sellNum + " sell  account:"+ tradeAcco + " outsideOrderNo:" +
          outsideOrderNo);
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(0L);
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(prodSellDTO.getUserProdId());
      trdPayFlow.setOrderDetailId(prodDtlSellDTO.getOrderDetailId());
      SellFundResult sellFundResult = fundTradeApiService.sellFund(userUuid, sellNum,
          outsideOrderNo, tradeAcco, code);
      if(null != sellFundResult){
        trdPayFlow.setApplySerial(sellFundResult.getApplySerial());
        trdPayFlow.setTrdStatus(TrdZZCheckStatusEnum.CONFIRMSUCCESS.getStatus());
        trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(prodDtlSellDTO.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(prodSellDTO.getUserId());
        trdPayFlow.setUpdateBy(prodSellDTO.getUserId());
        trdPayFlow.setTradeAcco(prodSellDTO.getTrdAcco());
        trdPayFlow.setUserProdId(prodSellDTO.getUserProdId());
        trdPayFlow.setUserId(prodSellDTO.getUserId());
        trdPayFlow.setTradeBrokeId(prodSellDTO.getTrdBrokerId());
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
        notifyPay(trdPayFlowMsg);
      }
    }

//    fundTradeApiService.sellFund(userUuid, sellNum, outsideOrderNo, tradeAcco, fundCode);
    return false;
  }

  /**
   */
  @Override
  public void queryZhongzhengTradeInfoBySerial(com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryBySerial request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.ApplyResult> responseObserver) {
    String applySerial = request.getApplySerial();
    Long userId = request.getUserId();
    ApplyResult applyResult = queryOrder(userId, applySerial);
    if(applyResult == null){
      logger.error("failed to queryZhongzhengTradeInfoBySerial by applySerial:" + applySerial +" "
          + "userId:"+  userId);
      applyResult = new ApplyResult();
    }
    com.shellshellfish.aaas.finance.trade.pay.ApplyResult.Builder resultBuilder = com
        .shellshellfish.aaas.finance.trade.pay.ApplyResult.newBuilder();
    BeanUtils.copyProperties(applyResult, resultBuilder);
    responseObserver.onNext(resultBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void queryZhongzhengTradeInfoByOrderDetailId(com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryByOrderDetailId request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.ApplyResult> responseObserver) {
    Long orderDetailId = request.getOrderDetailId();
    Long userId = request.getUserId();
    ApplyResult applyResult = null;
    try {
      applyResult = queryOrder(userId, orderDetailId);
    } catch (ExecutionException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    if(applyResult == null){
      logger.error("failed to queryZhongzhengTradeInfoBySerial by orderDetailId:" + orderDetailId
          + "userId:"+  userId);
      applyResult = new ApplyResult();
    }
    com.shellshellfish.aaas.finance.trade.pay.ApplyResult.Builder resultBuilder = com
        .shellshellfish.aaas.finance.trade.pay.ApplyResult.newBuilder();
    BeanUtils.copyProperties(applyResult, resultBuilder);
    responseObserver.onNext(resultBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public ApplyResult queryOrder(Long userId, String applySerial) {
    String tradeUserId = userId.toString();
    if(userId == 5605){//这个用户之前是用"shellshellfish来绑定中证账户的"
      tradeUserId = "shellshellfish";
    }
    ApplyResult applyResult = null;
    try {
      applyResult = fundTradeApiService.getApplyResultByApplySerial(tradeUserId, applySerial);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return applyResult;
  }

  @Override
  public ApplyResult queryOrder(Long userId, Long orderDetailId)
      throws ExecutionException, InterruptedException {
    String tradeUserId = userId.toString();
    //ToDo: 怎么知道订单是用哪个银行卡买的
    UserBankInfo userBankInfo =  userInfoService.getUserBankInfo(userId);
    String userPid = userBankInfo.getCardNumbersList().get(0).getUserPid();
    String openId = TradeUtil.getZZOpenId(userPid);

    ApplyResult applyResult = null;
    try {
      applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(openId, orderDetailId
          .toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return applyResult;

  }




  @Override
  public void bindBankCard(com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery bindBankCardQuery,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult> responseObserver) {
    BindBankCard bindBankCard = new BindBankCard();
    BeanUtils.copyProperties(bindBankCardQuery, bindBankCard);
    String trdAcco = null;
    try {
      trdAcco = bindCard(bindBankCard);
    } catch (ExecutionException e) {
      e.printStackTrace();
      logger.error(e.getMessage());

    } catch (InterruptedException e) {
      e.printStackTrace();

      logger.error(e.getMessage());
    } catch (JsonProcessingException e) {
      e.printStackTrace();

      logger.error(e.getMessage());
    }
    if(StringUtils.isEmpty(trdAcco) || trdAcco.equals("-1")){
      logger.error("failed to bind card with UserName:"+ bindBankCard.getUserName() + " pid:"+
          bindBankCard.getUserPid() + "bankCode:"+ bindBankCard.getBankCode() +
          "userId:" +bindBankCard.getUserId());
    }
    BindBankCardResult.Builder builder = BindBankCardResult.newBuilder();
    builder.setTradeacco(trdAcco);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override
  /**
   * <pre>
   **
   * 订单状态是待支付，所以调用pay模块，1. 检查是否已经支付(看子订单对应有没有apply_serial)
   * 2. 检查交易状态
   * 如果交易1.不满足 就重新发起支付，如果交易失败而且需要交易的基金数量大于0，那么再次发起交易
   * </pre>
   */
  public void orderJob2Pay(com.shellshellfish.aaas.finance.trade.pay.OrderPayReq request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.OrderPayResult> responseObserver) {
    PayOrderDto payOrderDto = new PayOrderDto();
    BeanUtils.copyProperties(request, payOrderDto);
    List<com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq> orderDetailPayReqs = request
    .getOrderDetailPayReqList();
    OrderPayResult.Builder resultBdr = OrderPayResult.newBuilder();
    List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
    if(CollectionUtils.isEmpty(orderDetailPayReqs)){
      logger.error("the input orderDetailPay list is empty: for userProdId" + payOrderDto.getUserProdId
          ());
      resultBdr.setResult(OrderJobPayRltEnum.FAILED.ordinal());
      responseObserver.onNext(resultBdr.build());
      responseObserver.onCompleted();
      return;
    }
    for(OrderDetailPayReq orderDetailPayReq: orderDetailPayReqs){
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      BeanUtils.copyProperties(orderDetailPayReq, trdOrderDetail);
      trdOrderDetails.add(trdOrderDetail);
    }
    payOrderDto.setOrderDetailList(trdOrderDetails);
    try{
      payOrderDto.setUserPid(request.getUserPid());
      payOrderByJob(payOrderDto);
    }catch (Exception ex){
      ex.printStackTrace();
      logger.error("got error when payOrderByJob" + ex.getMessage());
      resultBdr.setResult(OrderJobPayRltEnum.FAILED.ordinal());
      responseObserver.onNext(resultBdr.build());
      responseObserver.onCompleted();
    }
    resultBdr.setResult(OrderJobPayRltEnum.SUCCUSS.ordinal());
    responseObserver.onNext(resultBdr.build());
    responseObserver.onCompleted();
  }

  @Override
  public PayOrderDto payOrderByJob(PayOrderDto payOrderDto) throws Exception {
    /**
     * 比较需要支付的trdOrderDetailId 和payflow里面的trdOrderDetailId
     *  如果有，那么看状态是否是支付失败，支付失败的话再次发起支付
     *  否则就直接将payflow msg的消息生成出来走消息通道发出去让 order; orderDetail 和 uiProd uiProdDetail
     *  的状态更新
     */

    List<Exception > errs = new ArrayList<>();
    String trdAcco = payOrderDto.getTrdAccount();
    Long userProdId = payOrderDto.getUserProdId();
    String userUUID = payOrderDto.getUserUuid();
    int trdBrokerId = payOrderDto.getTrdBrokerId();

    List<TrdOrderDetail> orderDetailList = payOrderDto.getOrderDetailList();
    logger.info("总共有:" + orderDetailList.size() + " 个orderDetail需要处理");
    for(TrdOrderDetail trdOrderDetail: orderDetailList){
      logger.info("开始处理 trdOrderDetail with orderId:" + trdOrderDetail.getOrderId() + " "
          + "orderDetailId:" + trdOrderDetail.getId());
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());

      if(null == trdOrderDetail.getOrderId() || trdOrderDetail.getId() <= 0){
        logger.error("input pay request is not correct: OrderId is:"+trdOrderDetail.getOrderId()
            + " OrderDetailId is:" + trdOrderDetail.getId());
        continue;
      }else{
        String outsideOrderno = trdOrderDetail.getOrderId()+ trdOrderDetail.getId();
        List<TrdPayFlow> trdPayFlows =  trdPayFlowRepository.findAllByOutsideOrderno(outsideOrderno);
        if(!CollectionUtils.isEmpty(trdPayFlows) &&
            trdPayFlows.size() > 0){
          logger.error("repay request for outsideOrderno:" + outsideOrderno);
          //开始处理已经尝试过的payflow
          if(trdPayFlows.size() >=2){
            logger.error("this trdOrderDetail with outsideOrderNo"+outsideOrderno +" is payed more"
                + " than 2 times, need check logic");
            //准备去取支付成功的applyserial,如果没有，就取最后一次applySerial
            String applySerial = null;
            TrdPayFlow trdPayFlowRetry = null;
            for(TrdPayFlow trdPayFlow: trdPayFlows){
              if(!StringUtils.isEmpty(trdPayFlow.getApplySerial())){
                if(null == applySerial){
                  applySerial = trdPayFlow.getApplySerial();
                }else{
                  if(applySerial.compareToIgnoreCase(trdPayFlow.getApplySerial()) < 0){
                    //发现新的applySerial
                    //状态为不成功， 可以重试， 挑选applySerial最大的那个重试
                    applySerial = trdPayFlow.getApplySerial();
                    if(trdPayFlow.getTrdbkerStatusCode() != TrdZZCheckStatusEnum.CONFIRMSUCCESS.getStatus() ){
                      trdPayFlowRetry = trdPayFlow;
                    }
                  }
                }
                if(trdPayFlow.getTrdbkerStatusCode() == TrdZZCheckStatusEnum.CONFIRMSUCCESS
                    .getStatus() ){
                  //状态为成功， 应该是已经交易过，但是order 或者 ui prod系统没有更新应该发消息队列让对应模块去更新
                  logger.error("trdPayFlow with outsideOrderno:"+ outsideOrderno+"状态为成功， "
                      + "应该是已经交易过，但是order 或者 ui prod系统没有更新应该发消息队列让对应模块去更新");
                  com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
                      .shellshellfish.aaas.common.message.order.TrdPayFlow();
                  BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
                  notifyPay(trdPayFlowMsg);
                  //重发notify后退出这个orderDetailId的处理
                  break;
                }
              }
            }
          }
        }//已经支付过的payFlow
        else if(CollectionUtils.isEmpty(trdPayFlows) ||
            trdPayFlows.size() <= 0){
          //说明该orderDetailId是需要当成一个新的交易来处理
          logger.error("该orderDetailId是需要当成一个新的交易来处理, 导致这个问题的原因待查:"
              + "消息抖动？ 中证接口异常？ 程序异常？ ");

        }
      }
      payNewOrderDetail(trdOrderDetail, trdAcco, userProdId, userUUID, trdBrokerId, payOrderDto.getUserPid());
    }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return payOrderDto;
  }

  /**
   * <pre>
   **
   * 老板要求直接生成购买货币基金的预订单包含调中证接口
   * </pre>
   */
  @Override
  public void preOrder2Pay(com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult> responseObserver) {
    PreOrderPayResult preOrderPayResult = preOrder2Pay(request);
    responseObserver.onNext(preOrderPayResult);
    responseObserver.onCompleted();
  }


  @Override
  public PreOrderPayResult preOrder2Pay(PreOrderPayReq request) {
    PreOrderPayResult.Builder poprBuilder = PreOrderPayResult.newBuilder();
    poprBuilder.setPreOrderId(request.getUserId());

    BuyFundResult buyFundResult;
    try {
        buyFundResult = fundTradeApiService.buyFund(TradeUtil.getZZOpenId(request.getUserPid()), request
                .getTradeAccount()
          , TradeUtil.getBigDecimalNumWithDiv100(request.getPayAmount()), ""+request
              .getPreOrderId(), request.getFundCode());
      poprBuilder.setApplySerial(buyFundResult.getApplySerial());
      //把交易流水入库
      int kkStat = Integer.parseInt(buyFundResult.getKkstat());
      String kkStatName = ZZKKStatusEnum.getByStatus(kkStat).getComment();
      logger.info("preOrderId:"+ request.getPreOrderId() +" kkStat:"+ kkStat + " "
          + "kkStatName:" + kkStatName);
      TrdOrderStatusEnum trdOrderStatusEnum = ZZStatsToOrdStatsUtils.getOrdStatByZZKKStatus(ZZKKStatusEnum
          .getByStatus((kkStat)), TrdOrderOpTypeEnum.BUY);

      com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow trdPayFlow = new com
          .shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow();
//      BeanUtils.copyProperties(request.getTrdOrderDetail(), trdPayFlow);
      trdPayFlow.setUpdateBy(request.getUserId());
      trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.PREORDER.getOperation());
      trdPayFlow.setTrdDate(TradeUtil.getUTCTime());
      trdPayFlow.setUserId(request.getUserId());
      trdPayFlow.setApplySerial(buyFundResult.getApplySerial());
      trdPayFlow.setCreateBy(request.getUserId());
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
//      trdPayFlow.setUserProdId(request.getTrdOrderDetail().getUserProdId());
      trdPayFlow.setTradeAcco(request.getTradeAccount());
      trdPayFlow.setFundCode(request.getFundCode());
      trdPayFlow.setOrderDetailId(request.getPreOrderId());
      trdPayFlow.setBankCardNum(request.getBankCardNum());
      trdPayFlow.setOutsideOrderno(buyFundResult.getOutsideOrderNo());
      trdPayFlow.setTrdbkerStatusName(kkStatName);
      trdPayFlow.setTrdbkerStatusCode(kkStat);
      trdPayFlow.setTradeBrokeId(request.getTrdBrokerId());
      //注意外面接口用BigDecimal表示金额，入库都用long精确到分
      trdPayFlow.setTrdMoneyAmount(request.getPayAmount());
      trdPayFlow.setTrdStatus(trdOrderStatusEnum.getStatus());
      trdPayFlowRepository.save(trdPayFlow);
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
          .shellshellfish.aaas.common.message.order.TrdPayFlow();
      BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
//      notifyPayMsg( trdPayFlowMsg);
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      poprBuilder.setErrMsg(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      poprBuilder.setErrMsg(e.getMessage());
    }
    return poprBuilder.build();

  }

  @Override
  public PayPreOrderDto payPreOrder(PayPreOrderDto payPreOrderDto) throws Exception {
    List<Exception > errs = new ArrayList<>();
    String trdAcco = payPreOrderDto.getTrdAccount();
    List<TrdOrderDetail> orderDetailList = payPreOrderDto.getOrderDetailList();
    for(TrdOrderDetail trdOrderDetail: orderDetailList){
      String outsideOrderno = trdOrderDetail.getOrderId()+trdOrderDetail.getId();
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());
      if(null == trdOrderDetail.getOrderId() || trdOrderDetail.getId() <= 0){
        logger.error("input pay request is not correct: orderId is:"+ trdOrderDetail.getOrderId
            () + " orderDetailId:" + trdOrderDetail.getId());
        continue;
      }else{

        if(
            trdPayFlowRepository.findAllByOutsideOrderno(outsideOrderno).size() > 0){
          logger.error("repay request for outsideOrderno:"+ outsideOrderno);
          continue;
        }
      }

      //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundMoneyQuantity());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(0L);

      trdPayFlow.setTrdMoneyAmount(trdOrderDetail.getFundMoneyQuantity());
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.CONVERTWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(payPreOrderDto.getUserProdId());
      trdPayFlow.setOrderDetailId(trdOrderDetail.getId());
      trdPayFlow.setOutsideOrderno(outsideOrderno);
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.FUNDCONVERT.getOperation());
      FundConvertResult fundResult = null;
      try {
        String userId4Pay = TradeUtil.getZZOpenId(payPreOrderDto.getUserPid());
//        if(payPreOrderDto.getUserUuid().equals("shellshellfish")){
//          logger.info("use original uuid for pay because it is a test data");
//          userId4Pay = "shellshellfish";
//        }else{
//          userId4Pay = String.valueOf(payPreOrderDto.getUserUuid());
//        }
        fundResult = fundTradeApiService.fundConvert(userId4Pay , TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail
                .getFundNum()), ""+ trdOrderDetail.getId(), trdAcco,payPreOrderDto.getOriginFundCode(), trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }

      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.CONVERTWAITCONFIRM.getStatus());
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setUserId(trdOrderDetail.getUserId());
        trdPayFlow.setTradeBrokeId(payPreOrderDto.getTrdBrokerId());
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
        notifyPay(trdPayFlowMsg);
      }
    }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return payPreOrderDto;
  }


  /**
   * <pre>
   **
   * 老板要求直接生成订单包含调中证接口
   * </pre>
   */
  @Override
  public void order2Pay(com.shellshellfish.aaas.finance.trade.pay.OrderPayReq request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.OrderPayResult> responseObserver) {

    List<ZZBuyFund> zzBuyFunds = new ArrayList<>();
    for(OrderDetailPayReq orderDetailPayReq: request.getOrderDetailPayReqList()) {
      ZZBuyFund zzBuyFund = new ZZBuyFund();
      zzBuyFund.setTradeAcco(request.getTrdAccount());
      zzBuyFund.setUserId(request.getUserId());
      zzBuyFund.setUserPid(request.getUserPid());
      zzBuyFund.setApplySum(TradeUtil.getBigDecimalNumWithDiv100(orderDetailPayReq.getPayAmount()));
      zzBuyFund.setFundCode(orderDetailPayReq.getFundCode());
      zzBuyFund.setOutsideOrderNo(""+orderDetailPayReq.getId());
      zzBuyFunds.add(zzBuyFund);
    }
    OrderPayResult.Builder resultBuilder = OrderPayResult.newBuilder();
    List<TrdOrderDetail> trdOrderDetails = multiThreadTaskHandler.processBuyOrders(zzBuyFunds);
    boolean tradeSuccess = true;
    for(OrderDetailPayReq orderDetailPayReq: request.getOrderDetailPayReqList()) {
      if(orderDetailPayReq.getOrderDetailStatus() == TrdOrderStatusEnum.FAILED.getStatus()){
        //只要一个交易失败，就把这个订单的交易作为失败
        resultBuilder.setResult(-1);
        tradeSuccess = false;
        break;
      }else{
        OrderPayResultDetail.Builder orderDetailRltBuilder = OrderPayResultDetail.newBuilder();
        BeanUtils.copyProperties(orderDetailPayReq, orderDetailRltBuilder);
        resultBuilder.addOrderPayResultDetail(orderDetailRltBuilder);
      }
    }
    if(tradeSuccess){
      resultBuilder.setResult(1);
    }
    responseObserver.onNext(resultBuilder.build());
    responseObserver.onCompleted();
  }
}
