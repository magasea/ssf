package com.shellshellfish.fundcheck.service;

import com.shellshellfish.fundcheck.model.CSVFundInfo;
import java.io.IOException;
import java.util.List;

/**
 * Created by chenwei on 2018- 三月 - 07
 */

public interface CsvFundInfoService {
  List<CSVFundInfo> getFundsInfoFromCsvFile(String csvFile) throws IOException;
  List<CSVFundInfo> getInconsistFundInfos(List<CSVFundInfo> origCsvFundInfos);
}
