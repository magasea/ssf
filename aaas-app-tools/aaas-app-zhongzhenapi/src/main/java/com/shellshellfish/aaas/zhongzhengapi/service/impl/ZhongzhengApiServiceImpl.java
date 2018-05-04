package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.google.gson.Gson;
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
import org.springframework.web.client.RestTemplate;


@Service
public class ZhongzhengApiServiceImpl extends AbstractZhongzhengApiService implements ZhongZhengApiService  {
  Logger logger = LoggerFactory.getLogger(getClass());

  private final Gson gson = new Gson();
  private RestTemplate restTemplate = new RestTemplate();

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
      origInfo.put("fundcode", fundCode);
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


}
