package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralResp;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import com.shellshellfish.aaas.zhongzhengapi.util.ZhongZhengAPIConstants;
import com.shellshellfish.aaas.zhongzhengapi.util.ZhongZhengAPIUtils;
import java.lang.reflect.Type;
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
      ZZGeneralResp<BankZhongZhenInfo> resp =  callZZApi(ZhongZhengAPIConstants
          .ZZ_API_URL_SUPPORT_BANK_LIST,BankZhongZhenInfo.class, info);
      checkResult(resp);
//      String json = restTemplate.postForObject(ZhongZhengAPIConstants.ZZ_API_URL_SUPPORT_BANK_LIST, info, String.class);
//      Type ZZGeneralRespT = new TypeToken<ZZGeneralResp<BankZhongZhenInfo>>() {}.getType();
//      ZZGeneralResp<BankZhongZhenInfo> zhongZhenInfoZZGeneralResp =  gson.fromJson(json,
//          ZZGeneralRespT);
      checkResult(resp);
      return resp.getData();
    } catch (Exception e) {
      logger.error("Error:", e);
      e.printStackTrace();
    }
    return null;
  }







}
