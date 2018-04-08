package com.shellshellfish.aaas.transfer.service;

import java.util.Map;

/**
 * Created by chenwei on 2018- 四月 - 08
 */

public interface GrpcOemInfoService {

  Map<String, String> getOemInfoById(Long oem);

}
