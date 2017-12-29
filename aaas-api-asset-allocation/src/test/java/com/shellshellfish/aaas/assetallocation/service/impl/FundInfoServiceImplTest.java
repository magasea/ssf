package com.shellshellfish.aaas.assetallocation.service.impl;

import static org.junit.Assert.*;

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
@ActiveProfiles(profiles="dev")
public class FundInfoServiceImplTest {
  @Autowired
  FundInfoService fundInfoService;

  @Test
  public void getDailyFunds() throws Exception {
    DailyFundsQuery.Builder builder = DailyFundsQuery.newBuilder();
    builder.setNavLatestDateStart("2017-11-30");
    builder.setNavLatestDateEnd("2017-12-30");
    builder.addCodes("000312.OF");
    List<DailyFunds> dailyFundsList = fundInfoService.getDailyFunds(builder.build());
    for(DailyFunds dailyFunds: dailyFundsList){
      System.out.println(dailyFunds.getFname());
    }
  }

}