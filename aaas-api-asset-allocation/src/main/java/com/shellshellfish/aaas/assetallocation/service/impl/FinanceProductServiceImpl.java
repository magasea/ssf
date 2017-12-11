package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.service.FinanceProductService;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection.Builder;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc
    .FinanceProductServiceImplBase;

public class FinanceProductServiceImpl  extends
    FinanceProductServiceImplBase implements FinanceProductService{

  Logger logger = LoggerFactory.getLogger(FinanceProductServiceImpl.class);

  @Override
  public List<ProductMakeUpInfo> getProductInfo(ProductBaseInfo productBaseInfo) {
    //ToDo: 目前是用hardCode数据 将来要用真实开发的功能来填充
    logger.info("prodId:{} groupId:{}",productBaseInfo.getProdId(), productBaseInfo.getGroupId());
    return generateTestData();
  }

  @Override
  public void getFinanceProdInfo(FinanceProdInfoQuery financeProdInfoQuery,
      StreamObserver<FinanceProdInfoCollection> responseObserver){
    ProductBaseInfo productBaseInfo = new ProductBaseInfo();
    BeanUtils.copyProperties(financeProdInfoQuery, productBaseInfo);
    List<ProductMakeUpInfo> productMakeUpInfos = getProductInfo(productBaseInfo);
    Builder builderOfFPIC = FinanceProdInfoCollection.newBuilder();
    FinanceProdInfo.Builder builderOfFPI = FinanceProdInfo.newBuilder();
    for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      BeanUtils.copyProperties(productMakeUpInfo, builderOfFPI);
      builderOfFPIC.addFinanceProdInfo(builderOfFPI);
      builderOfFPI.clear();
    }
    responseObserver.onNext(builderOfFPIC.build());
    responseObserver.onCompleted();
  }


  private List<ProductMakeUpInfo> generateTestData(){
    int idx = 0;
    List<ProductMakeUpInfo> productMakeUpInfos = new ArrayList<>();

    ProductMakeUpInfo productMakeUpInfo = new ProductMakeUpInfo();
    productMakeUpInfo.setFundCode("160512.SZ");
    productMakeUpInfo.setFundShare(3333);
    productMakeUpInfo.setGroupId(1L);
    productMakeUpInfo.setProdName("理财产品A1");
    productMakeUpInfo.setProdId(100L);
    productMakeUpInfos.add(productMakeUpInfo);
    ProductMakeUpInfo productMakeUpInfo1 = new ProductMakeUpInfo();
    productMakeUpInfo1.setFundCode("290006.OF");
    productMakeUpInfo1.setFundShare(3333);
    productMakeUpInfo1.setGroupId(1L);
    productMakeUpInfo1.setProdName("理财产品A1");
    productMakeUpInfo1.setProdId(100L);
    productMakeUpInfos.add(productMakeUpInfo1);
    ProductMakeUpInfo productMakeUpInfo2 = new ProductMakeUpInfo();
    productMakeUpInfo1.setFundCode("000788.OF");
    productMakeUpInfo1.setFundShare(3333);
    productMakeUpInfo1.setGroupId(1L);
    productMakeUpInfo1.setProdName("理财产品A1");
    productMakeUpInfo1.setProdId(100L);
    productMakeUpInfos.add(productMakeUpInfo2);
    return productMakeUpInfos;

  }




}
