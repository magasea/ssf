package com.shellshellfish.aaas.finance.service;

import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.model.dto.UserProdChgDetail;
import java.util.List;
import java.util.Map;

/**
 * Created by chenwei on 2018- 三月 - 15
 */

public interface UserProdChangeLogService {

  List<UserProdChg> getGeneralChangeLogs(Long prodId);

  List<UserProdChgDetail> getDetailChangeLogs(Long prodId, Long changeSeq);

  boolean insertGeneralChangeLogs(List<UserProdChg> userProdChgs);

  boolean insertDetailChangeLogs(List<UserProdChgDetail> userProdChgDetails);

  List<Map> getWarehouseRecords();
}
