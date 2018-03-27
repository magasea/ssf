package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenwei on 2018- 一月 - 03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class CheckFundsBuyJobServiceTest {


  @Autowired
  private CheckFundsTradeJobService jobService;
  @Test
  public void executeSampleJob() throws Exception {
    jobService.executeSampleJob();
  }

}