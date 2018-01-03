package com.shellshellfish.aaas.datacollection.server.service;

import com.shellshellfish.aaas.datacollection.server.model.FundYeildRate;
import java.util.List;

public interface DataCollectionService {

  public List<String> CollectItemsSyn(List<String> collectDatas) throws Exception;

  public List<String> CollectItemsAsyn(List<String> collectDatas) throws Exception;

  public List<FundYeildRate> getLastFundYeildRates(List<String> fundCodes);

  public List<FundYeildRate> getLastFundYeildRates4Test(List<String> fundCodes);

}
