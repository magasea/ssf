package com.shellshellfish.aaas.finance.trade.order.service.impl;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 一月 - 22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@Ignore
public class CheckFundsOrderJobServiceTest {

  @Autowired
  CheckFundsOrderJobService checkFundsOrderJobService;

  @Test
  public void executeSampleJob() throws Exception {
      checkFundsOrderJobService.executeSampleJob();
  }

}