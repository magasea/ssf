package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.DataCollectorUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
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
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Autowired
  ManagedChannel managedPayChannel;

  @PostConstruct
  public void init(){
    payRpcFutureStub = PayRpcServiceGrpc.newFutureStub(managedPayChannel);
  }

  public void shutdown() throws InterruptedException {
    managedPayChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

	@Override
	public String bindCard(BindBankCard bindBankCard)
			throws ExecutionException, InterruptedException {
		final String errMsg = "-1";
		logger.info("bindCard:" + bindBankCard);
		BindBankCardQuery.Builder builder = BindBankCardQuery.newBuilder();
		BeanUtils.copyProperties(bindBankCard, builder, DataCollectorUtil.getNullPropertyNames(bindBankCard));
		builder.setTradeBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());


		String trdAcco = payRpcFutureStub.bindBankCard(builder.build()).get().getTradeacco();

		if (trdAcco == null || errMsg.equals(trdAcco))
			return errMsg;


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
      e.printStackTrace();
      logger.error(e.getMessage());
      return -1;
    } catch (ExecutionException e) {
      e.printStackTrace();
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
      e.printStackTrace();
      logger.error(e.getMessage());
      return TrdOrderStatusEnum.FAILED;
    } catch (ExecutionException e) {
      e.printStackTrace();
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



}
