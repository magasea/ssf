package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZSellWltRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.CancelTradeResult;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
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
public class ZhongzhengApiServiceImpl extends AbstractZhongzhengApiService implements ZhongZhengApiService  {
  Logger logger = LoggerFactory.getLogger(getClass());



  @Override
  public List<BankZhongZhenInfo> getSupportBankList() {
    try {
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(true, null);
      logMap(info);
      ZZGeneralRespWithListData<BankZhongZhenInfo> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_SUPPORT_BANK_LIST,BankZhongZhenInfo.class, info);
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
      ZZGeneralResp<CancelTradeResult> resp =  callZZApiGeneral(ZhongZhengAPIConstants
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
  public SellResult  sellFund(String sellNum, String outsideOrderNo, String trdAcco,
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
      ZZGeneralResp<SellResult> resp =  callZZApiGeneral(ZhongZhengAPIConstants
          .ZZ_API_URL_SELL_FUND, SellResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getApplyResultByOutSideOrderNo(String outsideOrderNo, String trdAcco,
      String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("outsideorderno", outsideOrderNo);
      origInfo.put("tradeacco", trdAcco);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getApplyResultByTrdAcco(String trdAcco, String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("tradeacco", trdAcco);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getApplyResultByApplySerial(String applySerial, String trdAcco,
      String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("applyserial", trdAcco);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      throw e;
    }
  }

  @Override
  public List<ApplyResult> getConfirmResultByTrdAcco(String trdAcco, String pid) throws Exception {
    try {
      TreeMap<String, String> origInfo = ZhongZhengAPIUtils.makeOrigInfo(pid);
      origInfo.put("tradeacco", trdAcco);
      TreeMap<String, String> info = ZhongZhengAPIUtils.makeInfo(false, origInfo);
      logMap(info);
      ZZGeneralRespWithListData<ApplyResult> resp =  callZZApiWithListData(ZhongZhengAPIConstants
          .ZZ_API_URL_APPLY_LIST, ApplyResult.class, info);
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
}
