package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.google.gson.GsonBuilder;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2018- 四月 - 24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZhongzhengApiServiceImpl.class)
@ActiveProfiles("dev")
public class ZhongzhengApiServiceImplTest {

  @Autowired
  ZhongZhengApiService zhongZhengApiService;

  @Test
  public void getSupportBankList() throws Exception {
    List<BankZhongZhenInfo> bankZhongZhenInfoList =
    zhongZhengApiService.getSupportBankList();
    StringBuilder sb = new StringBuilder();
    bankZhongZhenInfoList.forEach(bank->{
      sb.append(bank.getBankName()).append("|").append(bank.getBankSerial());
      System.out.println(sb.toString());
      sb.delete(0, sb.length());
    });
    Assert.assertTrue(!CollectionUtils.isEmpty(bankZhongZhenInfoList));


  }


  @Test
  public void sellFund() throws Exception {
    String sellNum = "1";
    String outsideTradeNo = "123"+ TradeUtil.getUTCTime();
    String trdAcco = "33653";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "522101197402150413";
    zhongZhengApiService.sellFund(sellNum,outsideTradeNo, trdAcco, fundCode, sellType, pid );

  }
}
