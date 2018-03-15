package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shellshellfish.aaas.common.enums.FundClassEnum;
import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.model.dto.UserProdChgDetail;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@Service
public class UserProdChangeLogServiceImpl implements UserProdChangeLogService{

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public List<UserProdChg> getGeneralChangeLogs(Long prodId) {
    Criteria criteria = Criteria.where("prodId").is(prodId);
    Query query = new Query(criteria);
    query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "modifySeq"));
    List<UserProdChg> reslist = mongoTemplate.find(query, UserProdChg.class);
    return reslist;
  }

  @Override
  public List<UserProdChgDetail> getDetailChangeLogs(Long prodId, Integer changeSeq) {
    Criteria criteria = Criteria.where("prodId").is(prodId).and("modifySeq").is(changeSeq);
    Query query = new Query(criteria);
    query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "fundType"));
    List<UserProdChgDetail> reslist = mongoTemplate.find(query, UserProdChgDetail.class);
    return reslist;
  }
  
  @Override
  public List<UserProdChg> getGeneralChangeLogs(Long prodId, Long groupId) {
    Criteria criteria = Criteria.where("prodId").is(prodId).and("groupId").is(groupId);
    Query query = new Query(criteria);
    query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "modifySeq"));
    List<UserProdChg> reslist = mongoTemplate.find(query, UserProdChg.class);
    return reslist;
  }

  @Override
  public List<Map> getWarehouseRecords(Long prodId, Integer modifySeq) {
    Map<Integer, Integer> fundClassMap = null;
    FundClassEnum fundClassEnum[] = FundClassEnum.values();
    fundClassMap = new HashMap<Integer, Integer>();
    for (int count = 0; count < fundClassEnum.length; count++) {
      Integer value = fundClassEnum[count].getFundClassId();
      fundClassMap.put(value, value);
    }
    // 获取fn_upc_detail数据
    List<UserProdChgDetail> userProdChgDetailsList = this.getDetailChangeLogs(prodId, modifySeq);
    Map<Integer, Map> resultMap = new HashMap<Integer, Map>();
    Map<String, Object> firstMap = new HashMap<String, Object>();
    Map<String, Object> secondMap = new HashMap<String, Object>();
    if (userProdChgDetailsList != null && !userProdChgDetailsList.isEmpty()) {
      for (int j = 0; j < userProdChgDetailsList.size(); j++) {
        firstMap = new HashMap<String, Object>();
        List<Map<String, Object>> firstList = new ArrayList<Map<String, Object>>();
        secondMap = new HashMap<String, Object>();
        UserProdChgDetail userProdChgDetail = userProdChgDetailsList.get(j);
        int fundType = userProdChgDetail.getFundType();
        Long adjustBefore = 0L;
        Long adjustAfter = 0L;
        Long percentBefore = 0L;
        Long percentAfter = 0L;
        if (fundClassMap.containsKey(fundType)) {
          fundClassMap.remove(fundType);
          firstMap.put("fundTypeName", userProdChgDetail.getFundTypeName());
          firstMap.put("adjustBefore", userProdChgDetail.getPercentBefore());
          firstMap.put("adjustAfter", userProdChgDetail.getPercentAfter());

          secondMap.put("code", userProdChgDetail.getCode());
          secondMap.put("fundName", userProdChgDetail.getFundName());
          secondMap.put("percentBefore", userProdChgDetail.getPercentBefore());
          secondMap.put("percentAfter", userProdChgDetail.getPercentAfter());
          firstList.add(secondMap);
          firstMap.put("fundType", fundType);
          firstMap.put("fundList", firstList);

          resultMap.put(fundType, firstMap);
        } else {
          if (resultMap.get(fundType) != null) {
            firstMap = (Map<String, Object>) resultMap.get(fundType);
            firstList = (List<Map<String, Object>>) firstMap.get("fundList");
            if (firstMap.get("adjustBefore") != null) {
              Long beforeValue = (Long) firstMap.get("adjustBefore");
              firstMap.put("adjustBefore", userProdChgDetail.getPercentBefore() + beforeValue);
            }
            if (firstMap.get("adjustAfter") != null) {
              Long afterValue = (Long) firstMap.get("adjustAfter");
              firstMap.put("adjustAfter", userProdChgDetail.getPercentBefore() + afterValue);
            }

            secondMap.put("fundTypeName", userProdChgDetail.getFundTypeName());
            secondMap.put("code", userProdChgDetail.getCode());
            secondMap.put("fundName", userProdChgDetail.getFundName());
            secondMap.put("percentBefore", userProdChgDetail.getPercentBefore());
            secondMap.put("percentAfter", userProdChgDetail.getPercentAfter());
            firstList.add(secondMap);
            firstMap.put("fundType", fundType);
            firstMap.put("fundList", firstList);

            resultMap.put(fundType, firstMap);
          }
        }
      }

      if (fundClassMap != null && fundClassMap.size() > 0) {
        for (Integer value : fundClassMap.keySet()) {
          Map typeMap = new HashMap<String, Object>();
          typeMap.put("adjustBefore", "0.00");
          typeMap.put("adjustAfter", "0.00");
          typeMap.put("fundTypeName", FundClassEnum.getComment(value));
          typeMap.put("fundType", value);
          typeMap.put("fundList", new ArrayList<Map<String, Object>>());
          resultMap.put(value, typeMap);
        }
      }
    }

    List<Map> result = new ArrayList<Map>();
    if (resultMap != null && resultMap.size() > 0) {
      for (Map obj : resultMap.values()) {
        result.add(obj);
      }
    }
    return result;
  }

  @Override
  public boolean insertGeneralChangeLogs(List<UserProdChg> userProdChgs) {
    for(UserProdChg userProdChg: userProdChgs){
      mongoTemplate.save(userProdChg);
    }
    return true;
  }

  @Override
  public boolean insertDetailChangeLogs(List<UserProdChgDetail> userProdChgDetails) {
    for(UserProdChgDetail userProdChgDetail: userProdChgDetails){
      mongoTemplate.save(userProdChgDetail);
    }
    return false;
  }


}
