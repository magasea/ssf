package com.shellshellfish.aaas.oeminfo.service;

import java.util.List;
import java.util.Map;

/**
 * Created by chenwei on 2018- 四月 - 03
 */
public interface OemInfoService {
  Map<String, String> getOemInfo(Long oemId);

  List<String> getOemInfoBankName();
}
