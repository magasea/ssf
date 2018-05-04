package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralResp;
import com.shellshellfish.aaas.zhongzhengapi.util.ZhongZhengAPIConstants;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by chenwei on 2018- 四月 - 25
 */

public abstract class AbstractZhongzhengApiService extends ZZApiServiceGrpc.ZZApiServiceImplBase {
  Logger logger = LoggerFactory.getLogger(getClass());


  private final Gson gson = new Gson();
  private RestTemplate restTemplate = new RestTemplate();
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

  protected <T> ZZGeneralResp<T> parseAbstractResponse(String json, TypeToken type) {
    return new GsonBuilder()
        .create()
        .fromJson(json, type.getType());
  }

  <T> ZZGeneralResp<T> callZZApi(String url, Class<T> cl ,  Map info){
    TypeToken<ZZGeneralResp<T>> typeToken = new TypeToken<ZZGeneralResp<T>>() {};
    String json = restTemplate.postForObject(url, info, String.class);
    ZZGeneralResp<T> responseBase = gson.fromJson(json, getType(ZZGeneralResp.class, cl));
//    ZZGeneralResp<T> responseBase = parseAbstractResponse(json, typeToken);
    return responseBase;
  }

  private Type getType(final Class<?> rawClass, final Class<?> parameterClass) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{parameterClass};
      }

      @Override
      public Type getRawType() {
        return rawClass;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

    };
  }

}
