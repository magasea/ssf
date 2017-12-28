package com.shellshellfish.aaas.datacollection.client.service;


import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery.Builder;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceStub;
import com.shellshellfish.aaas.datacollection.client.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.client.model.vo.FundsQuery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {



  Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);

  private DataCollectionServiceBlockingStub blockingStub;
  private DataCollectionServiceStub asyncStub;


  @Autowired
  ManagedChannelBuilder grpcChannelBuilder;

  @Autowired
  ManagedChannel managedChannel;


  @PostConstruct
  public void init(){
    blockingStub = DataCollectionServiceGrpc.newBlockingStub(managedChannel);
    asyncStub = DataCollectionServiceGrpc.newStub(managedChannel);
  }

  public void shutdown() throws InterruptedException {
    managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }



  @Override
  public List<DailyFunds> getDailyFunds(FundsQuery fundsQuery) throws Exception {
    Builder queryBuilder = DailyFundsQuery.newBuilder();
    queryBuilder.setNavLatestDateStart(fundsQuery.getNavLatestDateStart());
    queryBuilder.setNavLatestDateEnd(fundsQuery.getNavLatestDateEnd());

    for(String code: fundsQuery.getCodes()){
      queryBuilder.addCodes( code);
    }
    java.util.List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFundsList =
    DataCollectionServiceGrpc
    .newFutureStub(managedChannel).getFundDataOfDay(queryBuilder.build()
    ).get().getDailyFundsList();
    List<DailyFunds> dailyFundsResult = new ArrayList<>();
    for(com.shellshellfish.aaas.datacollect.DailyFunds item : dailyFundsList){
      DailyFunds dailyFunds = new DailyFunds();
      BeanUtils.copyProperties(item, dailyFunds);
      dailyFundsResult.add(dailyFunds);
    }
    return dailyFundsResult;
  }
}
