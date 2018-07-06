package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.PayServiceApplication;
import com.shellshellfish.aaas.finance.trade.pay.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.ConfirmResult;
import com.shellshellfish.aaas.finance.trade.pay.model.FundNetZZInfo;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import java.math.BigDecimal;
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



  @Test
  public void getSupportedBank() throws Exception {

    for(BankZhongZhenInfo bankZhongZhenInfo: fundTradeApiService.getSupportedBank()){
      System.out.println(bankZhongZhenInfo.getBankName());
      System.out.println(bankZhongZhenInfo.getBankSerial());
      System.out.println(bankZhongZhenInfo.getCapitalModel());
      System.out.println(bankZhongZhenInfo.getMoneyLimitDay());
      System.out.println(bankZhongZhenInfo.getMoneyLimitOne());
    };
  }

  Logger logger = LoggerFactory.getLogger(FundTradeZhongZhengApiServiceTest.class);

  @Test
  public void getFundNets() throws Exception {
    List<FundNetZZInfo> fundNetZZInfos =  fundTradeApiService.getFundNets("001987.OF", -1, 2);
    for(FundNetZZInfo fundNetZZInfo: fundNetZZInfos){
      System.out.println(fundNetZZInfo.getTradeDate());
      System.out.println(fundNetZZInfo.getAccumNet());
      System.out.println(fundNetZZInfo.getChngPct());
      System.out.println(fundNetZZInfo.getFundCode());
      System.out.println(fundNetZZInfo.getTenThouUnitIncm());
      System.out.println(fundNetZZInfo.getUnitNet());
    }
  }

  @Test
  public void getApplyResultByOutsideOrderNo() throws Exception {

    String personId = "362522198709220031";
    String openId = TradeUtil.getZZOpenId(personId);
    String outsideOrderno = "test123456789012342";
    ApplyResult applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(openId,
        outsideOrderno);
    System.out.println(applyResult.getConfirmflag());
  }

  @Test
  public void getApplyResultByApplySerial() throws Exception {

    String personId = "362522198709220031";
    String openId = TradeUtil.getZZOpenId(personId);
    String applySerial = "20180315000095";
    ApplyResult applyResult = fundTradeApiService.getApplyResultByApplySerial(openId,
        applySerial);
    System.out.println(applyResult.getConfirmflag());
  }

  @Test
  public void getConfirmResultByApplySerial() throws Exception {

    String personId = "362522198709220031";
    String openId = TradeUtil.getZZOpenId(personId);
    String applySerial = "20180315000095";
    List<ConfirmResult> confirmResults = fundTradeApiService.getConfirmResultsBySerial(openId,
        applySerial);
    System.out.println(confirmResults.get(0).getConfirmflag());
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

  @Test
  public void sellFund() throws Exception {
    String sellNum = "1";
    String outsideTradeNo = "123"+ TradeUtil.getUTCTime();
    String trdAcco = "33653";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "";
    String userOpenId = TradeUtil.getZZOpenId(pid);
    fundTradeApiService.sellFund(userOpenId, BigDecimal.ONE, outsideTradeNo, trdAcco, fundCode);
  }

  @Test
  public void buyFund() throws  Exception{
    BigDecimal applySum = new BigDecimal(10);
    String trdAcco = "33600";
    String pid = "362522198709220031";
    String outsideOrderNo = "test123456789012342";
    String fundCode = "003474";
    String openId = TradeUtil.getZZOpenId(pid);
    BuyFundResult buyFundResult = fundTradeApiService.buyFund( openId,  trdAcco,  applySum,
        outsideOrderNo, fundCode);

    System.out.println(buyFundResult.getApplySerial());
    System.out.println(buyFundResult.getCapitalMode());
    System.out.println(buyFundResult.getRequestDate());
    System.out.println(buyFundResult.getOutsideOrderNo());
    System.out.println(buyFundResult.getConfirmdate());
    System.out.println(buyFundResult.getKkstat());
  }
}