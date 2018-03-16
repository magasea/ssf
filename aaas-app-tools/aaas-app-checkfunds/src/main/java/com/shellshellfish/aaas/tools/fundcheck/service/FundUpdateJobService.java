package com.shellshellfish.aaas.tools.fundcheck.service;

import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import java.io.IOException;
import java.util.List;

/**
 * Created by chenwei on 2018- 三月 - 07
 */

public interface FundUpdateJobService {
  boolean checkAndUpdateFunds(String csvFileOriginPath);

  List<FundCheckRecord> getCurrentFundCheckRecord();

  List<BaseCheckRecord> getBaseFundCheckRecord();

}
