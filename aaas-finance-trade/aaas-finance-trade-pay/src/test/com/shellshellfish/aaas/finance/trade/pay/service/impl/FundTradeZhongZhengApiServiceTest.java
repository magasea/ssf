package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.PayServiceApplication;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 一月 - 18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayServiceApplication.class)
@ActiveProfiles(profiles = "dev")
public class FundTradeZhongZhengApiServiceTest {

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Test
  public void commitRisk() throws Exception {
    String openId = TradeUtil.getZZOpenId("352230198703172130");
    fundTradeApiService.commitRisk(openId, 3);
  }

}