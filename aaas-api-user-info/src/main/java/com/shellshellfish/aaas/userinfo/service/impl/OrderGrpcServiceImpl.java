package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 二月 - 09
 */
@Service
public class OrderGrpcServiceImpl implements OrderRpcService {

  private static final Logger logger = LoggerFactory.getLogger(OrderGrpcServiceImpl.class);

  @Autowired
  OrderRpcServiceBlockingStub tradeOrderServiceBlockingStub;



  @Autowired
  UserInfoService userInfoService;


  @Autowired
  UserInfoRepository userInfoRepository;



  @Override
  public String getBankCardNumberByUserProdId(Long userProdId) {
    UserProdId.Builder npiBuilder = UserProdId.newBuilder();
    npiBuilder.setUserProdId(userProdId);
    return tradeOrderServiceBlockingStub.getBankCardNumByUserProdId(npiBuilder.build()).getUserBankCardnum();
  }



}
