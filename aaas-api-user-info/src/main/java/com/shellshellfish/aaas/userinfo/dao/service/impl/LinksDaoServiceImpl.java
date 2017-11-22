package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.LinksDaoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Map> relatedList=new ArrayList();
        related.put("href","/api/userInfo/baseInfo/id");
        related.put("name","userInfo");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/assetsOverview/id");
        related.put("name","userAssets");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/userInvestedProducts/id");
        related.put("name","userInvestedProducts");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/userBankCards/id");
        related.put("name","userBankCards");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/userInviteFriends/id");
        related.put("name","userInviteFriends");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/userMessages/id");
        related.put("name","userMessages");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/userInfo/aboutUs");
        related.put("name","aboutUs");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/homePage/id");
        related.put("name","home");
        relatedList.add(related);
        
        related = new HashMap<>();
        related.put("href","/api/finance/id");
        related.put("name","finance");
        relatedList.add(related);
        
        _links.put("related",relatedList);

        return _links;
      }
    }
    return null;
  }
}
