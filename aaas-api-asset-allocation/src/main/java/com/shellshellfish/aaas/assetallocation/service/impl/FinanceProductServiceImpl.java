package com.shellshellfish.aaas.assetallocation.service.impl;
import com.shellshellfish.aaas.assetallocation.neo.entity.Interval;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.service.FinanceProductService;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection.Builder;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceImplBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceProductServiceImpl  extends
    FinanceProductServiceImplBase implements FinanceProductService{

  Logger logger = LoggerFactory.getLogger(FinanceProductServiceImpl.class);

  @Autowired
  private FundGroupMapper fundGroupMapper;


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
    List<ProductMakeUpInfo> productMakeUpInfos = new ArrayList<>();

    String id = "1";
    String subGroupId = "1";
    List<Interval> intervalList = fundGroupMapper.selectById(id, subGroupId);
    if (intervalList.size()>0){
      for (Interval interval : intervalList) {
        ProductMakeUpInfo productMakeUpInfo = new ProductMakeUpInfo();
        productMakeUpInfo.setFundCode(interval.getFund_id());
        productMakeUpInfo.setGroupId(Long.parseLong(interval.getFund_group_id()));
        productMakeUpInfo.setFundShare((int) (interval.getProportion()*10000));
        productMakeUpInfo.setProdName(interval.getFname());
        productMakeUpInfo.setProdId(Long.parseLong(interval.getFund_group_sub_id()));
        productMakeUpInfos.add(productMakeUpInfo);
      }
    }


    /*ProductMakeUpInfo productMakeUpInfo1 = new ProductMakeUpInfo();
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
    productMakeUpInfos.add(productMakeUpInfo2);*/
    return productMakeUpInfos;

  }




}
