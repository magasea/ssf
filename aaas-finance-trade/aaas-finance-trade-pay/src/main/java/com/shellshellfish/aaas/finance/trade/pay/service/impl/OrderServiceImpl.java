package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.finance.trade.order.GetPIDReq;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.UserPID;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.service.OrderService;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by chenwei on 2018- 一月 - 22
 */
@Service
public class OrderServiceImpl implements OrderService {


  @Autowired
  ManagedChannel managedOrderChannel;

  OrderRpcServiceGrpc.OrderRpcServiceFutureStub orderRpcServiceFutureStub;

  @PostConstruct
  void init(){
    orderRpcServiceFutureStub = OrderRpcServiceGrpc.newFutureStub(managedOrderChannel);
  }

  @Override
  public String getPidFromTrdAccoBrokerId(TrdPayFlow trdPayFlow) throws Exception {
    GetPIDReq.Builder gpidrBuilder = GetPIDReq.newBuilder();
    gpidrBuilder.setTrdAcco(trdPayFlow.getTradeAcco());
    gpidrBuilder.setUserId(trdPayFlow.getUserId());
    gpidrBuilder.setTrdBrokerId(Math.toIntExact(trdPayFlow.getTradeBrokeId()));
    UserPID userPID = orderRpcServiceFutureStub.getPidFromTrdAccoBrokerId(gpidrBuilder.build()).get();
    return  userPID.getUserPid();
  }
}
