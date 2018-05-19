package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralErrResp;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralErrRespReturnList;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralResp;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZGeneralRespWithListData;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by chenwei on 2018- 四月 - 25
 */

public abstract class AbstractZhongzhengApiService  {
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
  void checkResult(ZZGeneralRespWithListData zzGeneralRespWithListData) throws Exception {
    if(!zzGeneralRespWithListData.getStatus().equals("1") || !zzGeneralRespWithListData.getErrno().equals("0000")){
      String errMsg = String.format("%s:%s", zzGeneralRespWithListData.getErrno(),
          zzGeneralRespWithListData.getMsg());
      logger.error(errMsg);
      throw new Exception(errMsg);
    }
  }
  void checkResult(ZZGeneralResp zzGeneralResp) throws Exception {
    if(!zzGeneralResp.getStatus().equals("1") || !zzGeneralResp.getErrno().equals("0000")){
      String errMsg = String.format("%s:%s", zzGeneralResp.getErrno(), zzGeneralResp
          .getMsg());
      logger.error(errMsg);
      throw new Exception(errMsg);
    }
  }

  protected <T> ZZGeneralRespWithListData<T> parseAbstractResponse(String json, TypeToken type) {
    return new GsonBuilder()
        .create()
        .fromJson(json, type.getType());
  }

  <T> ZZGeneralRespWithListData<T> callZZApiWithListData(String url, Class<T> cl ,  Map info){

    String json = restTemplate.postForObject(url, info, String.class);
    if(json.contains(ZZGeneralErrRespReturnList.RETURN_LIST)){
      ZZGeneralErrRespReturnList responseBase = gson.fromJson(json, getType
          (ZZGeneralErrRespReturnList.class, cl));
      ZZGeneralRespWithListData<T> responseBaseNormal = new ZZGeneralRespWithListData<>();
      MyBeanUtils.mapEntityIntoDTO(responseBase, responseBaseNormal);
      return responseBaseNormal;
    }
    ZZGeneralRespWithListData<T> responseBase = gson.fromJson(json, getType(ZZGeneralRespWithListData.class, cl));

    return responseBase;
  }

  <T> ZZGeneralResp<T> callZZApiGeneral(String url, Class<T> cl ,  Map info){

    String json = restTemplate.postForObject(url, info, String.class);
    logger.info(json);
    ZZGeneralResp<T> responseBase = null;
    try {
      responseBase = gson.fromJson(json, getType(ZZGeneralResp.class, cl));
    }catch (Exception ex){
      logger.error("err:", ex);
      ZZGeneralErrResp errResp = gson.fromJson(json, ZZGeneralErrResp.class);
      responseBase = new ZZGeneralResp<>();
      MyBeanUtils.mapEntityIntoDTO(errResp, responseBase);
    }

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
