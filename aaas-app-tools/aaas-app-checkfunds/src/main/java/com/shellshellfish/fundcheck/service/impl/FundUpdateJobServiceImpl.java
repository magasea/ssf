package com.shellshellfish.fundcheck.service.impl;

import com.shellshellfish.fundcheck.model.redis.UpdateFundsJobBaseInfoRedis;
import com.shellshellfish.fundcheck.repositories.redis.UpdateFundsJobInfoBaseDao;
import com.shellshellfish.fundcheck.service.CsvFundInfoService;
import com.shellshellfish.fundcheck.service.FundGrpcService;
import com.shellshellfish.fundcheck.service.FundUpdateJobService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
}
