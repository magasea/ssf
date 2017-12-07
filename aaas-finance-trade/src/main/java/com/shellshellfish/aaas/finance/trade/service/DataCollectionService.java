package com.shellshellfish.aaas.finance.trade.service;

import java.util.List;

public interface DataCollectionService {

  public List<String> CollectItemsSyn(List<String> collectDatas) throws Exception;

  public List<String> CollectItemsAsyn(List<String> collectDatas) throws Exception;

}
