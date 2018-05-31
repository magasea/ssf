package com.shellshellfish.aaas.finance.trade.order.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.shellshellfish.aaas.finance.trade.order.util.BusinFlag.BUY_FUND;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FinanceInfoServiceImplTest {


    @Autowired
    private FundInfoZhongZhengApiService fundInfoService;


    @Test
    public void testGetRateOfBuyFund() throws Exception {
     /*   String fundCode = "400016";

    BigDecimal rate = fundInfoService.getRateOfBuyFund(fundCode, BUY_FUND.getCode());
        System.out.println(rate);
        Assert.assertTrue(rate.compareTo(BigDecimal.ZERO) > 0);*/
    }

}
