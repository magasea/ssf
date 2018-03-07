package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.datamanager.service.FundUpdateJobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Service
public class FundUpdateJobServiceImpl implements FundUpdateJobService{

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;

  @Value("${shellshellfish.csvFileOriginName}")
  String csvFileOriginName;

  @Override
  public boolean checkAndUpdateFunds(String csvFileOriginPath) {
    return false;
  }
}
