package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.OrderJobPayRltEnum;
import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.ZZKKStatusEnum;
import com.shellshellfish.aaas.common.enums.ZZRiskAbilityEnum;
import com.shellshellfish.aaas.common.exceptions.ErrorConstants;
import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellPercentMsg;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.ZZRiskToSSFRiskUtils;
import com.shellshellfish.aaas.common.utils.ZZStatsToOrdStatsUtils;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfos;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayResult;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayResultDetail;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceImplBase;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.FundConvertResult;
import com.shellshellfish.aaas.finance.trade.pay.model.FundNetZZInfo;
import com.shellshellfish.aaas.finance.trade.pay.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.pay.model.SellFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.UserBank;
import com.shellshellfish.aaas.finance.trade.pay.model.ZZBuyFund;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mongo.MongoFundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.mysql.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.DataCollectionService;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.service.UserInfoService;
import com.shellshellfish.aaas.finance.trade.pay.service.impl.CheckFundsTradeJobService.MyEntry;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.grpc.common.PayFlowResult;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import net.bytebuddy.description.field.FieldDescription.InGenericShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

  @Autowired
  DataCollectionService dataCollectionService;

  @Autowired
  CheckFundsTradeJobService checkFundsTradeJobService;

  @Autowired
  MongoTemplate mongoPayTemplate;

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
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100
          (trdOrderDetail.getFundSum());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(0L);

      trdPayFlow.setTradeTargetSum(trdOrderDetail.getFundSum());
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
        logger.error("exception:",ex);

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
      logger.error("meet errors in pay api services");
      throw new Exception("meet errors in pay api services");
    }
    return trdPayFlowMsg;
  }

  private PayOrderDto payNewOrder(PayOrderDto payOrderDto) throws Exception{
    List<Exception > errs = new ArrayList<>();
    String trdAcco = payOrderDto.getTrdAccount();
    String userId4Pay = TradeUtil.getZZOpenId(payOrderDto.getUserPid());
    //此处因为产品设计丑陋所以只好在每次购买的时候设置风险测评值
    fundTradeApiService.commitRisk(userId4Pay, payOrderDto.getRiskLevel());
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
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundSum());
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
      //重要。。。。。
      trdPayFlow.setOutsideOrderno(sbOutsideOrderno.toString());
      trdPayFlow.setTradeTargetSum(trdOrderDetail.getFundSum());
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(payOrderDto.getUserProdId());
      trdPayFlow.setOrderDetailId(trdOrderDetail.getId());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.BUY.getOperation());
      BuyFundResult fundResult = null;
      ApplyResult applyResult = null;
      try {
        fundResult = fundTradeApiService.buyFund(userId4Pay, trdAcco, payAmount,
            sbOutsideOrderno.toString(),trdOrderDetail.getFundCode());
      }catch (Exception ex){
        logger.error("exception:",ex);

        errs.add(ex);
        if(ex.getMessage().contains("网络错误")){
          //try it again
          try {
            fundResult = fundTradeApiService.buyFund(userId4Pay, trdAcco, payAmount,
                sbOutsideOrderno.toString(), trdOrderDetail.getFundCode());
          }catch(Exception exagain){

            logger.error("Exception:",exagain);
            if(exagain.getMessage().contains("1013")||exagain.getMessage().contains("订单号重复")){
              logger.info("because it is a retry, so maybe the order is already available for "
                  + "next query");
              try {
                applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo
                    (userId4Pay, sbOutsideOrderno.toString());
              }catch (Exception exagainagain){
                logger.error("Exception:", exagainagain);
              }
            }
            errs.add(exagain);
          }
        }

      }

      if(null == fundResult && null == applyResult ){
        logger.error("failed to pay for:" + payOrderDto.getUserPid() + " with prodId:" +
            payOrderDto.getUserProdId() + " with TrdMoneyAmount" + payAmount + " fundCode:"+
            trdOrderDetail.getFundCode());
        StringBuilder sb = new StringBuilder();
        for(Exception ex: errs){
          sb.append(ex.getMessage());
        }
        trdPayFlow.setErrMsg(sb.toString());
        trdPayFlow.setTradeTargetSum(trdOrderDetail.getFundSum());
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setOutsideOrderno(trdOrderDetail.getOrderId()+trdOrderDetail.getId());
        trdPayFlow.setTradeBrokeId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.FAILED.getStatus());
        notifyPendingRecordsFailed(trdPayFlow);
        continue;
      }
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
          .shellshellfish.aaas.common.message.order.TrdPayFlow();
      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setTrdStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        trdPayFlowMsg.getTrdPayFlowExt().setConfirmDateExpected(fundResult.getConfirmdate());
      }else if(null != applyResult){
        trdPayFlow.setApplySerial(applyResult.getApplyserial());
        // to avoid unnecessary error for this kind of applyResult, make the status in a init status
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.WAITPAY.getStatus());
      }
      if( null != fundResult || null != applyResult){
        trdPayFlow.setTradeTargetSum(trdOrderDetail.getFundSum());
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setUserProdId(trdOrderDetail.getUserProdId());
        trdPayFlow.setUserId(trdOrderDetail.getUserId());
        trdPayFlow.setTradeBrokeId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
        trdPayFlow.setTrdApplyDate("-1"); // default value
        trdPayFlow.setApplydateUnitvalue(-1);
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);

        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);

        notifyPay(trdPayFlowMsg);
      }
    }
    if(errs.size() > 0){
      logger.error("meet errors in pay api services");
      throw new Exception("meet errors in pay api services");
    }
    return payOrderDto;
  }

  private void notifyPendingRecordsFailed(TrdPayFlow trdPayFlow) {
    logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());
    com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
        .shellshellfish.aaas.common.message.order.TrdPayFlow();
    MyBeanUtils.mapEntityIntoDTO(trdPayFlow, trdPayFlowMsg);
//    trdPayFlowMsg.setTrdStatus(TrdOrderStatusEnum.FAILED.getStatus());
    broadcastMessageProducers.sendFailedMsgToPendingRecord(trdPayFlowMsg);
    broadcastMessageProducers.sendFailedMsgToOrderDetail(trdPayFlowMsg);
    broadcastMessageProducers.sendFailedMsgToTrdLog(trdPayFlowMsg);

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
    broadcastMessageProducers.sendSellMessage(trdPayFlow);
    return trdPayFlow;
  }

  @Override
  public String bindCard(BindBankCard bindBankCard) throws Exception {
    String tradeAcco = null;
    try {
      OpenAccountResult openAccountResult = fundTradeApiService.openAccount("" +TradeUtil
              .getZZOpenId(bindBankCard.getUserPid()), bindBankCard.getUserName(),bindBankCard
                      .getCellphone(), bindBankCard.getUserPid(), bindBankCard.getBankCardNum(),
          bindBankCard.getBankCode());
      tradeAcco =  openAccountResult.getTradeAcco();
    } catch (Exception e) {
      boolean canRetry = false;
      logger.error("exception:",e);
      if(!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains(":")){
        if(e.getMessage().split("\\:")[0].equals("1009")){
          canRetry = true;
        }
      }
      if(canRetry){
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
          logger.error(e1.getMessage(), e1);
          e1.printStackTrace();
          throw e1;
        }
      }else{
        throw e;
      }
    }
    //风险评估再设置一下
//    UserInfo userInfo = userInfoService.getUserInfoByUserId(bindBankCard.getUserId());
    if(bindBankCard.getRiskLevel() > 0){
      ZZRiskAbilityEnum zzRiskAbilityEnum = ZZRiskToSSFRiskUtils.getZZRiskAbilityFromSSFRisk(
          UserRiskLevelEnum.get(bindBankCard.getRiskLevel()));
      logger.info("now set the user:"+ bindBankCard.getUserPid() + " risk level:" +
          zzRiskAbilityEnum.getRiskLevel());
      fundTradeApiService.commitRisk(TradeUtil.getZZOpenId(bindBankCard.getUserPid()),
          zzRiskAbilityEnum.getRiskLevel());
    }
    return tradeAcco;
  }

  @Override
  public boolean sellProd(ProdSellDTO prodSellDTO) throws Exception {
    logger.info("sell prod with: userId:" + prodSellDTO.getUserId()+ "" + prodSellDTO.getUserUuid
        ()+ prodSellDTO.getTrdAcco());
    String openId = TradeUtil.getZZOpenId(prodSellDTO.getUserPid());
    String tradeAcco = prodSellDTO.getTrdAcco();
    if(CollectionUtils.isEmpty(prodSellDTO.getProdDtlSellDTOList())){
      logger.error("empty sellProd list");
      return false;
    }
    for(ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
      int sellNum = prodDtlSellDTO.getFundQuantity();
      String fundCode = prodDtlSellDTO.getFundCode();
      String outsideOrderNo = prodSellDTO.getOrderId()+prodDtlSellDTO.getOrderDetailId();
      logger.info("sell prod with fundCode :"+fundCode
          +"sell fund quantity:"+ sellNum + " sell  account:"+ tradeAcco + " outsideOrderNo:" +
          outsideOrderNo);
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(prodSellDTO.getUserId());
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(prodSellDTO.getUserProdId());
      trdPayFlow.setOrderDetailId(prodDtlSellDTO.getOrderDetailId());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
      BigDecimal sellAmount = BigDecimal.valueOf(0);
      if(MonetaryFundEnum.containsCode(fundCode)){
        //如果是货币基金 ， 就直接用
        sellAmount = prodDtlSellDTO.getTargetSellAmount();
      }else {
        sellAmount = TradeUtil.getBigDecimalNumWithDiv100(Long.valueOf(sellNum));
      }
      try{
        SellFundResult sellFundResult = fundTradeApiService.sellFund(openId, sellAmount,
            outsideOrderNo, tradeAcco, fundCode);
        if(null != sellFundResult){
          trdPayFlow.setApplySerial(sellFundResult.getApplySerial());
          trdPayFlow.setTrdStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
          trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
          trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
          trdPayFlow.setTradeTargetShare(prodDtlSellDTO.getFundQuantity());
          trdPayFlow.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
              .getTargetSellAmount()));
          trdPayFlow.setFundCode(prodDtlSellDTO.getFundCode());
          trdPayFlow.setOutsideOrderno(outsideOrderNo);
          trdPayFlow.setOrderDetailId(prodDtlSellDTO.getOrderDetailId());
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
          notifySell(trdPayFlowMsg);
        }else{
          //赎回请求失败，需要把扣减的基金数量加回去
          notifyRollback(trdPayFlow, prodDtlSellDTO, sellNum);
        }
      }catch (Exception ex){
        logger.error("exception:",ex);

        logger.error("because of error:" + ex.getMessage() + " we need send out rollback notification");
        //赎回请求失败，需要把扣减的基金数量加回去
        if(!StringUtils.isEmpty(ex.getMessage())){
          if(ex.getMessage().split(":").length >= 2){
            trdPayFlow.setErrCode(ex.getMessage().split(":")[0]);
            trdPayFlow.setErrCode(ex.getMessage().split(":")[1]);
          }else{
            logger.error("strange err message from ZZ:"+ ex.getMessage());
          }
        }
        notifyRollback(trdPayFlow, prodDtlSellDTO, sellNum);
      }
    }
//    fundTradeApiService.sellFund(userUuid, sellNum, outsideOrderNo, tradeAcco, fundCode);
    return false;
  }



  private void notifyRollback(TrdPayFlow trdPayFlow, ProdDtlSellDTO prodDtlSellDTO, int sellNum){
    com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
        .shellshellfish.aaas.common.message.order.TrdPayFlow();
    trdPayFlow.setTrdStatus(TrdOrderStatusEnum.REDEEMFAILED.getStatus());
    trdPayFlow.setTradeTargetShare(sellNum);
    trdPayFlow.setUserProdId(prodDtlSellDTO.getUserProdId());
    trdPayFlow.setFundCode(prodDtlSellDTO.getFundCode());
    trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
    MyBeanUtils.mapEntityIntoDTO(trdPayFlow, trdPayFlowMsg);
    notifySell(trdPayFlowMsg);
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
      logger.error("exception:",e);
      logger.error(e.getMessage());
    } catch (InterruptedException e) {
      logger.error("exception:",e);
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
      logger.error("exception:",e);
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
      logger.error("exception:",e);
      logger.error(e.getMessage());
    }
    return applyResult;

  }


	@Override
	public void bindBankCard(com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery bindBankCardQuery,
							 io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult> responseObserver) {
		final String errMsg = "-1";

		BindBankCard bindBankCard = new BindBankCard();
		BeanUtils.copyProperties(bindBankCardQuery, bindBankCard);
		String trdAcco = null;
    BindBankCardResult.Builder builder = BindBankCardResult.newBuilder();
		try {
			trdAcco = bindCard(bindBankCard);
		} catch (JsonProcessingException e) {
			trdAcco = errMsg;
			logger.error(e.getMessage());
		} catch (Exception e) {
      logger.error("exception:",e);
      ErrInfo.Builder eiBuilder = ErrInfo.newBuilder();
      eiBuilder.setErrMsg(e.getMessage());
      eiBuilder.setErrCode(ErrorConstants.GRPC_ERROR_UI_BINDCARD_FAIL_GENERAL);
      builder.setErrInfo(eiBuilder.build());
    }
    if (StringUtils.isEmpty(trdAcco) || trdAcco.equals(errMsg)) {
			logger.error("failed to bind card with UserName:" + bindBankCard.getUserName() + " pid:" +
					bindBankCard.getUserPid() + "bankCode:" + bindBankCard.getBankCode() +
					"userId:" + bindBankCard.getUserId());
			trdAcco = "-1";
		}

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
      logger.error("exception:",ex);
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
      logger.error("meet errors in pay api services");
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

      TrdPayFlow trdPayFlow = new TrdPayFlow();
//      BeanUtils.copyProperties(request.getTrdOrderDetail(), trdPayFlow);
      trdPayFlow.setUpdateBy(request.getUserId());
      trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.PREORDER.getOperation());
      trdPayFlow.setTrdConfirmDate(TradeUtil.getUTCTime());
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
      trdPayFlow.setTradeTargetSum(request.getPayAmount());
      trdPayFlow.setTrdStatus(trdOrderStatusEnum.getStatus());
      trdPayFlowRepository.save(trdPayFlow);
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
          .shellshellfish.aaas.common.message.order.TrdPayFlow();
      BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
//      notifyPayMsg( trdPayFlowMsg);
    } catch (InterruptedException e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
      poprBuilder.setErrMsg(e.getMessage());
    } catch (Exception e) {
      logger.error("exception:",e);
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
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundSum());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(0L);

      trdPayFlow.setTradeTargetSum(trdOrderDetail.getFundSum());
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
        logger.error("exception:",ex);

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
      logger.error("meet errors in pay api services");
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

  /**
   * <pre>
   **
   * 用中证提供的接口获取交易基金的净值
   * </pre>
   */
  public void getLatestFundNet(com.shellshellfish.aaas.finance.trade.pay.FundNetQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.FundNetInfos> responseObserver) {
    List<MongoFundNetInfo> mongoFundNetInfoList = getFundNetInfo(request.getFundCodeList(),
        request.getTradeDays(), request.getUserPid());
    FundNetInfos.Builder fnisBuilder = FundNetInfos.newBuilder();
    for(MongoFundNetInfo mongoFundNetInfo: mongoFundNetInfoList){
      FundNetInfo.Builder fniBuilder = FundNetInfo.newBuilder();
      MyBeanUtils.mapEntityIntoDTO(mongoFundNetInfo, fniBuilder);
      fnisBuilder.addFundNetInfo(fniBuilder);
    }
    responseObserver.onNext(fnisBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public List<MongoFundNetInfo> getFundNetInfo(List<String> fundCodes, int trdDates, String
      userPid) {

    String currentDay = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()).split("T")[0];
    String latestWorkDay = null;
    try {
      latestWorkDay = fundTradeApiService.getWorkDay(TradeUtil.getZZOpenId(userPid),
          "left", 1);
    } catch (Exception e) {
      logger.error("exception:",e);
    }

    List<MongoFundNetInfo> mongoFundNetInfoList = new ArrayList<>();
    if(trdDates == 0){
      //默认取当前10天的交易信息 返回最近的交易信息
      for(String fundCode: fundCodes){
        try {
          List<MongoFundNetInfo> mongoFundNetInfoListInit = initMongoFundNetInfo(fundCode, 10,
              latestWorkDay);
          mongoFundNetInfoList.addAll(mongoFundNetInfoListInit);
        } catch (Exception e) {
          logger.error("exception:",e);
          logger.error(e.getMessage());
        }
      }
      return mongoFundNetInfoList;
    }else{
      for(String fundCode: fundCodes){
        Query findFundNetInfoQuery = new Query();
        findFundNetInfoQuery.addCriteria(Criteria.where("fund_code").in(fundCode).andOperator
            (Criteria.where("trade_date").is(latestWorkDay)));
        findFundNetInfoQuery.with(new Sort(Direction.DESC, "trade_date"));
        findFundNetInfoQuery.limit(1);
        List<MongoFundNetInfo> mongoFundNetInfos = mongoPayTemplate.find(findFundNetInfoQuery,
            MongoFundNetInfo.class);
        if(CollectionUtils.isEmpty(mongoFundNetInfos)){
          try {
            List<MongoFundNetInfo> mongoFundNetInfoListInit = initMongoFundNetInfo(fundCode, 1,
                latestWorkDay);
            mongoFundNetInfoList.addAll(mongoFundNetInfoListInit);
          } catch (Exception e) {
            logger.error("exception:",e);
            logger.error(e.getMessage());
          }
        }else{
          mongoFundNetInfoList.addAll(mongoFundNetInfos);
        }
      }
      return mongoFundNetInfoList;
    }
  }

  @Override
  public boolean sellPercentProd(ProdSellPercentMsg prodSellPercentMsg) {
    logger.info("sell prod with: userId:" + prodSellPercentMsg.getUserId()+ "" + prodSellPercentMsg.getUserUuid
        ()+ prodSellPercentMsg.getTrdAcco());
    String openId = TradeUtil.getZZOpenId(prodSellPercentMsg.getUserPid());
    String tradeAcco = prodSellPercentMsg.getTrdAcco();
    if(CollectionUtils.isEmpty(prodSellPercentMsg.getProdDtlSellDTOList())){
      logger.error("empty ProdDtlSellDTOList list");
      return false;
    }
    for(ProdDtlSellDTO prodDtlSellDTO: prodSellPercentMsg.getProdDtlSellDTOList()){

      processSingleSellReq(openId, prodSellPercentMsg.getUserId(),prodDtlSellDTO.getFundCode(),
          prodDtlSellDTO.getOrderDetailId(),prodSellPercentMsg.getOrderId(),prodDtlSellDTO
              .getFundQuantity(),prodSellPercentMsg.getUserProdId(),prodSellPercentMsg
              .getTrdBrokerId(),prodSellPercentMsg.getTrdAcco() );
    }
//    fundTradeApiService.sellFund(userUuid, sellNum, outsideOrderNo, tradeAcco, fundCode);
    return false;
  }

  public boolean processSingleSellReq(String openId, Long userId, String fundCode, Long
      orderDetailId, String
      orderId, Integer targetQuantity, Long userProdId,  Long brokerId, String tradeAcco){

      int sellNum = targetQuantity;
      String outsideOrderNo = orderId+orderDetailId;
      logger.info("sell prod with fundCode :"+fundCode
          +"sell fund quantity:"+ sellNum + " sell  account:"+ tradeAcco + " outsideOrderNo:" +
          outsideOrderNo);
      if(sellNum <= 0 ){
        logger.error("we cannot sell quantity <= 0 's funds");
        return false;
      }
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setCreateBy(userId);
      trdPayFlow.setUserId(userId);
      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
      trdPayFlow.setUserProdId(userProdId);
      trdPayFlow.setOrderDetailId(orderDetailId);
      trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
      trdPayFlow.setOutsideOrderno(outsideOrderNo);
      BigDecimal sellAmount = BigDecimal.valueOf(0);
      //如果是货币基金，得把原始份额乘以最近交易日的navadj来折算应该售出的份额
      if(!MonetaryFundEnum.containsCode(fundCode)){
        sellAmount = TradeUtil.getBigDecimalNumWithDiv100(Long.valueOf(sellNum));
      }else{
        BigDecimal navadj = BigDecimal.ZERO;
        try {
          Long originNavadj = getMoneyCodeNavAdjNow(fundCode);
          if(originNavadj < 0L){
            logger.error("Failed to get current navadj for :{} with outsideOrderNo:{}", fundCode, outsideOrderNo);
            //ToDo: make notification to let trdOrder know this or, use trdOrder to routine check
            // this issue ? and send request to retry sell?
            return false;

          }
          navadj = TradeUtil.getNavadjByLongOrigin(originNavadj);
          sellAmount = navadj.multiply(TradeUtil.getBigDecimalNumWithDiv100(Long.valueOf(sellNum)));
          logger.info("sell money fundCode:{} with sellAmount:{} originSellNum:{} navadj:{}",
              trdPayFlow.getFundCode(), sellAmount, sellNum, navadj);
        }catch (Exception ex){
          logger.error("exception:", ex);
          logger.error("Failed to get current navadj for :{} with outsideOrderNo:{}", fundCode, outsideOrderNo);
          return false;
        }
      }
      try{
        SellFundResult sellFundResult = fundTradeApiService.sellFund(openId, sellAmount,
            outsideOrderNo, tradeAcco, fundCode);
        if(null != sellFundResult){
          trdPayFlow.setApplySerial(sellFundResult.getApplySerial());
          trdPayFlow.setTrdStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
          trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
          trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
          trdPayFlow.setTradeTargetShare(targetQuantity);
//          trdPayFlow.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
//              .getTargetSellAmount()));
          trdPayFlow.setFundCode(fundCode);
          trdPayFlow.setOutsideOrderno(outsideOrderNo);
          trdPayFlow.setOrderDetailId(orderDetailId);
          trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
          trdPayFlow.setCreateBy(userId);
          trdPayFlow.setUpdateBy(userId);
          trdPayFlow.setTradeAcco(tradeAcco);
          trdPayFlow.setUserProdId(userProdId);
          if(userId <=0 ){
            logger.error("userId is not correct:{}", userId);
          }
          trdPayFlow.setUserId(userId);
          trdPayFlow.setTradeBrokeId(brokerId);
          TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
          com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
              .shellshellfish.aaas.common.message.order.TrdPayFlow();
          BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
          trdPayFlowMsg.getTrdPayFlowExt().setConfirmDateExpected(sellFundResult.getConfirmdate());
          notifySell(trdPayFlowMsg);
        }else{
          //ToDo: 以后这里不需要加回去， 因为没有扣减发生， 需要发消息通知把pendingRecord状态置为handled

          //赎回请求失败，需要把扣减的基金数量加回去
          trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
          trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
          trdPayFlow.setTradeTargetShare(targetQuantity);
//          trdPayFlow.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
//              .getTargetSellAmount()));
          trdPayFlow.setFundCode(fundCode);
          trdPayFlow.setOutsideOrderno(outsideOrderNo);
          trdPayFlow.setOrderDetailId(orderDetailId);
          trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
          trdPayFlow.setCreateBy(userId);
          trdPayFlow.setUpdateBy(userId);
          trdPayFlow.setTradeAcco(tradeAcco);
          trdPayFlow.setUserProdId(userProdId);
          trdPayFlow.setTrdStatus(TrdOrderStatusEnum.REDEEMFAILED.getStatus());
          notifyPendingRecordsFailed(trdPayFlow);
        }
      }catch (Exception ex){
        logger.error("exception:",ex);

        logger.error("because of error:" + ex.getMessage() + " we need send out failed notification");

        trdPayFlow.setTrdType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setTradeTargetShare(targetQuantity);
//          trdPayFlow.setTradeTargetSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
//              .getTargetSellAmount()));
        trdPayFlow.setFundCode(fundCode);
        trdPayFlow.setOutsideOrderno(outsideOrderNo);
        trdPayFlow.setOrderDetailId(orderDetailId);
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(userId);
        trdPayFlow.setUpdateBy(userId);
        trdPayFlow.setTradeAcco(tradeAcco);
        trdPayFlow.setUserProdId(userProdId);
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.REDEEMFAILED.getStatus());
        if(!StringUtils.isEmpty(ex.getMessage())){
          if(ex.getMessage().split(":").length >= 2){
            trdPayFlow.setErrCode(ex.getMessage().split(":")[0]);
            trdPayFlow.setErrMsg(ex.getMessage());
          }else{
            logger.error("strange err message from ZZ:"+ ex.getMessage());
          }
        }
        notifyPendingRecordsFailed(trdPayFlow);

        return false;
    }
    return true;
  }


  private List<MongoFundNetInfo> initMongoFundNetInfo(String fundCode, int days, String
      latestWorkDay){
    String currentDay = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()).split("T")[0];
    List<MongoFundNetInfo> mongoFundNetInfoList = new ArrayList<>();
    try {
      if(MonetaryFundEnum.containsCode(fundCode)){
        logger.info("monetary fund should alwary be 1 for netunit");
      }
      int dayStart = days -1 >= 0? 0: days;//按照中证的定义0就是默认最近的一个交易日
      List<FundNetZZInfo> fundNets =  fundTradeApiService.getFundNets(fundCode, dayStart, days);
      if(!CollectionUtils.isEmpty(fundNets)){
        for(FundNetZZInfo fundNet: fundNets){
          String key = fundCode;
          Query findFundNetInfoQuery = new Query();
          findFundNetInfoQuery.addCriteria(Criteria.where("fund_code").is(fundCode).andOperator
              (Criteria.where("trade_date").is(String.join("",fundNet.getTradeDate().split("-")
              ))));
          findFundNetInfoQuery.with(new Sort(Direction.DESC, "trade_date"));
          findFundNetInfoQuery.limit(1);
          List<MongoFundNetInfo> mongoFundNetInfos = mongoPayTemplate.find(findFundNetInfoQuery,
              MongoFundNetInfo.class);
          if(CollectionUtils.isEmpty(mongoFundNetInfos )){
            MongoFundNetInfo mongoFundNetInfo = new MongoFundNetInfo();
            MyBeanUtils.mapEntityIntoDTO(fundNet, mongoFundNetInfo);
            mongoFundNetInfo.setFundCode(fundCode);
            mongoFundNetInfo.setTradeDate(String.join("", fundNet.getTradeDate().split("-")));
            mongoPayTemplate.save(mongoFundNetInfo);
          }else{
            logger.info("the record of mongoFundNetInfo of fundCode:"+ fundCode + " and "
                + "trdDate:" + fundNet.getTradeDate() +" is already exists");
          }
        }
      }
      Query findFundNetInfoQuery = new Query();
      findFundNetInfoQuery.addCriteria(Criteria.where("fund_code").is(fundCode));
      findFundNetInfoQuery.with(new Sort(Direction.DESC, "trade_date"));
      findFundNetInfoQuery.limit(1);
      List<MongoFundNetInfo> mongoFundNetInfos = mongoPayTemplate.find(findFundNetInfoQuery,
          MongoFundNetInfo.class);
      mongoFundNetInfoList.add(mongoFundNetInfos.get(0));
    } catch (Exception e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
    }
    return mongoFundNetInfoList;
  }


//  public List<MongoFundNetInfo> getFundNetInfoByTradeDate(String tradeDate, String fundCode) throws
//      Exception {
//    if(!tradeDate.contains("-")){
//      throw new Exception(String.format("tradeDate is illeagule:%s",tradeDate));
//    }
//    Query findFundNetInfoQuery = new Query();
//    findFundNetInfoQuery.addCriteria(Criteria.where("fund_code").is(fundCode));
//    findFundNetInfoQuery.with(new Sort(Direction.DESC, "trade_date"));
//    findFundNetInfoQuery.limit(1);
//    List<MongoFundNetInfo> mongoFundNetInfos = mongoPayTemplate.find(findFundNetInfoQuery,
//        MongoFundNetInfo.class);
//  }

  public Long getMoneyCodeNavAdjNow(String fundCode) throws Exception {
    String curentDateTime = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime());
    String applyDate = curentDateTime.split("T")[0];
    if(MonetaryFundEnum.containsCode(fundCode)){
      String beginDate = TradeUtil.getDayBefore(applyDate, 1);
      List<String> codes = new ArrayList<>();
      codes.add(fundCode);
      List<DCDailyFunds> dcDailyFunds = dataCollectionService.getFundDataOfDay(codes,
          beginDate, applyDate);
      DCDailyFunds dcDailyFundsFound = null;
      if(!CollectionUtils.isEmpty(dcDailyFunds)){
        Long latestDate = 0L;
        if(dcDailyFunds.size() > 1){
          for(DCDailyFunds item: dcDailyFunds){
            if(latestDate < item.getNavLatestDate()){
              dcDailyFundsFound = item;
              latestDate = item.getNavLatestDate();
            }
          }
        }
        Double navadj = dcDailyFundsFound.getNavadj();
        return TradeUtil.getLongNumWithMul1000000(navadj);
      }else{
        int beginDays = 1;
        while(CollectionUtils.isEmpty(dcDailyFunds)){
          String beginDateTry = TradeUtil.getDayBefore(applyDate, beginDays++);
          dcDailyFunds = dataCollectionService.getFundDataOfDay(codes,
              beginDateTry, applyDate);
          if(beginDays > 100){
            logger.error("The fund Database should be emptyed, you need to handle this"
                + " issue before trade can be handled");
            throw new Exception("The fund Database should be emptyed, you need to handle this"
                + " issue before trade can be handled");
          }
        }
        Double navadj = dcDailyFunds.get(0).getNavadj();
        return TradeUtil.getLongNumWithMul1000000(navadj);
      }
    }
    return -1L;
  }
  /**
   * <pre>
   **
   * 检查订单创立后状态一直没有改变的数据，用orderId+orderDetailId fundCode 以及applySerial查询
   * 如果查到不一致: 1. trd_pay_flow 有记录， 那么直接把该记录拿来更新 trd_order_detail
   * 2. trd_pay_flow 没有记录，那么直接用trd_order_detail 去试图生成交易trd_pay_flow如果中证已经有交易
   * 记录，那么调用查询接口获取最新的状态，用最新的trd_pay_flow状态来更新trd_order_detail
   * 3. trd_pay_flow 没有记录，调中证接口查询也没有对应的交易记录，那么看order_detail的create时间，
   * 如果当前时间和create时间在同一个交易日， 而且时间已经超过1小时，那么试图用order_detail去生成trd_pay_flow发起交易
   * 否则直接标记trd_order_detail状态为失败
   * </pre>
   */
  public void patchPayFlowWithOrderDetail(com.shellshellfish.aaas.finance.trade.pay.OrderDetailQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.grpc.common.PayFlowResult> responseObserver) {

    try{
      PayFlowResult.Builder pfrBuilder = PayFlowResult.newBuilder();
      String outsideOrderNo = request.getOrderDetail().getOrderId()+request.getOrderDetail()
          .getId();
      List<TrdPayFlow> trdPayFlows = trdPayFlowRepository.findAllByOutsideOrderno(outsideOrderNo);
      if(CollectionUtils.isEmpty(trdPayFlows) && !StringUtils.isEmpty(request.getOrderDetail()
          .getTradeApplySerial())){
        trdPayFlows = trdPayFlowRepository.findAllByApplySerial(request.getOrderDetail()
            .getTradeApplySerial());
      }
      if(CollectionUtils.isEmpty(trdPayFlows)){
        //检查中证系统中是否已经有该外部订单号，如果有那么就把对应的信息取回，并且patch一个TrdPayFlow
        if(StringUtils.isEmpty(request.getPid())){
          throw new Exception("there is no pid parameter in request");
        }
        ApplyResult applyResult = queryZZResultByOutsideOrderNo(request.getPid(), outsideOrderNo);
        if(applyResult == null && !StringUtils.isEmpty(request.getOrderDetail()
            .getTradeApplySerial())){
          applyResult = queryZZResultByApplySerial(request.getPid(), request.getOrderDetail()
              .getTradeApplySerial());
        }
        boolean shouldCheckStatus = false;
        if(applyResult != null){
          logger.error("this order had already been applied to zz info before, now patch it");
          TrdPayFlow trdPayFlow = new TrdPayFlow();
          MyBeanUtils.mapEntityIntoDTO(request.getOrderDetail(), trdPayFlow);
          trdPayFlow.setOrderDetailId(request.getOrderDetail().getId());
          trdPayFlow.setOutsideOrderno(outsideOrderNo);
          trdPayFlow.setUserId(request.getOrderDetail().getUserId());
          trdPayFlow.setTrdApplyDate(applyResult.getApplydate());
          trdPayFlow.setApplySerial(applyResult.getApplyserial());
          trdPayFlow.setTrdType(request.getOrderDetail().getTradeType());
          //Todo set tradeStatus here !!!
          int kkStat = Integer.parseInt(applyResult.getKkstat());
          String kkStatName = ZZKKStatusEnum.getByStatus(kkStat).getComment();
          logger.info("applyResult:"+ applyResult.getOutsideorderno() +" kkStat:"+ kkStat + " "
              + "kkStatName:" + kkStatName);
          int queryStatus = ZZStatsToOrdStatsUtils
              .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                  Integer.valueOf(applyResult.getConfirmflag())),TrdOrderOpTypeEnum.getByOper
                      (trdPayFlow.getTrdType()), ZZKKStatusEnum.getByStatus(kkStat)).getStatus();
//          if(trdPayFlow.getTrdStatus() == queryStatus){
//            logger.error("There is no status change for applySerial:{}, current "
//                + "status:{} queryStatus:{}", applyResult.getApplyserial(), trdPayFlow
//                .getTrdStatus(), queryStatus);
//          }
          trdPayFlow.setTrdStatus(queryStatus);
          trdPayFlow.setTradeAcco(request.getTrdAcco());
          List<MyEntry<String,TrdPayFlow>> trdPayFlowsConfirm = new ArrayList<>();
          checkFundsTradeJobService.updateTrdPayFlowWithApplyResult(request.getPid(), applyResult,
              trdPayFlow, trdPayFlowsConfirm, shouldCheckStatus);
          checkFundsTradeJobService.checkAndSendConfirmInfo(trdPayFlowsConfirm);
        }else{
          if(TradeUtil.getUTCTime() - request.getOrderDetail().getCreateDate() > 60*60*1000L){
            logger.error("this order have no trade happened in Zhongzheng, so we make it failed");
            logger.error("the zzinformation system havent found the outsideOrder:{} of pid:{}",
                outsideOrderNo, request.getPid());
            if(request.getOrderDetail().getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation()){
              pfrBuilder.setTrdStatus(TrdOrderStatusEnum.FAILED.getStatus());
            }else if(request.getOrderDetail().getTradeType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
              pfrBuilder.setTrdStatus(TrdOrderStatusEnum.REDEEMFAILED.getStatus());
            }else{
              logger.error("current we havent have specific way to handle trdType:{}", request
                  .getOrderDetail().getTradeType());
              pfrBuilder.setTrdStatus(TrdOrderStatusEnum.FAILED.getStatus());
            }
          }

        }
        responseObserver.onNext(pfrBuilder.build());
        responseObserver.onCompleted();
        return;

      }else{

        //检查是否order状态滞后
        if(TradeUtil.isLatterThan(TrdOrderStatusEnum.getTrdOrderStatusEnum(trdPayFlows.get(0)
            .getTrdStatus()), TrdOrderStatusEnum.getTrdOrderStatusEnum(request.getOrderDetail()
            .getOrderDetailStatus()))){
          logger.error("OrderDetail status:{} TrdPayFlow status:{} need to patch", request
              .getOrderDetail().getOrderDetailStatus(), trdPayFlows.get(0).getTrdStatus());
          com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
              .shellshellfish.aaas.common.message.order.TrdPayFlow();
          MyBeanUtils.mapEntityIntoDTO(trdPayFlows.get(0), trdPayFlowMsg);
          broadcastMessageProducers.sendMessage(trdPayFlowMsg);
          if(trdPayFlows.get(0).getTrdStatus() == TrdOrderStatusEnum.SELLCONFIRMED.getStatus() ||
              trdPayFlows.get(0).getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()){

            checkFundsTradeJobService.checkAndSendConfirmInfo(trdPayFlows, request.getPid());
          }
        }
        MyBeanUtils.mapEntityIntoDTO(trdPayFlows.get(0), pfrBuilder);
        responseObserver.onNext(pfrBuilder.build());
        responseObserver.onCompleted();
      }

    }catch (Exception ex){
      onError(responseObserver, ex);
    }

  }

  private void onError(StreamObserver responseObserver, Exception ex){
    responseObserver.onError(Status.INTERNAL
        .withDescription(ex.getMessage())
        .augmentDescription("customException()")
        .withCause(ex) // This can be attached to the Status locally, but NOT transmitted to
        // the client!
        .asRuntimeException());
  }

  private ApplyResult queryZZResultByOutsideOrderNo(String pid, String outsideOrderNo){
    String openId = TradeUtil.getZZOpenId(pid);
    ApplyResult applyResult = null;
    try {
      applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(openId, outsideOrderNo);
    } catch (JsonProcessingException e) {
      logger.error("error:", e);
    }
    return applyResult;
  }

  private ApplyResult queryZZResultByApplySerial(String pid, String applySerial){
    String openId = TradeUtil.getZZOpenId(pid);
    ApplyResult applyResult = null;
    try {
      applyResult = fundTradeApiService.getApplyResultByApplySerial(openId, applySerial);
    } catch (JsonProcessingException e) {
      logger.error("error:", e);
    }
    return applyResult;
  }
}
