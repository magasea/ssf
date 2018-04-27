package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.google.gson.GsonBuilder;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralResp;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by chenwei on 2018- 四月 - 25
 */

public abstract class AbstractZhongzhengApiService extends ZZApiServiceGrpc.ZZApiServiceImplBase {
  Logger logger = LoggerFactory.getLogger(getClass());



  void logMap(Map info){
    info.forEach(
        (key, value) ->{
          logger.info("{}:{}",key, value);
        }
    );
  }
  void checkResult(ZZGeneralResp zzGeneralResp) throws Exception {
    if(!zzGeneralResp.getStatus().equals("1") || !zzGeneralResp.getErrno().equals("0000")){
      String errMsg = String.format("{}:{}", zzGeneralResp.getErrno(), zzGeneralResp.getMsg());
      logger.error(errMsg);
      throw new Exception(errMsg);
    }
  }

//  void callZZApi<T> (String url, Map info, Class<T> classType){
//    String json = restTemplate.postForObject(ZhongZhengAPIConstants.ZZ_API_URL_SUPPORT_BANK_LIST, info, String.class);
//    Type ZZGeneralRespT = new TypeToken<ZZGeneralResp<T>>() {}.getType();
//    ZZGeneralResp<BankZhongZhenInfo> zhongZhenInfoZZGeneralResp =  gsonBuilder.create().fromJson(json,
//        ZZGeneralRespT);
//    System.out.println(zhongZhenInfoZZGeneralResp.getData().size());
//  }



}
