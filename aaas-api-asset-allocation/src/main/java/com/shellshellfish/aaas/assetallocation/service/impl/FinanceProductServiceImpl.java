package com.shellshellfish.aaas.assetallocation.service.impl;
import com.shellshellfish.aaas.assetallocation.service.FinanceProductService;
import com.shellshellfish.aaas.assetallocation.neo.entity.Interval;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductDetailInfoPage;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductDetailQueryInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.utils.DataCollectorUtil;
import com.shellshellfish.aaas.trade.finance.prod.*;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection.Builder;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceImplBase;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceProductServiceImpl  extends
    FinanceProductServiceImplBase implements FinanceProductService {

  Logger logger = LoggerFactory.getLogger(FinanceProductServiceImpl.class);

  @Autowired
  private FundGroupMapper fundGroupMapper;

  @Override
  public List<ProductMakeUpInfo> getProductInfo(ProductBaseInfo productBaseInfo) {
    //ToDo: 目前是用hardCode数据 将来要用真实开发的功能来填充
    logger.info("prodId:{} groupId:{}",productBaseInfo.getProdId(), productBaseInfo.getGroupId());
    return generateTestData(productBaseInfo.getProdId().toString(), productBaseInfo.getGroupId().toString());
  }

  @Override
  public ProductDetailInfoPage getProductDetailInfo(ProductDetailQueryInfo productDetailQueryInfo) {
    return generateTestData(productDetailQueryInfo);
  }

//  @Override
//  public void getFinanceProds(com.shellshellfish.aaas.trade.finance.prod.FinanceProdDetailQuery request,
//      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.trade.finance.prod.FinanceProdDetails>
//          responseObserver){
//    ProductDetailQueryInfo productDetailQueryInfo = new ProductDetailQueryInfo();
//    BeanUtils.copyProperties(request, productDetailQueryInfo);
//    ProductDetailInfoPage productDetailInfoPage = getProductDetailInfo(productDetailQueryInfo);
//    FinanceProdDetailItem.Builder builderOfFPDI = FinanceProdDetailItem.newBuilder();
//    FinanceProdDetails.Builder builderOfFPD = FinanceProdDetails.newBuilder();
//
//    for(Map<String, Object> item: productDetailInfoPage.getItems()){
//      //Todo: need coding
////      for(Entry<String, Object> subItem: item.entrySet()){
////        builderOfFPDI.setField(subItem.getKey(),subItem.getKey());
////      }
////      BeanUtils.copyProperties(productMakeUpInfo, builderOfFPI);
////      builderOfFPIC.addFinanceProdInfo(builderOfFPI);
////      builderOfFPI.clear();
//    }
//    responseObserver.onNext(builderOfFPD.build());
//    responseObserver.onCompleted();
//  }

  private ProductDetailInfoPage generateTestData(ProductDetailQueryInfo productDetailQueryInfo){
    ProductDetailInfoPage productDetailInfoPage = new ProductDetailInfoPage();
    productDetailInfoPage.setGroupId(productDetailQueryInfo.getGroupId());
    productDetailInfoPage.setPageNum(productDetailQueryInfo.getPageNum());
    productDetailInfoPage.setPageSize(productDetailQueryInfo.getPageSize());
    productDetailInfoPage.setProdId(productDetailQueryInfo.getProdId());
    List<Map<String, Object>> items = new ArrayList<>();
    for(int idx = 0; idx < 100; idx ++){
      Map<String, Object> item = new HashMap<>();
      if(idx%4 == 0){
        item.put(""+idx, new Integer(idx));
      }else if(idx%4 == 1){
        item.put(""+idx, ""+idx);
      }else if(idx%4 == 2){
        item.put(""+idx, new BigDecimal(idx));
      }else if(idx%4 == 1){
        item.put(""+idx, Long.valueOf(idx));
      }
      items.add(item);
    }
    productDetailInfoPage.setItems(items);
    return productDetailInfoPage;
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
      BeanUtils.copyProperties(productMakeUpInfo, builderOfFPI, DataCollectorUtil
          .getNullPropertyNames(productMakeUpInfo));
      builderOfFPIC.addFinanceProdInfo(builderOfFPI);
      builderOfFPI.clear();
    }
    responseObserver.onNext(builderOfFPIC.build());
    responseObserver.onCompleted();
  }

  private List<ProductMakeUpInfo> generateTestData(String id,String subGroupId ){
    List<ProductMakeUpInfo> productMakeUpInfos = new ArrayList<>();
    Map<String,String> query = new HashMap<>();
    query.put("id",id);
    query.put("subGroupId",subGroupId);
    List<Interval> intervalList = fundGroupMapper.selectById(query);
    if (intervalList.size()>0){
      for (Interval interval : intervalList) {
        ProductMakeUpInfo productMakeUpInfo = new ProductMakeUpInfo();
        productMakeUpInfo.setFundCode(interval.getFund_id());
        productMakeUpInfo.setGroupId(Long.parseLong(interval.getFund_group_id()));

        Integer result = Double.valueOf(interval.getProportion()*10000).intValue();
        logger.info("fundShare:" + interval.getProportion() + "result:"+result);
        productMakeUpInfo.setFundShare(result);
        productMakeUpInfo.setProdName(interval.getFund_group_name());
        productMakeUpInfo.setProdId(Long.parseLong(interval.getFund_group_sub_id()));

        productMakeUpInfo.setFundName(interval.getFname());
        productMakeUpInfos.add(productMakeUpInfo);
      }
    }
    return productMakeUpInfos;
  }
}
