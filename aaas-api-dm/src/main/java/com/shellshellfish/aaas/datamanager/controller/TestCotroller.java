package com.shellshellfish.aaas.datamanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.datamanager.model.FundResources;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-datamanage")
public class TestCotroller {
  @Autowired
  private MongoTemplate mongoTemplate;

  @RequestMapping(value = "/wpUpdateFundResource")
  public String wpUpdateFundResource(String fundInfos) {
    System.out.println("开始执行data-manage");
    List<String> funds = new ArrayList<>();
    JSONArray jsonArray = JSONObject.parseArray(fundInfos);
    try {
      if(jsonArray.size()>0){
        for(int i=0;i<jsonArray.size();i++){
          JSONObject fund = jsonArray.getJSONObject(i);
          Map fundInfoMap = JSONObject.toJavaObject(fund, Map.class);
          String code=(String)fundInfoMap.get("code");
          String fundrisklevel=String.valueOf(Integer.parseInt((String)fundInfoMap.get("fundrisklevel"))+1);
          //查询fundresources
          Criteria criteria = Criteria.where("code").is(code+".OF");
          Query query = new Query(criteria);
          List<FundResources> list = mongoTemplate.find(query, FundResources.class);
          if (list != null && list.size() == 1) {
            FundResources fundResources = list.get(0);
              Update update = new Update();
              update.set("risklevel", "R"+fundrisklevel);
              System.out.println("更新基金风险等级fundcode:"+code+ "  原风险等级: "+fundResources.getRisklevel()+"  更新后风险等级: R"+fundrisklevel);
              mongoTemplate.findAndModify(query, update, FundResources.class);

          } else {
            System.out.println("不存在该中正基金fundcode:"+code);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "更新成功";
  }
}
