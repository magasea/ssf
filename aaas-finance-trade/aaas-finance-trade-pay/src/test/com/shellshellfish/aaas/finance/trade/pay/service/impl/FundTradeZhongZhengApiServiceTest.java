package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.PayServiceApplication;
import com.shellshellfish.aaas.finance.trade.pay.model.FundNetZZInfo;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  Logger logger = LoggerFactory.getLogger(FundTradeZhongZhengApiServiceTest.class);

  @Test
  public void getFundNets() throws Exception {
    List<FundNetZZInfo> fundNetZZInfos =  fundTradeApiService.getFundNets("001987.OF", -1, 2);
    for(FundNetZZInfo fundNetZZInfo: fundNetZZInfos){
      System.out.println(fundNetZZInfo.getTradeDate());
    }
  }

  @Test
  public void getApplyResultByOutsideOrderNo() throws Exception {

    String personId = "411327198710181169";
    String openId = TradeUtil.getZZOpenId(personId);
    String outsideOrderno = "621709900000000015169683005511231";
    ApplyResult applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(openId,
        outsideOrderno);
    System.out.println(applyResult.getConfirmflag());
  }

  @Test
  public void testGetAllFundsInfo() throws Exception {
    List<String> allFunds = fundTradeApiService.getAllFundsInfo();
    allFunds.forEach(System.out::println);
    logger.debug("allFunds: {}", allFunds);
  }

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Test
  public void commitRisk() throws Exception {
    String openId = TradeUtil.getZZOpenId("362522198709220031");
    fundTradeApiService.commitRisk(openId, 4);
  }

  @Test
  public void getAllFundInfo() throws Exception {
    String openId = TradeUtil.getZZOpenId("362522198709220031");
    fundTradeApiService.getAllFundsInfo();
  }

}