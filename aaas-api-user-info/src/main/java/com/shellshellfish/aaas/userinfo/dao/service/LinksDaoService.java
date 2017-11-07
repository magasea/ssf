package com.shellshellfish.aaas.userinfo.dao.service;

import java.util.Map;

public interface LinksDaoService {

  public Map<String, Object> getLinksByCondition(Map<String, String> cond);

}
