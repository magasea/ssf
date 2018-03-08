package com.shellshellfish.fundcheck.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.shellshellfish.fundcheck.model.CSVBaseInfo;
import com.shellshellfish.fundcheck.model.CSVFundInfo;
import com.shellshellfish.fundcheck.service.CsvFundInfoService;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Service
public class CsvFundInfoServiceImpl implements CsvFundInfoService {

  Logger logger = LoggerFactory.getLogger(CsvFundInfoServiceImpl.class);

  @Autowired
  MongoTemplate mongoTemplate;

  final static String CNST_BASE = "基准";

  @Override
  public List<CSVFundInfo> getFundsInfoFromCsvFile(String csvFile) throws IOException {


    try (
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
    ) {
      LineNumberReader lineNumberReader = new LineNumberReader(reader);
      String line = lineNumberReader.readLine();
      logger.info("first line: \\n{}", line);
      if(line.contains(CNST_BASE)){
        handleBaseInfo(reader);
      }else{
        handleFundInfo(reader);
      }


    }

    return null;
  }

  private void handleFundInfo(Reader reader) {
    CsvToBean csvToBean = new CsvToBeanBuilder(reader)
        .withType(CSVBaseInfo.class)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    List<CSVBaseInfo> csvFundInfos = csvToBean.parse();
    csvFundInfos.forEach(item -> {
          System.out.println(item.getCode());
          System.out.println(item.getBaseName());
          System.out.println(item.getClose());

          System.out.println(item.getNavlatestdate());

        }
    );
  }

  private void handleBaseInfo(Reader reader) {
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

  @Override
  public List<CSVFundInfo> getInconsistFundInfos(List<CSVFundInfo> origCsvFundInfos) {
    return null;
  }
}
