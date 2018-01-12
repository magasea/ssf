package com.shellshellfish.aaas.finance.trade.order.service.impl;


import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;

import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceMoneyFundInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceFutureStub;
import io.grpc.ManagedChannel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FinanceProdInfoServiceImpl implements FinanceProdInfoService {


  Logger logger = LoggerFactory.getLogger(FinanceProdInfoServiceImpl.class);

  FinanceProductServiceFutureStub financeProductServiceFutureStub;




  @Autowired
  ManagedChannel managedFINChannel;



  @PostConstruct
  public void init(){
    financeProductServiceFutureStub = FinanceProductServiceGrpc.newFutureStub(managedFINChannel);
  }

  public void shutdown() throws InterruptedException {
    managedFINChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
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

  @Override
  public FinanceMoneyFundInfo getMoneyFund(FinanceProdInfoQuery request) {
    //Todo: 需要在对端实现真实逻辑
    FinanceMoneyFundInfo.Builder fmfiBuilder = FinanceMoneyFundInfo.newBuilder();
    fmfiBuilder.setFundCode("001987.OF");
    fmfiBuilder.setFundName("东方金元宝货币");
    return fmfiBuilder.build();
  }

}
