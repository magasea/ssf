package com.shellshellfish.aaas.datacollection.server.service;


import com.shellshellfish.aaas.datacollect.DailyFundsCollection;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceImplBase;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceStub;
import com.shellshellfish.aaas.datacollection.server.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.server.repositories.DailyFundsRepository;
import com.shellshellfish.aaas.datacollection.server.util.DateUtil;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class DataCollectionServiceImpl extends DataCollectionServiceImplBase {


  Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);


  private Random random = new Random();

  @Autowired
  DailyFundsRepository dailyFundsRepository;

  @Override
  public void getFundDataOfDay(DailyFundsQuery request, StreamObserver<DailyFundsCollection>
      responseObserver){
    String navLatestDate = request.getNavLatestDate();
    List<String> code = request.getCodesList();
    List<DailyFunds> dailyFundsList = null;
    try {
      dailyFundsList = dailyFundsRepository.findByNavLatestDateBetweenAndCodeIsIn
          (DateUtil.getDateLongValOneDayBefore(navLatestDate), DateUtil.getDateLongVal(navLatestDate), code);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFundsListProto = new ArrayList<>();
    for(DailyFunds dailyFunds : dailyFundsList){
      dailyFundsListProto.add(
          com.shellshellfish.aaas.datacollect.DailyFunds
              .newBuilder().setBmIndexChgPct(dailyFunds.getBminDexChgPct())
          .setCode(dailyFunds
          .getCode()).setFundScale(dailyFunds.getFundScale()).setId(dailyFunds.getId())
          .setMillionRevenue(dailyFunds.getMillionRevenue()).setNavAccum(dailyFunds.getNavAccum()
      ).setNavAdj(dailyFunds.getNavAdj()).setNavLatestDate(dailyFunds.getNavLatestDate())
          .setNavUnit(dailyFunds.getNavUnit()).setYieldOf7Days(dailyFunds.getYieldOf7Days())
          .build());
    }
    final DailyFundsCollection.Builder builder = DailyFundsCollection.newBuilder()
        .addAllDailyFunds(dailyFundsListProto);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

}
