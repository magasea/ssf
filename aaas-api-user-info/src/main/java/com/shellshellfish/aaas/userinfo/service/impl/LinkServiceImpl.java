package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.LinksDaoService;
import com.shellshellfish.aaas.userinfo.service.LinkService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

public class LinkServiceImpl implements LinkService {

  Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);

  @Autowired
  LinksDaoService linksDaoService;

  @Override
  public Map<String, Object> getLinksForRequest(Map<String, String> cond) {
    if(CollectionUtils.isEmpty(cond)){
      logger.error("There is no hint information in cond");
      return null;
    }else{
      return linksDaoService.getLinksByCondition(cond);
    }
  }
}
