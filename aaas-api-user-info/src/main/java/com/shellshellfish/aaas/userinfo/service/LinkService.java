package com.shellshellfish.aaas.userinfo.service;

import java.util.Map;

public interface LinkService {

  Map<String, Object>  getLinksForRequest(Map<String, String> cond);

}
