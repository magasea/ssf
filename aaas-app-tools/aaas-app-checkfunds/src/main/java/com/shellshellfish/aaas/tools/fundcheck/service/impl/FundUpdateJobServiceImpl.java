package com.shellshellfish.aaas.tools.fundcheck.service.impl;

import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.redis.UpdateFundsJobBaseInfoRedis;
import com.shellshellfish.aaas.tools.fundcheck.repositories.redis.UpdateFundsJobInfoBaseDao;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import com.shellshellfish.aaas.tools.fundcheck.service.CsvFundInfoService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Component
public class FundUpdateJobServiceImpl implements FundUpdateJobService {

  private static final Logger logger = LoggerFactory.getLogger(FundUpdateJobServiceImpl.class);

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;



  @Resource
  UpdateFundsJobInfoBaseDao updateFundsJobInfoBaseDao;

  @Autowired
  CsvFundInfoService csvFundInfoService;


  @Override
  public boolean checkAndUpdateFunds(String csvFileOriginPath) {

    File csvFile = Paths.get(csvFileOriginPath).toFile();
    Long modifiedTime = 0L;
    try {
      BasicFileAttributes attrs = Files.readAttributes(csvFile.getAbsoluteFile().toPath(), BasicFileAttributes
      .class);
      modifiedTime = attrs.lastAccessTime().toMillis();
    } catch (IOException e) {
      logger.error("Exception:", e);
      e.printStackTrace();
    }

    UpdateFundsJobBaseInfoRedis updateFundsJobBaseInfoRedis = updateFundsJobInfoBaseDao.get
        (modifiedTime);
    if(updateFundsJobBaseInfoRedis != null){
      logger.error("this last modified time:{} is already in handling", modifiedTime.toString());
//      return false;
    }
      updateFundsJobBaseInfoRedis = new UpdateFundsJobBaseInfoRedis();
      updateFundsJobBaseInfoRedis.setFileUpdateTime(modifiedTime);
      updateFundsJobInfoBaseDao.addUserBaseInfo(updateFundsJobBaseInfoRedis);
      try {
        csvFundInfoService.getFundsInfoFromCsvFile(csvFile.getAbsolutePath());

      } catch (IOException e) {
        logger.error("Exception:", e);
      }

    return false;
  }

  @Override
  public List<FundCheckRecord> getCurrentFundCheckRecord() {
    return csvFundInfoService.getInconsistFundInfos();

  }

  @Override
  public List<BaseCheckRecord> getBaseFundCheckRecord() {
    return csvFundInfoService.getInconsistBaseInfos();

  }
}