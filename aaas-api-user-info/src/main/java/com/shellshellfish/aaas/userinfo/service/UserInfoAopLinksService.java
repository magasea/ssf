package com.shellshellfish.aaas.userinfo.service;

import java.util.Map;

public interface UserInfoAopLinksService {

  public void insertLinksByPointCut(String pointCut, Map<String, Object> response);


}
