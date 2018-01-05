package com.shellshellfish.aaas.datacollection.server.service.impl;


import com.shellshellfish.aaas.common.utils.DataCollectorUtil;
import com.shellshellfish.aaas.common.utils.MathUtil;
import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.aaas.datacollect.DailyFunds.Builder;
import com.shellshellfish.aaas.datacollect.DailyFundsCollection;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceImplBase;
import com.shellshellfish.aaas.datacollect.FundInfo;
import com.shellshellfish.aaas.datacollect.FundInfos;
import com.shellshellfish.aaas.datacollection.server.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.server.model.DayIndicator;
import com.shellshellfish.aaas.datacollection.server.model.FundResources;
import com.shellshellfish.aaas.datacollection.server.model.FundYeildRate;
import com.shellshellfish.aaas.datacollection.server.repositories.DailyFundsRepository;

import com.shellshellfish.aaas.datacollection.server.repositories.MongoFundYeildRateRepository;
import com.shellshellfish.aaas.datacollection.server.service.DataCollectionService;
import com.shellshellfish.aaas.datacollection.server.service.FundInfoService;
import com.shellshellfish.aaas.datacollection.server.util.DateUtil;
import io.grpc.stub.StreamObserver;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.DateUtils;


public class DataCollectionServiceImpl extends DataCollectionServiceImplBase implements
    DataCollectionService{


  Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);


  private Random random = new Random();

  @Autowired
  DailyFundsRepository dailyFundsRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  MongoFundYeildRateRepository mongoFundYeildRateRepository;

  @Override
  public List<FundYeildRate> getLastFundYeildRates(List<String> fundCodes) {
    Long yestDay = SSFDateUtils.getYestdayDateInLong();
    return  mongoFundYeildRateRepository.findByQuerydateAndCodeIsIn(yestDay/1000L, fundCodes);
  }

  @Override
  public List<FundYeildRate> getLastFundYeildRates4Test(List<String> fundCodes) {
    return mongoFundYeildRateRepository.findByCodeIsIn(fundCodes);
  }

  @Override
  public void getFundsPrice(com.shellshellfish.aaas.datacollect.FundCodes request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.datacollect.FundInfos> responseObserver){
    List<String> codes = request.getFundCodeList();
    FundInfos fundPrices = getPriceOfCodes(codes);
    responseObserver.onNext(fundPrices);
    responseObserver.onCompleted();

  }

  @Override
  public void getFundDataOfDay(DailyFundsQuery request, StreamObserver<DailyFundsCollection>
      responseObserver){
    String navLatestDateStart = request.getNavLatestDateStart();
    String navLatestDateEnd = request.getNavLatestDateEnd();
    List<String> codes = request.getCodesList();
    List<DailyFunds> dailyFundsList = null;
    try {
      Query query = new Query();
      query.addCriteria(Criteria.where("code").in(codes).andOperator(
          Criteria.where("navlatestdate").gt(DateUtil.getDateLongVal
                  (navLatestDateStart)/1000),
          Criteria.where("navlatestdate").lte(DateUtil.getDateLongVal
              (navLatestDateEnd)/1000)));
      dailyFundsList  = mongoTemplate.find(query, DailyFunds.class, "fund_yieldrate");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFundsListProto = new ArrayList<>();
    Builder builderDailyFunds = com.shellshellfish.aaas.datacollect.DailyFunds.newBuilder();
    for(DailyFunds dailyFunds: dailyFundsList){
      BeanUtils.copyProperties(dailyFunds, builderDailyFunds, DataCollectorUtil
          .getNullPropertyNames(dailyFunds));

      Query query = new Query();
      query.addCriteria(Criteria.where("code").is(dailyFunds.getCode()));
      List<FundResources> fundResources = mongoTemplate.find(query, FundResources.class,
          "fundresources");
      if( CollectionUtils.isEmpty(fundResources)){
        logger.error("cannot find fundTypeOne for code:" + dailyFunds.getCode());
      }else{
        builderDailyFunds.setFname(fundResources.get(0).getFname());
        builderDailyFunds.setFirstInvestType(fundResources.get(0).getFirstinvesttype());
        builderDailyFunds.setSecondInvestType(fundResources.get(0).getSecondinvesttype());
      }
      List<DayIndicator> dayIndicators = mongoTemplate.find(query, DayIndicator.class,
          "dayindicator");
      if( CollectionUtils.isEmpty(dayIndicators)){
        logger.error("cannot find close for code:" + dailyFunds.getCode());
      }else{
        builderDailyFunds.setClose(dayIndicators.get(0).getClose());
      }
      dailyFundsListProto.add(builderDailyFunds.build());
      builderDailyFunds.clear();
    }
    final DailyFundsCollection.Builder builder = DailyFundsCollection.newBuilder()
        .addAllDailyFunds(dailyFundsListProto);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }


  public FundInfos getPriceOfCodes(List<String> codes) {
    List<FundYeildRate> fundYeildRateList =  getLastFundYeildRates4Test(codes);
    List<FundYeildRate> filteredFunds = filter(fundYeildRateList);
    FundInfos.Builder builder = FundInfos.newBuilder();
    FundInfo.Builder builderFI = FundInfo.newBuilder();
    for(FundYeildRate fundYeildRate: filteredFunds){

      builderFI.setNavunit(
          Math.toIntExact(MathUtil.getLongPriceFromDoubleOrig(fundYeildRate.getNavunit())));
      builderFI.setFundCode(fundYeildRate.getCode());
      builder.addFundInfo(builderFI);
    }
    return builder.build();
  }

  private List<FundYeildRate> filter(List<FundYeildRate> fundYeildRateList) {
    Map<String, FundYeildRate> fundYeildRateHashMap = new HashMap<>();
    for(FundYeildRate fundYeildRate: fundYeildRateList){
      if(!fundYeildRateHashMap.containsKey(fundYeildRate.getCode())){
        if(fundYeildRate.getNavunit() != null && fundYeildRate.getNavunit() != Double.MIN_VALUE){
          fundYeildRateHashMap.put(fundYeildRate.getCode(), fundYeildRate);
        }else{
          if(fundYeildRateHashMap.get(fundYeildRate.getCode()).getQuerydate() < fundYeildRate
              .getQuerydate() && fundYeildRate.getNavunit() != Double.MIN_VALUE ){
            fundYeildRateHashMap.put(fundYeildRate.getCode(), fundYeildRate);
          }
        }
      }
    }
    List<FundYeildRate> fundYeildRates = new ArrayList<>();
    for(Entry<String, FundYeildRate> entry: fundYeildRateHashMap.entrySet()){
      fundYeildRates.add(entry.getValue());
    }
    return fundYeildRates;
  }

  @Override
  public List<String> CollectItemsSyn(List<String> collectDatas) throws Exception {
    return null;
  }

  @Override
  public List<String> CollectItemsAsyn(List<String> collectDatas) throws Exception {
    return null;
  }



}

