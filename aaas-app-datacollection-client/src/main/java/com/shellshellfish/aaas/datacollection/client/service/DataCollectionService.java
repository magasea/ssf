package com.shellshellfish.aaas.datacollection.client.service;
import com.shellshellfish.aaas.datacollection.client.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.client.model.vo.FundsQuery;
import java.util.List;

public interface DataCollectionService {

  public List<DailyFunds> getDailyFunds(FundsQuery fundsQuery) throws Exception;


}
