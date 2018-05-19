package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
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
    String trdAcco = "33586";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "362522198709220031";
    zhongZhengApiService.sellFund(sellNum,outsideTradeNo, trdAcco, fundCode, sellType, pid );

  }


  @Test
  public void getApplyResultByOutSideOrderNo() throws Exception {
    String sellNum = "1";
    String outsideTradeNo = "123"+ TradeUtil.getUTCTime();
    String trdAcco = "33586";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "362522198709220031";
    zhongZhengApiService.getApplyResultByOutSideOrderNo(outsideTradeNo, trdAcco,   pid );

  }

  @Test
  public void getApplyResultByTrdAcco() throws Exception {
    String trdAcco = "33586";
    String pid = "362522198709220031";
    List<ApplyResult> applyResults =  zhongZhengApiService.getApplyResultByTrdAcco(trdAcco,   pid );
    applyResults.forEach(
        item->{
          System.out.println(item.getApplyDate());
          System.out.println(item.getApplySerial());
          System.out.println(item.getConfirmstat());
          System.out.println(item.getTradeConfirmShare());
        }
    );
  }

  @Test
  public void applyWallet() throws Exception {
    String trdAcco = "33586";
    String pid = "362522198709220031";
    WalletApplyResult applyResults =  zhongZhengApiService.applyWallet(trdAcco, pid, "30",
        "2234567890123456" );
    System.out.println(applyResults.getApplySerial());
    System.out.println(applyResults.getCapitalModel());
    System.out.println(applyResults.getOutsideOrderNo());
    System.out.println(applyResults.getRequestDate());


  }

  @Test
  public void getWalletInfo() throws Exception {

    String pid = "362522198709220031";
    List<ZZWltInfoRlt> applyResults =  zhongZhengApiService.getWltInfo(pid);
    applyResults.forEach(
        item -> {
          System.out.println(item.getDayInc());
          System.out.println(item.getDeClareState());
          System.out.println(item.getFundName());
          System.out.println(item.getFundRiskLevel());
          System.out.println(item.getFundState());
          System.out.println(item.getFundType());
          System.out.println(item.getHfinComeRatio());
          System.out.println(item.getIncomeRatio());
          System.out.println(item.getMinShare());
          System.out.println(item.getNavDate());
          System.out.println(item.getPernetValue());
          System.out.println(item.getShareType());
          System.out.println(item.getSubScribeState());
          System.out.println(item.getTaCode());
          System.out.println(item.getTotalNetValue());
          System.out.println(item.getTransFlag());
          System.out.println(item.getTrendState());
          System.out.println(item.getValuagrState());
          System.out.println(item.getWithDrawState());
        }
    );
  }

  @Test
  public void sellWallet() throws Exception {

    String pid = "362522198709220031";
    String trdAcco = "33586";

  }


}
