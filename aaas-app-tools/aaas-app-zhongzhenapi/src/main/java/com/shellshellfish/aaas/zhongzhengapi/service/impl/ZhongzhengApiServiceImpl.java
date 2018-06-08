package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZAplyCfmInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZDiscountInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundShareInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZRiskCmtResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZSellWltRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZTradeLimit;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltAplyInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZBankInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.CancelTradeResult;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBonusInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBuyResult;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralResp;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralRespWithListData;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import com.shellshellfish.aaas.zhongzhengapi.util.ZhongZhengAPIConstants;
import com.shellshellfish.aaas.zhongzhengapi.util.ZhongZhengAPIUtils;
import java.util.List;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ZhongzhengApiServiceImpl extends AbstractZhongzhengApiService implements ZhongZhengApiService {

  Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  public List<ZZBankInfo> getSupportBankList() {
    try {
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(true, null);
      logMap(info);
      ZZGeneralRespWithListData<ZZBankInfo> resp = callZZApiWithListData(
          ZhongZhengAPIConstants
              .ZZ_API_URL_SUPPORT_BANK_LIST, ZZBankInfo.class, info);
      checkResult(resp);

      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public CancelTradeResult cancelTrade(String applySerial, String pid) {
    try {

      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("applyserial", applySerial);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);

      logMap(info);
      ZZGeneralResp<CancelTradeResult> resp = callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_CANCEL_TRADE, CancelTradeResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public SellResult sellFund(String sellNum, String outsideOrderNo, String trdAcco,
      String fundCode, String sellType, String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("sell_num", sellNum);
      origInfo.put("outsideorderno", outsideOrderNo);
      origInfo.put("tradeacco", trdAcco);
      origInfo.put("fundcode", trimFundCode(fundCode));
      origInfo.put("sell_type", sellType);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralResp<SellResult> resp = callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_SELL_FUND, SellResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getApplyResultByOutSideOrderNo(String outsideOrderNo,
      String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("outsideorderno", outsideOrderNo);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp = callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }



  @Override
  public List<ApplyResult> getApplyResultByApplySerial(String applySerial,
      String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("applyserial", applySerial);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp = callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZAplyCfmInfo> getConfirmResult(String trdAcco, String applySerial,String
      outsideOrderNo, String pid)
      throws Exception {
      try {
        TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);

         origInfo.put("tradeacco", trdAcco);
        if(!StringUtils.isEmpty(applySerial)) {
          origInfo.put("applyserial", applySerial);
        }else if(!StringUtils.isEmpty(outsideOrderNo)){
          origInfo.put("outsideorderno", outsideOrderNo);
        }
        TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
        logMap(info);
        ZZGeneralRespWithListData<ZZAplyCfmInfo> resp = callZZApiWithListData(ZhongZhengAPIConstants
            .ZZ_API_URL_CONFIRM_LIST, ZZAplyCfmInfo.class, info);
        checkResult(resp);
        return resp.getData();

      } catch (Exception e) {
        logger.error("Error:", e);
        throw e;
      }
  }



  private String trimFundCode(String originCode){
    if(!StringUtils.isEmpty(originCode) && originCode.contains(".")){
      String fundCode = originCode.split("\\.")[0];
      return fundCode;
    }else{
      return originCode;
    }
  }

  @Override
  public WalletApplyResult applyWallet(String trdAcco, String pid, String applySum,
      String outsideOrderNo) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("applysum", applySum);
      origInfo.put("tradeacco", trdAcco);
      origInfo.put("outsideorderno", outsideOrderNo);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralResp<WalletApplyResult> resp =  callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_WALLET_APPLY, WalletApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }

  }

  @Override
  public List<ZZWltInfoRlt> getWltInfo(String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZWltInfoRlt> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_WALLET_INFO, ZZWltInfoRlt.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public ZZSellWltRlt sellWallet(String pid, String sellNum, String outsideOrderNo,
      String tradeAcco) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("sell_num", sellNum);
      origInfo.put("outsideorderno", outsideOrderNo);
      origInfo.put("tradeacco", tradeAcco);

      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralResp<ZZSellWltRlt> resp =  callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_SELL_WALLET, ZZSellWltRlt.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZWltAplyInfo> getWalletApply(String pid,String outsideOrderNo, String
      applySerial) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      if(!StringUtils.isEmpty(applySerial)) {
        origInfo.put("applyserial", applySerial);
      }else if(!StringUtils.isEmpty(outsideOrderNo)){
        origInfo.put("outsideorderno", outsideOrderNo);
      }
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZWltAplyInfo> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_GET_WALLET_APPLY, ZZWltAplyInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZFundShareInfo> getFundShare(String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);

      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZFundShareInfo> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_FUND_SHARE, ZZFundShareInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZFundShareInfo> getFundShare(String pid, String fundCode) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      if(!StringUtils.isEmpty(fundCode)){
        origInfo.put("fundcode", fundCode);
      }
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZFundShareInfo> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_FUND_SHARE, ZZFundShareInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZFundInfo> getAllFundInfo() throws Exception {
    try {
      TreeMap<String, String> origInfo = new TreeMap<>();

      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(true, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZFundInfo> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_FUND_INFO, ZZFundInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZTradeLimit> getTradeLimit(String fundCode) throws Exception {
    try {
      TreeMap<String, String> origInfo = new TreeMap<>();
      origInfo.put("fundcode", fundCode);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(true, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZTradeLimit> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_TRADE_LIMIT, ZZTradeLimit.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getApplyResults(String outsideOrderNo, String applySerial,
       String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      if(!StringUtils.isEmpty(applySerial)){
        origInfo.put("applyserial", applySerial);
      }else if(StringUtils.isEmpty(outsideOrderNo)){
        origInfo.put("outsideorderno", outsideOrderNo);
      }


      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp = callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public ZZBuyResult buyFund(String pid, String tradeAcco, String fundCode, String fundSum,
      String outsideOrderNo) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("fundcode", fundCode);
      origInfo.put("applysum", fundSum);
      origInfo.put("tradeacco", tradeAcco);
      origInfo.put("outsideorderno", outsideOrderNo);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralResp<ZZBuyResult> resp = callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_BUY_FUND, ZZBuyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZBonusInfo> getBonusInfo(String pid, String fundCode, String startDate)
      throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("fundcode", fundCode);
      origInfo.put("startdate", startDate);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZBonusInfo> resp = callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_BONUS_LIST, ZZBonusInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ZZDiscountInfo> getDiscountInfo(String pid, String fundCode) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("fundcode", fundCode);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ZZDiscountInfo> resp = callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_TRADE_DISCOUNT, ZZDiscountInfo.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public ZZRiskCmtResult commitRiskLevel(String pid, Integer riskAbility) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("risk_ability", riskAbility.toString());
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralResp<ZZRiskCmtResult> resp = callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_COMMIT_RISK, ZZRiskCmtResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }
}
