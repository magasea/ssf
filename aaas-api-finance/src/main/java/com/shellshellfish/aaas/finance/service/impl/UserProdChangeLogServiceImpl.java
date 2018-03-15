package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.model.dto.UserProdChgDetail;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    return null;
  }

  @Override
  public List<UserProdChgDetail> getDetailChangeLogs(Long prodId, Long changeSeq) {
    return null;
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
