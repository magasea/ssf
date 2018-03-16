package com.shellshellfish.aaas.tools.fundcheck.service;

import java.util.List;

/**
 * Created by chenwei on 2018- 三月 - 13
 */
public interface FundGrpcService {
  public boolean checkFunds(List<String> fundCodes, String beginDate, String endDate, int tableIdx);
}
