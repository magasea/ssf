package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.service.FundInfoService;
import com.shellshellfish.aaas.datacollect.DailyFunds;
import com.shellshellfish.aaas.datacollect.DailyFundsCollection;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceFutureStub;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 23
 */
@Service
public class FundInfoServiceImpl implements FundInfoService {

  DataCollectionServiceFutureStub dataCollectionServiceFutureStub;

  @Autowired
  ManagedChannel managedDFChannel;

  @PostConstruct
  public void init(){
    dataCollectionServiceFutureStub = DataCollectionServiceGrpc.newFutureStub(managedDFChannel);
  }

  @Override
  public List<DailyFunds> getDailyFunds(DailyFundsQuery fundsQuery) throws Exception {

    List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFundsList =
            dataCollectionServiceFutureStub.getFundDataOfDay(fundsQuery).get().getDailyFundsList();
    return dailyFundsList;
  }
}
