package com.shellshellfish.aaas.tools.fundcheck.service;

import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.CSVFundInfo;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import java.io.IOException;
import java.util.List;

/**
 * Created by chenwei on 2018- 三月 - 07
 */

public interface CsvFundInfoService {
  List<CSVFundInfo> getFundsInfoFromCsvFile(String csvFile) throws IOException;
  List<FundCheckRecord> getInconsistFundInfos();
  List<BaseCheckRecord> getInconsistBaseInfos();
}
