package com.shellshellfish.aaas.finance.trade.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shellshellfish.aaas.finance.trade.order.service.impl.FundInfoZhongZhengApiService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping(value = "/test-financetrade")
@RestController
public class TestController {
  @Autowired
  private FundInfoZhongZhengApiService fundInfoService;
  @Autowired
  private MongoTemplate mongoTemplate;

  @ResponseBody
  @RequestMapping(value = "/wpGetFundInfoList",method = RequestMethod.GET)
  public String getFundInfoList(){
    System.out.println("开始执行finance-trade");
    List< HashMap<String ,Object>> fundInfoList=new ArrayList<>();
    String fundInfos="{}";
    try {
      List<String> allFundsInfo = fundInfoService.getAllFundsInfo();
      for (String fundInfo:allFundsInfo){
        HashMap<String ,Object> tempMap=new HashMap<>();
        Map fundInfoMap = (Map)JSON.parse(fundInfo);
        String code = (String)fundInfoMap.get("fundcode");
        String fundrisklevel = (String)fundInfoMap.get("fundrisklevel");
        System.out.println("code="+code);
        System.out.println("fundrisklevel="+fundrisklevel);
        tempMap.put("code",code);
        tempMap.put("fundrisklevel",fundrisklevel);
        fundInfoList.add(tempMap);
      }
      fundInfos=JSON.toJSONString(fundInfoList,SerializerFeature.DisableCircularReferenceDetect);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fundInfos;
  }

  @ResponseBody
  @RequestMapping(value = "/updateAllFundinfo",method = RequestMethod.GET)
  public String updateAllFundinfo(){
    try {
      List<String> allFundsInfo = fundInfoService.getAllFundsInfo();
      fundInfoService.writeAllFundsToMongoDb(allFundsInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "更新成功";
  }

}
