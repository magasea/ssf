package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdPayFlowStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceImplBase;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.pay.model.SellFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PayServiceImpl extends PayRpcServiceImplBase implements PayService {

  Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

  @Autowired
  BroadcastMessageProducers broadcastMessageProducers;

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Autowired
  TrdPayFlowRepository trdPayFlowRepository;




  @Override
  public PayDto payOrder(PayDto payDto) throws Exception {

    List<TrdOrderDetail> orderDetailList = payDto.getOrderDetailList();
    PayDto payDtoResult = null;
    List<TrdPayFlow> trdPayFlows =  trdPayFlowRepository.findAllByUserProdId(payDto.getUserProdId());
    if(CollectionUtils.isEmpty(trdPayFlows)){
      //说明是新的订单
      payDtoResult = payNewOrder(payDto);
    }else{
      //Todo
      //说明是重复请求
    }
    return payDtoResult;
  }

  private PayDto payNewOrder(PayDto payDto) throws Exception{
    List<Exception > errs = new ArrayList<>();
    String trdAcco = payDto.getTrdAccount();
    List<TrdOrderDetail> orderDetailList = payDto.getOrderDetailList();
    for(TrdOrderDetail trdOrderDetail: orderDetailList){
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());
      if(null == trdOrderDetail.getOrderDetailId()){
        logger.error("input pay request is not correct: OrderDetailId is:"+trdOrderDetail.getOrderDetailId());
        continue;
      }else{
        if(
        trdPayFlowRepository.findAllByOrderDetailId(trdOrderDetail.getOrderDetailId()).size() > 0){
          logger.error("repay request for :"+ trdOrderDetail.getOrderDetailId() + " we will "
              + "ignore it ");
          continue;
        }
      }
      trdOrderDetail.getOrderDetailId();
      //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundMoneyQuantity());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(SSFDateUtils.getCurrentDateInLong());
      trdPayFlow.setCreateBy(0L);

      trdPayFlow.setPayAmount(trdOrderDetail.getFundMoneyQuantity());
      trdPayFlow.setPayStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(payDto.getUserProdId());
      trdPayFlow.setOrderDetailId(trdOrderDetail.getOrderDetailId());
      trdPayFlow.setPayType(TrdOrderOpTypeEnum.BUY.getOperation());
      BuyFundResult fundResult = null;
      try {
        String userId4Pay = null;
        if(payDto.getUserUuid().equals("shellshellfish")){
          logger.info("use original uuid for pay because it is a test data");
          userId4Pay = "shellshellfish";
        }else{
          userId4Pay = String.valueOf(payDto.getOrderDetailList().get(0).getUserId());
        }
        fundResult = fundTradeApiService.buyFund(userId4Pay, trdAcco, payAmount,
            String.valueOf(trdOrderDetail.getId()),trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }
      //ToDo: 如果有真实数据， 则删除下面if代码
      if(null == fundResult){
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        trdPayFlowMsg.setPayStatus(TrdPayFlowStatusEnum.NOTHANDLED.getStatus());
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
        trdPayFlow.setPayStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setUserId(trdOrderDetail.getUserId());
        trdPayFlow.setTradeBrokeId(payDto.getTrdBrokerId());
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
    return payDto;
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
  public String bindCard(BindBankCard bindBankCard) {
    try {
      OpenAccountResult openAccountResult = fundTradeApiService.openAccount("" +bindBankCard
              .getUserId(), bindBankCard.getUserName(),bindBankCard.getCellphone(), bindBankCard
              .getUserPid(), bindBankCard.getBankCardNum(),
          bindBankCard.getBankCode());
      return openAccountResult.getTradeAcco();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
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
      trdPayFlow.setPayStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(prodSellDTO.getUserProdId());
      trdPayFlow.setOrderDetailId(prodDtlSellDTO.getOrderDetailId());
      SellFundResult sellFundResult = fundTradeApiService.sellFund(userUuid, sellNum,
          outsideOrderNo, tradeAcco, code);
      if(null != sellFundResult){
        trdPayFlow.setApplySerial(sellFundResult.getApplySerial());
        trdPayFlow.setPayStatus(TrdPayFlowStatusEnum.CONFIRMSUCCESS.getStatus());
        trdPayFlow.setPayType(TrdOrderOpTypeEnum.REDEEM.getOperation());
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
    ApplyResult applyResult = queryOrder(userId, orderDetailId);
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
  public ApplyResult queryOrder(Long userId, Long orderDetailId) {
    String tradeUserId = userId.toString();
    if(userId == 5605){//这个用户之前是用"shellshellfish来绑定中证账户的"
      tradeUserId = "shellshellfish";
    }
    ApplyResult applyResult = null;
    try {
      applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(tradeUserId, orderDetailId
          .toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return applyResult;

  }


  @Override
  public void bindBankCard(com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery bindBankCardQuery,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult> responseObserver){
    BindBankCard bindBankCard = new BindBankCard();
    BeanUtils.copyProperties(bindBankCardQuery, bindBankCard);
    String trdAcco = bindCard(bindBankCard);
    BindBankCardResult.Builder builder = BindBankCardResult.newBuilder();
    builder.setTradeacco(trdAcco);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
