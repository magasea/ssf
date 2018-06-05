package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.service.DataCollectionService;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by developer4 on 2018- 六月 - 04
 */
@Service
public class DataCollectionServiceImpl  implements DataCollectionService {

  @Autowired
  ManagedChannel managedDccChannel;

  DataCollectionServiceBlockingStub dataCollectionServiceBlockingStub;

  @PostConstruct
  void init(){
    dataCollectionServiceBlockingStub = DataCollectionServiceGrpc.newBlockingStub(managedDccChannel);
  }

  @Override
  public List<DCDailyFunds> getFundDataOfDay(List<String> codes, String startOfDay,
      String endOfDay) {
    DailyFundsQuery.Builder dfqBuilder = DailyFundsQuery.newBuilder();
    codes.forEach(
        item->dfqBuilder.addCodes(item)
    );
    dfqBuilder.setNavLatestDateStart(startOfDay);
    dfqBuilder.setNavLatestDateEnd(endOfDay);
    List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFunds =
     dataCollectionServiceBlockingStub.getFundDataOfDay(dfqBuilder.build()).getDailyFundsList();
    List<DCDailyFunds> dcDailyFunds = new ArrayList<>();
    dailyFunds.forEach(
        originItem->{
          DCDailyFunds dcDailyFunds1 = new DCDailyFunds();
          MyBeanUtils.mapEntityIntoDTO(originItem, dcDailyFunds1);
          dcDailyFunds.add(dcDailyFunds1);
        }
    );
    return dcDailyFunds;
  }
}
