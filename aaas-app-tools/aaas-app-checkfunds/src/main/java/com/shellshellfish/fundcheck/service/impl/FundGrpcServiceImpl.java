package com.shellshellfish.fundcheck.service.impl;

import com.shellshellfish.aaas.tools.checkfunds.DailyFundsQuery;
import com.shellshellfish.aaas.tools.checkfunds.FundCheckerServiceGrpc.FundCheckerServiceBlockingStub;
import com.shellshellfish.fundcheck.service.FundGrpcService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 13
 */
@Service
public class FundGrpcServiceImpl implements FundGrpcService {
  Logger logger = LoggerFactory.getLogger(FundGrpcServiceImpl.class);

  @Autowired
  FundCheckerServiceBlockingStub fundCheckRpcServiceBlockingStub;

  @Override
  public boolean checkFunds(List<String> fundCodes, String beginDate, String endDate,
      int tableIdx) {

      for(String codeItem: fundCodes){
        List<String> code = new ArrayList<>();
        code.add(codeItem);
        DailyFundsQuery.Builder dfqBuilder = DailyFundsQuery.newBuilder();
        dfqBuilder.addCodes(codeItem);
        dfqBuilder.setCheckTableIdx(tableIdx);
        dfqBuilder.setNavLatestDateStart(beginDate);
        dfqBuilder.setNavLatestDateEnd(endDate);
        try{
        fundCheckRpcServiceBlockingStub.checkFunds(dfqBuilder.build());
        }catch (Exception ex){
          logger.error("Exception:", ex);
        }
      }




    return true;
  }
}
