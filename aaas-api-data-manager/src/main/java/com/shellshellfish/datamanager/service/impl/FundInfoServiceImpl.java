package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.datamanager.model.FundYeildRate;
import com.shellshellfish.datamanager.repositories.MongoFundYeildRateRepository;
import com.shellshellfish.datamanager.service.FundInfoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */
@Service
public class FundInfoServiceImpl implements FundInfoService{

  MongoFundYeildRateRepository mongoFundYeildRateRepository;

  @Override
  public List<FundYeildRate> getLastFundYeildRates(List<String> fundCodes) {
    Long yestDay = SSFDateUtils.getYestdayDateInLong();
    return  mongoFundYeildRateRepository.findByQuerydateAndCodeIsIn(yestDay/1000L, fundCodes);
  }

  @Override
  public List<FundYeildRate> getLastFundYeildRates4Test(List<String> fundCodes) {
    return mongoFundYeildRateRepository.findByCodeIsIn(fundCodes);
  }

}
