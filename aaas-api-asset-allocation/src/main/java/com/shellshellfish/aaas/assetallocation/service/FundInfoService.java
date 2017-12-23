package com.shellshellfish.aaas.assetallocation.service;

import com.shellshellfish.aaas.datacollect.DailyFunds;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 23
 */

public interface FundInfoService {

  public List<DailyFunds> getDailyFunds(DailyFundsQuery fundsQuery) throws Exception;

}
