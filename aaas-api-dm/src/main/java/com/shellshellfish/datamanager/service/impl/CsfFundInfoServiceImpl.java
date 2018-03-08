package com.shellshellfish.datamanager.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.shellshellfish.datamanager.model.CSVFundInfo;
import com.shellshellfish.datamanager.service.CsvFundInfoService;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Service
public class CsfFundInfoServiceImpl implements CsvFundInfoService{

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public List<CSVFundInfo> getFundsInfoFromCsvFile(String csvFile) throws IOException {


    try (
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
    ) {
      CsvToBean csvToBean = new CsvToBeanBuilder(reader)
          .withType(CSVFundInfo.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      List<CSVFundInfo> csvFundInfos = csvToBean.parse();
      csvFundInfos.forEach(item -> {
        System.out.println(item.getCode());
        System.out.println(item.getFundName());
        System.out.println(item.getNavaccum());
        System.out.println(item.getNavadj());
        System.out.println(item.getNavlatestdate());
        System.out.println(item.getNavunit());
        }
      );
    }

    return null;
  }

  @Override
  public List<CSVFundInfo> getInconsistFundInfos(List<CSVFundInfo> origCsvFundInfos) {
    return null;
  }
}
