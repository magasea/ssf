package com.shellshellfish.fundcheck.service;

import java.io.IOException;

/**
 * Created by chenwei on 2018- 三月 - 07
 */

public interface FundUpdateJobService {
  boolean checkAndUpdateFunds(String csvFileOriginPath);

}
