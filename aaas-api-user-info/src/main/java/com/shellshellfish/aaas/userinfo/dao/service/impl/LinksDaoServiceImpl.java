package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.LinksDaoService;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
@Service
public class LinksDaoServiceImpl implements LinksDaoService{
  Logger logger = LoggerFactory.getLogger(LinksDaoServiceImpl.class);

  @Override
  public Map<String, Object> getLinksByCondition(Map<String, String> cond) {
    Map<String, Object> result = new HashMap<>();
    if(CollectionUtils.isEmpty(cond)){
      logger.error("No cond information got");
      return result;
    }else{
      if(cond.get("requestName").equals("userInfo")){
        Map<String, Object> _links = new HashMap<>();
        _links.put("create",new HashMap());
        _links.put("delete",new HashMap());
        Map<String, Object> related = new HashMap<>();
        related.put("userInfo","/api/userInfo/baseInfo/id");
        related.put("userAssets","/api/userInfo/assetsOverview/id");
        related.put("userInvestedProducts","/api/userInfo/userInvestedProducts/id");
        related.put("userBankCards","/api/userInfo/userBankCards/id");
        related.put("userInviteFriends","/api/userInfo/userInviteFriends/id");
        related.put("userMessages","/api/userInfo/userMessages/id");
        related.put("aboutUs","/api/userInfo/aboutUs");
        related.put("home","/api/homePage/id");
        related.put("finance","/api/finance/id");
        _links.put("related",related);

        return _links;
      }
    }
    return null;
  }
}
