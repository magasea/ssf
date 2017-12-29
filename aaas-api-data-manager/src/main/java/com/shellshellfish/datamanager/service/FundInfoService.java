package com.shellshellfish.datamanager.service;

import com.shellshellfish.datamanager.model.FundYeildRate;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public interface FundInfoService {
  public List<FundYeildRate> getLastFundYeildRates(List<String> fundCodes);

  public List<FundYeildRate> getLastFundYeildRates4Test(List<String> fundCodes);
}
