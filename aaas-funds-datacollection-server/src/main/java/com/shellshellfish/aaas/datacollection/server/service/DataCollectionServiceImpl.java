package com.shellshellfish.aaas.datacollection.server.service;


import com.shellshellfish.aaas.common.utils.DataCollectorUtil;
import com.shellshellfish.aaas.datacollect.DailyFunds.Builder;
import com.shellshellfish.aaas.datacollect.DailyFundsCollection;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceImplBase;
import com.shellshellfish.aaas.datacollection.server.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.server.model.FundResources;
import com.shellshellfish.aaas.datacollection.server.repositories.DailyFundsRepository;

import com.shellshellfish.aaas.datacollection.server.util.DateUtil;
import io.grpc.stub.StreamObserver;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;


public class DataCollectionServiceImpl extends DataCollectionServiceImplBase {


  Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);


  private Random random = new Random();

  @Autowired
  DailyFundsRepository dailyFundsRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public void getFundDataOfDay(DailyFundsQuery request, StreamObserver<DailyFundsCollection>
      responseObserver){
    String navLatestDate = request.getNavLatestDate();
    List<String> codes = request.getCodesList();
    List<DailyFunds> dailyFundsList = null;
    try {
      Query query = new Query();
      query.addCriteria(Criteria.where("navlatestdate").is(DateUtil.getDateLongVal(navLatestDate)
          /1000).and("code").in(codes));
      dailyFundsList  = mongoTemplate.find(query, DailyFunds.class, "dailyfunds");
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
        builderDailyFunds.setFirstInvestType(fundResources.get(0).getFirstinvesttype());
        builderDailyFunds.setSecondInvestType(fundResources.get(0).getSecondinvesttype());
      }
      dailyFundsListProto.add(builderDailyFunds.build());
      builderDailyFunds.clear();
    }
    final DailyFundsCollection.Builder builder = DailyFundsCollection.newBuilder()
        .addAllDailyFunds(dailyFundsListProto);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

}
