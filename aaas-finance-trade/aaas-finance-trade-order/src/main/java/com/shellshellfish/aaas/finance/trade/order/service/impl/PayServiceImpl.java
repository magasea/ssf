package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery;
import com.shellshellfish.aaas.finance.trade.pay.OrderDetailPayReq;
import com.shellshellfish.aaas.finance.trade.pay.OrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceBlockingStub;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import io.grpc.ManagedChannel;
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
      //ToDo:
    logger.info("bindCard:" + bindBankCard);
    BindBankCardQuery.Builder builder = BindBankCardQuery.newBuilder();
    BeanUtils.copyProperties(bindBankCard, builder);
    return payRpcFutureStub.bindBankCard(builder.build()).get().getTradeacco();
  }


  @Override
  public int order2PayJob(PayDto payDto) {
    OrderPayReq.Builder bdOfReq = OrderPayReq.newBuilder();
    bdOfReq.setTrdBrokerId(payDto.getTrdBrokerId());
    bdOfReq.setUserProdId(payDto.getUserProdId());
    bdOfReq.setTrdAccount(payDto.getTrdAccount());
    bdOfReq.setUserUuid(payDto.getUserUuid());
    OrderDetailPayReq.Builder ordDetailReqBuilder = OrderDetailPayReq.newBuilder();
    for(TrdOrderDetail trdOrderDetail: payDto.getOrderDetailList()){
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

}
