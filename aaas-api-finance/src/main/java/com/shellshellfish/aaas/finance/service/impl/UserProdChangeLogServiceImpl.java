package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.model.dto.UserProdChgDetail;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
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
    List<UserProdChg> reslist = mongoTemplate.find(query, UserProdChg.class);
    return reslist;
  }

  @Override
  public List<UserProdChgDetail> getDetailChangeLogs(Long prodId, Long changeSeq) {
    Criteria criteria = Criteria.where("prodId").is(prodId).and("modifySeq").is(changeSeq);
    Query query = new Query(criteria);
    List<UserProdChgDetail> reslist = mongoTemplate.find(query, UserProdChgDetail.class);
    return reslist;
  }

  @Override
  public List<Map> getWarehouseRecords() {
    List<Map> result = new ArrayList<Map>();
    Map<String,String> subdivisionMap = new HashMap<>();
    // 获取UserProdChg数据
    List<UserProdChg> userProdChgList = this.getGeneralChangeLogs(15L);
    if (userProdChgList != null && userProdChgList.size() > 0) {
      for (int i = 0; i < userProdChgList.size(); i++) {
        subdivisionMap = new HashMap<>();
        UserProdChg userProdChg = new UserProdChg();
        userProdChg = userProdChgList.get(0);
        // 获取UserProdChgDetail数据
        List<UserProdChgDetail> userProdChgDetailsList =
            this.getDetailChangeLogs(15L, userProdChg.getModifySeq());
        if (userProdChgDetailsList != null && !userProdChgDetailsList.isEmpty()) {
          for(int j = 0; j < userProdChgDetailsList.size();j++){
            UserProdChgDetail userProdChgDetail = userProdChgDetailsList.get(0);
//            subdivisionMap.put("", userProdChgDetail.get);
          }
        }
      }
    }
    return result;
  }

  @Override
  public boolean insertGeneralChangeLogs(List<UserProdChg> userProdChgs) {
    return false;
  }

  @Override
  public boolean insertDetailChangeLogs(List<UserProdChgDetail> userProdChgDetails) {
    return false;
  }


}
