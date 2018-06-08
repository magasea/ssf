package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.finance.trade.pay.PayServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by developer4 on 2018- 六月 - 07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayServiceApplication.class)
@ActiveProfiles(profiles = "dev")
public class PayServiceImplTest {

  @Autowired
  PayServiceImpl payService;

  @Test
  public void sellPercentProd() {
  }

  @Test
  public void getMoneyCodeNavAdjNow() throws Exception {
    System.out.println(payService.getMoneyCodeNavAdjNow("003474.OF"));
  }
}
