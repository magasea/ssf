package com.shellshellfish.aaas.finance.trade.order.service.impl;


import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;

import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceFutureStub;
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


public class FinanceProdInfoServiceImpl implements FinanceProdInfoService {


  Logger logger = LoggerFactory.getLogger(FinanceProdInfoServiceImpl.class);

  FinanceProductServiceFutureStub financeProductServiceFutureStub;



  @Autowired
  ManagedChannel managedChannel;


  @PostConstruct
  public void init(){
    financeProductServiceFutureStub = FinanceProductServiceGrpc.newFutureStub(managedChannel);
  }

  public void shutdown() throws InterruptedException {
    managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  @Override
  public List<ProductMakeUpInfo> getFinanceProdMakeUpInfo(ProductBaseInfo productBaseInfo)
      throws ExecutionException, InterruptedException {
    FinanceProdInfoQuery.Builder builder = FinanceProdInfoQuery.newBuilder();
    BeanUtils.copyProperties(productBaseInfo, builder);
    List<com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo> financeProdInfoList =
    financeProductServiceFutureStub.getFinanceProdInfo(builder.build()).get().getFinanceProdInfoList();
    List<ProductMakeUpInfo> productMakeUpInfoList = new ArrayList<>();
    for(FinanceProdInfo prodInfoItem: financeProdInfoList){
      ProductMakeUpInfo productMakeUpInfo = new ProductMakeUpInfo();
      BeanUtils.copyProperties(prodInfoItem, productMakeUpInfo);
      productMakeUpInfoList.add(productMakeUpInfo);
    }
    return productMakeUpInfoList;
  }
}
