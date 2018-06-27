package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.DataCollectorUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.redis.UserPidDAO;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfos;
import com.shellshellfish.aaas.finance.trade.pay.FundNetQuery;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailQuery;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import com.shellshellfish.aaas.grpc.common.PayFlowResult;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 23
 */
@Service
public class PayServiceImpl implements PayService {

  Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

  PayRpcServiceFutureStub payRpcFutureStub;

  @Autowired
  OrderService orderService;

  @Autowired
  UserInfoService userInfoService;

  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Autowired
  ManagedChannel managedPayChannel;

  @Resource
  UserPidDAO userPidDAO;

  @PostConstruct
  public void init(){
    payRpcFutureStub = PayRpcServiceGrpc.newFutureStub(managedPayChannel);
  }

  public void shutdown() throws InterruptedException {
    managedPayChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

	@Override
	public String bindCard(BindBankCard bindBankCard)
      throws Exception {
		final String errMsg = "-1";
		logger.info("bindCard:" + bindBankCard);
		BindBankCardQuery.Builder builder = BindBankCardQuery.newBuilder();
		BeanUtils.copyProperties(bindBankCard, builder, DataCollectorUtil.getNullPropertyNames(bindBankCard));
		builder.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());

    BindBankCardResult bindBankCardResult = payRpcFutureStub.bindBankCard(builder.build()).get();

		String trdAcco = bindBankCardResult.getTradeacco();

		if (trdAcco == null || errMsg.equals(trdAcco) || !StringUtils.isEmpty(bindBankCardResult
        .getErrInfo().getErrMsg())){
		  logger.error("failed to bindCard because of errCode:{} and errMsg:{}",bindBankCardResult
          .getErrInfo().getErrCode(), bindBankCardResult.getErrInfo().getErrMsg());

		  throw new Exception(bindBankCardResult.getErrInfo().getErrCode()+"|"+bindBankCardResult
          .getErrInfo().getErrMsg());
    }


    TrdBrokerUser trdBrokerUserOld = trdBrokerUserRepository.findByUserIdAndBankCardNum(bindBankCard
            .getUserId(), bindBankCard.getBankCardNum());
    if(trdBrokerUserOld != null){
      logger.error("the intending bind card user already have trade account there ");
      trdBrokerUserOld.setTradeAcco(trdAcco);
      trdBrokerUserOld.setBankCardNum(bindBankCard.getBankCardNum());
      trdBrokerUserOld.setTradeAcco(trdAcco);
      trdBrokerUserOld.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId().intValue());
      trdBrokerUserOld.setUserId(bindBankCard.getUserId());
      trdBrokerUserOld.setUpdateBy(bindBankCard.getUserId());
      trdBrokerUserOld.setUpdateDate(TradeUtil.getUTCTime());
      trdBrokerUserRepository.save(trdBrokerUserOld);
      //update userPidDao
      userPidDAO.deleteUserPid(trdAcco, trdBrokerUserOld.getTradeBrokerId(), bindBankCard.getUserId());
    }else{
      TrdBrokerUser trdBrokerUserNew = new TrdBrokerUser();
      trdBrokerUserNew.setTradeAcco(trdAcco);
      trdBrokerUserNew.setBankCardNum(bindBankCard.getBankCardNum());
      trdBrokerUserNew.setCreateBy(bindBankCard.getUserId());
      trdBrokerUserNew.setCreateDate(TradeUtil.getUTCTime());
      trdBrokerUserNew.setTradeAcco(trdAcco);
      trdBrokerUserNew.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId().intValue());
      trdBrokerUserNew.setUserId(bindBankCard.getUserId());
      trdBrokerUserNew.setUpdateBy(bindBankCard.getUserId());
      trdBrokerUserNew.setUpdateDate(TradeUtil.getUTCTime());
      trdBrokerUserRepository.save(trdBrokerUserNew);
      //update userPidDao
      userPidDAO.deleteUserPid(trdAcco, trdBrokerUserOld.getTradeBrokerId(), bindBankCard.getUserId());
    }
		return trdAcco;
	}


  @Override
  public int order2PayJob(PayOrderDto payOrderDto) {
    OrderPayReq.Builder bdOfReq = OrderPayReq.newBuilder();
    bdOfReq.setTrdBrokerId(payOrderDto.getTrdBrokerId());
    bdOfReq.setUserProdId(payOrderDto.getUserProdId());
    bdOfReq.setTrdAccount(payOrderDto.getTrdAccount());
    bdOfReq.setUserUuid(payOrderDto.getUserUuid());
    OrderDetailPayReq.Builder ordDetailReqBuilder = OrderDetailPayReq.newBuilder();
    for(TrdOrderDetail trdOrderDetail: payOrderDto.getOrderDetailList()){
      BeanUtils.copyProperties(trdOrderDetail, ordDetailReqBuilder);
      bdOfReq.addOrderDetailPayReq(ordDetailReqBuilder);
      ordDetailReqBuilder.clear();
    }

    try {
      return payRpcFutureStub.orderJob2Pay(bdOfReq.build()).get().getResult();
    } catch (InterruptedException e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
      return -1;
    } catch (ExecutionException e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
      return -1;
    }
  }

  @Override
  public TrdOrderStatusEnum order2Pay(PayOrderDto payOrderDto) {
    OrderPayReq.Builder bdOfReq = OrderPayReq.newBuilder();
    bdOfReq.setTrdBrokerId(payOrderDto.getTrdBrokerId());
    bdOfReq.setUserProdId(payOrderDto.getUserProdId());
    bdOfReq.setTrdAccount(payOrderDto.getTrdAccount());
    bdOfReq.setUserUuid(payOrderDto.getUserUuid());
    bdOfReq.setUserPid(payOrderDto.getUserPid());
    OrderDetailPayReq.Builder ordDetailReqBuilder = OrderDetailPayReq.newBuilder();
    for(TrdOrderDetail trdOrderDetail: payOrderDto.getOrderDetailList()){
      BeanUtils.copyProperties(trdOrderDetail, ordDetailReqBuilder, DataCollectorUtil
          .getNullPropertyNames(trdOrderDetail));
      bdOfReq.addOrderDetailPayReq(ordDetailReqBuilder);
      ordDetailReqBuilder.clear();
    }
    List<TrdOrderDetail> trdOrderDetailList = new ArrayList<>();
    try {

      List<com.shellshellfish.aaas.finance.trade.pay.OrderPayResultDetail> orderDetailPayReqList =
      payRpcFutureStub.order2Pay(bdOfReq.build()).get().getOrderPayResultDetailList();
      int orderResult = payRpcFutureStub.order2Pay(bdOfReq.build()).get().getResult();
      if(orderResult == 1){
        return TrdOrderStatusEnum.PAYWAITCONFIRM;
      }else{
        return TrdOrderStatusEnum.FAILED;
      }

//      for(OrderPayResultDetail orderPayResultDetail: orderDetailPayReqList){
//        TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
//        BeanUtils.copyProperties(orderPayResultDetail, trdOrderDetail);
//        trdOrderDetailList.add(trdOrderDetail);
//      }
    } catch (InterruptedException e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
      return TrdOrderStatusEnum.FAILED;
    } catch (ExecutionException e) {
      logger.error("exception:",e);
      logger.error(e.getMessage());
      return TrdOrderStatusEnum.FAILED;
    }

  }

  @Override
  public PreOrderPayResult preOrder2Pay(PreOrderPayReq preOrderPayReq)
      throws ExecutionException, InterruptedException {
    PreOrderPayResult preOrderPayResult  = payRpcFutureStub.preOrder2Pay(preOrderPayReq).get();

    return preOrderPayResult;
  }

  @Override
  public List<FundNetInfo> getFundNetInfo(String userPid , List<String> fundCodes, int days)
      throws ExecutionException, InterruptedException {
    FundNetQuery.Builder fnqBuilder = FundNetQuery.newBuilder();
    for(String fundCode: fundCodes){
      fnqBuilder.addFundCode(fundCode);
    }
    fnqBuilder.setTradeDays(days);
    fnqBuilder.setUserPid(userPid);
    FundNetInfos fundNetInfos = payRpcFutureStub.getLatestFundNet(fnqBuilder.build()).get();
    return fundNetInfos.getFundNetInfoList();
  }

  @Override
  public TrdPayFlow patchOrderToPay(
      com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail trdOrderDetail) {
    OrderDetailQuery.Builder odqBuilder = OrderDetailQuery.newBuilder();
    OrderDetail.Builder odBuilder = OrderDetail.newBuilder();
  //Todo: add parameters
    PayFlowResult trdPayFlowResult = null;
    TrdOrder trdOrder = orderService.getOrderByOrderId(trdOrderDetail.getOrderId());
    String pid = userInfoService.getUserPidByBankCard(trdOrder.getBankCardNum());
    TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndBankCardNum
        (trdOrder.getUserId(), trdOrder.getBankCardNum());
    if(StringUtils.isEmpty(pid) && StringUtils.isEmpty(trdOrderDetail.getTradeApplySerial())){
      logger.error("Failed to get pid for bankCard:{} set this order status as failed",
          trdOrderDetail.getBankCardNum());
      TrdPayFlow trdPayFlow = new TrdPayFlow();

      trdPayFlow.setTrdStatus(TrdOrderStatusEnum.CANCELED.getStatus());
      if(trdPayFlow.getTrdType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.REDEEMFAILED.getStatus());
      }else{
        trdPayFlow.setTrdStatus(TrdOrderStatusEnum.FAILED.getStatus());
      }
      return trdPayFlow;
    }
    MyBeanUtils.mapEntityIntoDTO(trdOrderDetail, odBuilder);
    odqBuilder.setOrderDetail(odBuilder);
    odqBuilder.setPid(pid);
    odqBuilder.setTrdAcco(trdBrokerUser.getTradeAcco());
    try {

      trdPayFlowResult = payRpcFutureStub.patchPayFlowWithOrderDetail(odqBuilder.build()).get();
    } catch (Exception e) {
      logger.error("call payRpcFutureStub.patchPayFlowWithOrderDetail got exception:", e);
    }
    TrdPayFlow trdPayFlow = new TrdPayFlow();
    if(trdPayFlowResult != null){
      MyBeanUtils.mapEntityIntoDTO(trdPayFlowResult, trdPayFlow);
    }
    return trdPayFlow;
  }
}
