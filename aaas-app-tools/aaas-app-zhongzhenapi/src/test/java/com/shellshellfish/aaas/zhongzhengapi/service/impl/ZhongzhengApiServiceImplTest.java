package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.shellshellfish.aaas.AaasZhongzhengApp;
import com.shellshellfish.aaas.common.enums.ZZKKStatusEnum;
import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZAplyCfmInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundNetInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundShareInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZRiskCmtResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZSellWltRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZTradeLimit;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltAplyInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZBankInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.FundRiskCheckLog;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBonusInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBuyResult;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 四月 - 24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AaasZhongzhengApp.class)
@ActiveProfiles("dev")
public class ZhongzhengApiServiceImplTest {

  @Autowired
  ZhongZhengApiService zhongZhengApiService;

  @Autowired
  MongoTemplate mongoTemplate;

  @Test
  public void getSupportBankList() throws Exception {
    List<ZZBankInfo> bankZhongZhenInfoList =
    zhongZhengApiService.getSupportBankList();
    StringBuilder sb = new StringBuilder();
    bankZhongZhenInfoList.forEach(bank->{
      sb.append(bank.getBankName()).append("|").append(bank.getBankSerial()).append("|").append(bank
          .getCapitalModel()).append("|").append(bank.getMoneyLimitDay()).append("|").append(bank
          .getMoneyLimitOne());
      System.out.println(sb.toString());
      sb.delete(0, sb.length());
    });
    Assert.assertTrue(!CollectionUtils.isEmpty(bankZhongZhenInfoList));


  }


  @Test
  public void sellFund() throws Exception {
    String sellNum = "1";
    String outsideTradeNo = "123"+ TradeUtil.getUTCTime();
    String trdAcco = "33600";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "362522198709220031";
    zhongZhengApiService.sellFund(sellNum,outsideTradeNo, trdAcco, fundCode, sellType, pid );

  }


  @Test
  public void getApplyResultByOutSideOrderNo() throws Exception {
    String sellNum = "1";
    String outsideTradeNo = "6222021560000015264606433902174";
    String trdAcco = "33600";
    String fundCode = "40009.OF";
    String sellType = "0";
    String pid = "362522198709220031";
    List<ApplyResult> applyResults =  zhongZhengApiService.getApplyResultByOutSideOrderNo
        (outsideTradeNo,   pid );
    applyResults.forEach(
        item->{
          System.out.println(item.getKkStat());
          System.out.println(item.getApplyDate());
          System.out.println(item.getApplySerial());
          System.out.println(item.getConfirmstat());
          System.out.println(item.getTradeConfirmShare());
        }
    );
  }

  @Test
  public void getApplyResultByTrdAcco() throws Exception {
    String trdAcco = "33586";
    String pid = "352230198703172130";
    List<ApplyResult> applyResults =  zhongZhengApiService.getApplyResults("","",  pid );
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
  public void getApplyResultByApplySerial() throws Exception {
    String pid = "362522198709220031";
    String trdAcco = "33600";
    String applySerial = "20180625000873";
    List<ApplyResult> applyResults =  zhongZhengApiService.getApplyResults("", applySerial, pid);
    applyResults.forEach(
        item->{
          System.out.println(item.getAccepTtime());
          System.out.println(item.getApplyDate());
          System.out.println(item.getApplySerial());
          System.out.println(item.getApplyShare());
          System.out.println(item.getApplySum());
          System.out.println(item.getApplyTime());
          System.out.println(item.getBankAcco());
          System.out.println(item.getBankName());
          System.out.println(item.getBankSerial());
          System.out.println(item.getBusinFlagStr());
          System.out.println(item.getCallingCode());
          System.out.println(item.getCapitalMode());
          System.out.println(item.getCommisionDiscount());
          System.out.println(item.getConfirmFlag());
          System.out.println(item.getConfirmstat());
          System.out.println(item.getFreezeMethod());
          System.out.println(item.getFundAcco());
          System.out.println(item.getFundName());
          System.out.println(item.getKkStat());
          System.out.println(item.getMelonMethod());
          System.out.println(item.getOccurBankAcco());
          System.out.println(item.getOriApplyDate());
          System.out.println(item.getOriginalApplySerial());
          System.out.println(item.getOutsideOrderNo());
          System.out.println(item.getRiskMatch());
          System.out.println(item.getShareType());
          System.out.println(item.getTradeAcco());
          System.out.println(item.getTradeConfirmShare());
          System.out.println(item.getTradeConfirmSum());
          System.out.println(item.getXyh());
        }
    );
  }

  @Test
  public void applyWallet() throws Exception {
    String trdAcco = "33600";
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
    String trdAcco = "33600";
    String sellNum = "4";
    String outsideTradeNo = "1234567890123";
    ZZSellWltRlt zzSellWltRlt = zhongZhengApiService.sellWallet(pid,sellNum,outsideTradeNo,trdAcco);
    System.out.println(zzSellWltRlt.getAcceptDate());
    System.out.println(zzSellWltRlt.getApplySerial());
    System.out.println(zzSellWltRlt.getOutsideOrderNo());
    System.out.println(zzSellWltRlt.getRequestDate());
  }

  @Test
  public void getWalletApplyInfo() throws Exception {

    String pid = "362522198709220031";
    List<ZZWltAplyInfo> applyResults =  zhongZhengApiService.getWalletApply(pid, null, null);
    applyResults.forEach(
        item -> {
          System.out.println(item.getAcceptTime());
          System.out.println(item.getApplyDate());
          System.out.println(item.getApplySerial());
          System.out.println(item.getApplyShare());
          System.out.println(item.getApplySum());
          System.out.println(item.getApplyTime());
          System.out.println(item.getBankAcco());
          System.out.println(item.getBankName());
          System.out.println(item.getBankSerial());
          System.out.println(item.getBusinFlagStr());
          System.out.println(item.getCallingCode());
          System.out.println(item.getCapitalMode());
          System.out.println(item.getCommisionDiscount());
          System.out.println(item.getConfirmFlag());
          System.out.println(item.getConfirmStat());
          System.out.println(item.getFreezeMethod());
          System.out.println(item.getFundAcco());
          System.out.println(item.getFundName());
          System.out.println(item.getKkStat());
          System.out.println(item.getMelonMethod());
          System.out.println(item.getOccurBankAcco());
          System.out.println(item.getOriApplydate());
          System.out.println(item.getOriginalApplySerial());
          System.out.println(item.getOutsideOrderno());
          System.out.println(item.getRiskMatch());
          System.out.println(item.getShareType());
          System.out.println(item.getTradeAcco());
          System.out.println(item.getTradeConfirmShare());
          System.out.println(item.getTradeConfirmSum());
          System.out.println(item.getXyh());
        }
    );
  }

  @Test
  public void getConfirmResult() throws Exception {
    String pid = "362522198709220031";
    String trdAcco = "33600";
    String applySerial = "20180516000606";
    List<ZZAplyCfmInfo> applyResults = zhongZhengApiService.getConfirmResult(trdAcco,applySerial,"", pid);
    applyResults.forEach(
        item->{
          System.out.println(item.getApplydate());
          System.out.println(item.getApplyserial());
          System.out.println(item.getApplyshare());
          System.out.println(item.getApplysum());
          System.out.println(item.getApplytime());
          System.out.println(item.getBankacco());
          System.out.println(item.getBankname());
          System.out.println(item.getBankSerial());
          System.out.println(item.getBusinflagStr());
          System.out.println(item.getCallingcode());
          System.out.println(item.getCapitalmode());
          System.out.println(item.getConfirmdate());
          System.out.println(item.getConfirmflag());
          System.out.println(item.getConfirmstat());
          System.out.println(item.getFreezemethod());
          System.out.println(item.getFundacco());
          System.out.println(item.getFundname());
          System.out.println(item.getFundCode());
          System.out.println(item.getMelonmethod());
          System.out.println(item.getOccurbankacco());
          System.out.println(item.getOriapplydate());
          System.out.println(item.getOriginalapplyserial());
          System.out.println(item.getOutsideorderno());
          System.out.println(item.getSharetype());
          System.out.println(item.getTradeacco());
          System.out.println(item.getTradeconfirmshare());
          System.out.println(item.getTradeconfirmsum());
          System.out.println(item.getXyh());
        }
    );
  }

  @Test
  public void sellAll() throws Exception {

    String pid = "352230198703172130";
    String trdAcco = "33586";
    List<ZZAplyCfmInfo> applyResults = zhongZhengApiService.getConfirmResult(trdAcco,"","", pid);
    Map<String, BigDecimal> allBuyInfo = new HashMap<>();

    applyResults.forEach(
        item ->{
          System.out.println("===================");
          System.out.println(item.getApplydate());
          System.out.println(item.getApplyserial());
          System.out.println(item.getApplyshare());
          System.out.println(item.getApplysum());
          System.out.println(item.getApplytime());
          System.out.println(item.getBankacco());
          System.out.println(item.getBankname());
          System.out.println(item.getBankSerial());
          System.out.println(item.getBusinflagStr());
          System.out.println(item.getCallingcode());
          System.out.println(item.getCapitalmode());
          System.out.println(item.getConfirmdate());
          System.out.println(item.getConfirmflag());
          System.out.println(item.getConfirmstat());
          System.out.println(item.getFreezemethod());
          System.out.println(item.getFundacco());
          System.out.println(item.getFundname());
          System.out.println(item.getFundCode());
          System.out.println(item.getMelonmethod());
          System.out.println(item.getOccurbankacco());
          System.out.println(item.getOriapplydate());
          System.out.println(item.getOriginalapplyserial());
          System.out.println(item.getOutsideorderno());
          System.out.println(item.getSharetype());
          System.out.println(item.getTradeacco());
          System.out.println(item.getTradeconfirmshare());
          System.out.println(item.getTradeconfirmsum());
          System.out.println(item.getXyh());
          if(item.getConfirmflag().equals("1")) {
            if(!allBuyInfo.containsKey(item.getFundCode())){
              if(item.getBusinflagStr().equals("赎回确认")){
                allBuyInfo.put(item.getFundCode(), new BigDecimal("-"+item.getTradeconfirmshare()));
              }else{
                allBuyInfo.put(item.getFundCode(), new BigDecimal(item.getTradeconfirmshare()));
              }

            }else{
              BigDecimal origin = allBuyInfo.get(item.getFundCode());
              System.out.println(item.getTradeconfirmshare());
              BigDecimal result = BigDecimal.ZERO;
              if(item.getBusinflagStr().equals("赎回确认")){
                result = origin.add(new BigDecimal("-"+item.getTradeconfirmshare()));
                System.out.println(String.format("origin:%s get:%s result:%s", origin, item
                    .getTradeconfirmshare(), result));

              }else{
                result = origin.add(new BigDecimal(item.getTradeconfirmshare()));
                System.out.println(String.format("origin:%s get:%s result:%s", origin, item
                    .getTradeconfirmshare(), result));
              }
              allBuyInfo.put(item.getFundCode(), result);

            }
          }
        }
    );
    allBuyInfo.forEach(
        (key, value) ->{
          //call sell interface
          System.out.println(String.format("fundCode:%s fundQuantity:%s", key, value));
        }
    );
    List<ZZFundShareInfo> fundShareInfos = zhongZhengApiService.getFundShare(pid);
    fundShareInfos.forEach(
        item->{
          System.out.println(String.format("fundCode:%s tradeAcco:%s fundQuantity:%s", item
                  .getFundCode(), item.getTradeAcco(), item.getUsableRemainShare()));
        }
    );
  }


  @Test
  public void getFundShare() throws Exception {
    String pid = "652901197006205729";
    List<ZZFundShareInfo> fundShareInfos = zhongZhengApiService.getFundShare(pid);
    fundShareInfos.forEach(
        item->{
          System.out.println(String.format("fundCode:%s tradeAcco:%s fundQuantity:%s", item
              .getFundCode(), item.getTradeAcco(), item.getCurrentRemainShare()));
        }
    );
  }


  @Test
  public void sellAllFundShare() throws Exception {
    String pid = "352230198703172130";
    String tradeAcco = "33586";
    String outsideOrderNumBase = "SellAll"+TradeUtil.getUTCTime();
    int idx = 0;
    List<ZZFundShareInfo> fundShareInfos = zhongZhengApiService.getFundShare(pid);
    for(ZZFundShareInfo zzFundShareInfo: fundShareInfos){
      System.out.println(String.format("fundCode:%s tradeAcco:%s fundQuantity:%s", zzFundShareInfo
          .getFundCode(), zzFundShareInfo.getTradeAcco(), zzFundShareInfo.getUsableRemainShare()));

      try {
        SellResult sellResult = zhongZhengApiService.sellFund(zzFundShareInfo.getUsableRemainShare(),
            outsideOrderNumBase+idx++,
            tradeAcco,zzFundShareInfo.getFundCode(),"0", pid);
        System.out.println(sellResult.getAcceptDate());
        System.out.println(sellResult.getApplySerial());
        System.out.println(sellResult.getOutsideOrderNo());
        System.out.println(sellResult.getRequestDate());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  @Test
  public void getAllFundInfo() throws Exception {
    List<ZZFundInfo> zzFundInfos = zhongZhengApiService.getAllFundInfo();
    StringBuilder sb = new StringBuilder();
    zzFundInfos.forEach(
        item->{

          sb.append(item.getFundCode()).append("|").append(item.getDeclareState()).append("|")
              .append(item.getFundRiskLevel()).append("|");
          try {
            List<ZZTradeLimit> zzTradeLimits = zhongZhengApiService.getTradeLimit(item.getFundCode
                ());
            sb.append(zzTradeLimits.get(0).getMinValue());
          } catch (Exception e) {
            e.printStackTrace();
          }
          sb.append("\n");
        }
    );
    System.out.println(sb.toString());
  }

  @Test
  public void buyFund() throws Exception {
    String pid = "362522198709220031";
    String trdAcco = "33600";
    String fundCode = "110022";
    String applyNum = "10";
    String outsideTradeNo = "1234567890124";
    ZZBuyResult zzBuyResult = zhongZhengApiService.buyFund(pid,trdAcco,fundCode,applyNum,
        outsideTradeNo);
    System.out.println(zzBuyResult.getApplySerial());
    System.out.println(zzBuyResult.getCapitalModel());
    System.out.println(zzBuyResult.getKkStat());
    System.out.println(zzBuyResult.getOutsideOrderNo());
    System.out.println(zzBuyResult.getRequestDate());
  }


  @Test
  public void getBonusInfo() throws Exception {
    String pid = "362522198709220031"; //362522198709220031
    String fundCode = "000248";
    String startDate = "20180101";
    List<ZZBonusInfo> zzBonusInfos = zhongZhengApiService.getBonusInfo(pid,fundCode,startDate);
    zzBonusInfos.forEach(
        item->{
          System.out.println(item.getConfirmDate());
          System.out.println(item.getEnrollDate());
          System.out.println(item.getEnrollShare());
          System.out.println(item.getFactBonusSum());
          System.out.println(item.getFreezeShare());
          System.out.println(item.getFreezeSum());
          System.out.println(item.getFundAcco());
          System.out.println(item.getFundCode());
          System.out.println(item.getFundName());
          System.out.println(item.getIdentityNo());
          System.out.println(item.getIncomeTax());
          System.out.println(item.getMelonCutting());
          System.out.println(item.getMelonMethod());
          System.out.println(item.getNetValue());
          System.out.println(item.getOccurBankAcco());
          System.out.println(item.getOccurBankNo());
          System.out.println(item.getPoundage());
          System.out.println(item.getSharePerBonus());
          System.out.println(item.getShareType());
          System.out.println(item.getSumperBonus());
          System.out.println(item.getTaConfirmSerial());
          System.out.println(item.getTradeAcco());
        }
    );
  }

  @Test
  public void checkFundRiskInBuyOp() throws Exception {
    String fileName = "zzfundrisk.txt";
    String pid = "362522198709220031";
    String trdAcco = "33600";

    String applyNum = "2";
    String outsideTradeNo = "";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());


//    File file = new File(getClass().getResource(fileName).getFile());
    FileInputStream readerHelp = new FileInputStream(file);

    Reader reader = new InputStreamReader(readerHelp, "utf-8");
    BufferedReader bufferedReader = new BufferedReader(reader);
    String line = bufferedReader.readLine();
    List<FundRiskCheckLog> fundRiskCheckLogs = new ArrayList<>();
    if(line.contains("FundCode")){

      bufferedReader.lines().forEach(lineReading -> {
        String fundCode = lineReading.split("[ ]+")[0];
        int fundRisk = Integer.parseInt(lineReading.split("[ ]+")[1]);
            FundRiskCheckLog fundRiskCheckLog = new FundRiskCheckLog();
            fundRiskCheckLog.setFundCode(fundCode);
            fundRiskCheckLog.setFundRisk(fundRisk);
            fundRiskCheckLogs.add(fundRiskCheckLog);
        Query query = new Query();
        query.addCriteria(new Criteria("fund_code").is(fundCode));
        List<FundRiskCheckLog> fundRiskCheckLogsInDB = mongoTemplate.find(query, FundRiskCheckLog
            .class);
        if(CollectionUtils.isEmpty(fundRiskCheckLogsInDB)){
          mongoTemplate.save(fundRiskCheckLog);
        }else{
          System.out.println(String.format("FundCode:%s already exists" , fundCode));
        }

          }
      );
    }
    //now begin to buy those fundcode which is not bought yet
    Query query = new Query();
    for(FundRiskCheckLog item: fundRiskCheckLogs){
          query = new Query();
          query.addCriteria(new Criteria("fund_code").is(item.getFundCode()));
          List<FundRiskCheckLog> fundRiskCheckLogsInDB = mongoTemplate.find(query, FundRiskCheckLog
              .class);
          query = null;
          if(CollectionUtils.isEmpty(fundRiskCheckLogsInDB)){
            try {
              throw  new Exception(String.format("there is no record for fundCode:%s in ssfzzapi",
                  item.getFundCode()));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }else{
            if(StringUtils.isEmpty(fundRiskCheckLogsInDB.get(0).getApplySerial())){
              System.out.print(String.format("now begin to buy fundCode:%s",
                  fundRiskCheckLogsInDB.get(0).getFundCode()));
              outsideTradeNo = ""+TradeUtil.getUTCTime();
              ZZBuyResult zzBuyResult = null;
              ZZRiskCmtResult zzRiskCmtResult = null;
              try{
                zzRiskCmtResult = zhongZhengApiService.commitRiskLevel(pid, item.getFundRisk()+1);
                zzBuyResult = zhongZhengApiService.buyFund(pid,trdAcco,item.getFundCode(),applyNum,
                    outsideTradeNo );
              }catch (Exception ex){
                ex.printStackTrace();
                if(ex.getMessage().contains(":") && ex.getMessage().contains("申请值太小")){
                  outsideTradeNo = ""+TradeUtil.getUTCTime();
                  zzBuyResult = zhongZhengApiService.buyFund(pid,trdAcco,item.getFundCode(),"10",
                      outsideTradeNo );
                }
              }
              if(zzBuyResult == null){
                continue;
              }
              System.out.println(zzBuyResult.getApplySerial());
              fundRiskCheckLogsInDB.get(0).setApplySerial(zzBuyResult.getApplySerial());
              System.out.println(zzBuyResult.getCapitalModel());
              System.out.println(zzBuyResult.getKkStat());
              fundRiskCheckLogsInDB.get(0).setStatus(zzBuyResult.getKkStat());
              if(zzBuyResult.getKkStat().equals(""+ZZKKStatusEnum.KKSUCCESS.getStatus())){
                fundRiskCheckLogsInDB.get(0).setCanBuy(Boolean.TRUE);
              }
              System.out.println(zzBuyResult.getOutsideOrderNo());
              fundRiskCheckLogsInDB.get(0).setOutsideOrderNo(zzBuyResult.getOutsideOrderNo());
              System.out.println(zzBuyResult.getRequestDate());
              mongoTemplate.save(fundRiskCheckLogsInDB.get(0));

            }
          }
        }

    System.out.println(line);
  }

  @Test
  public void getFundInfos() throws Exception {
    List<ZZFundNetInfo> zzFundNetInfos = zhongZhengApiService.getAllNet("003474.OF", 3, 3);
    zzFundNetInfos.forEach(
        item->{
          System.out.println(item.getAccumNet());
          System.out.println(item.getChngPct());
          System.out.println(item.getFundCode());
          System.out.println(item.getTradeDate());
          System.out.println(item.getUnitNet());
        }
    );
  }
}
