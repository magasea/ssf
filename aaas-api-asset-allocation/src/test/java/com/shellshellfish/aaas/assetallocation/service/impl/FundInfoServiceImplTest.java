package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.service.FundInfoService;
import com.shellshellfish.aaas.datacollect.DailyFunds;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2017- 十二月 - 26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="pretest")
public class FundInfoServiceImplTest {
  @Autowired
  FundInfoService fundInfoService;

  @Test
  public void getDailyFunds() throws Exception {
    DailyFundsQuery.Builder builder = DailyFundsQuery.newBuilder();
    builder.setNavLatestDateStart("2018-07-01");
    builder.setNavLatestDateEnd("2018-07-03");
//    builder.addCodes("300SH_5_CSI_5");
//    builder.addCodes("001987.OF");
    builder.addCodes("040036.OF");
    List<DailyFunds> dailyFundsList = fundInfoService.getDailyFunds(builder.build());
    for(DailyFunds dailyFunds: dailyFundsList){
      System.out.println(dailyFunds.getNavadj());
    }
  }

}