package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.datamanager.model.redis.UpdateFundsJobBaseInfoRedis;
import com.shellshellfish.datamanager.repositories.redis.UpdateFundsJobInfoBaseDao;
import com.shellshellfish.datamanager.service.CsvFundInfoService;
import com.shellshellfish.datamanager.service.FundUpdateJobService;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Service
public class FundUpdateJobServiceImpl implements FundUpdateJobService{

  private static final Logger logger = LoggerFactory.getLogger(FundUpdateJobServiceImpl.class);

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;

  @Value("${shellshellfish.csvFileOriginName}")
  String csvFileOriginName;

  @Resource
  UpdateFundsJobInfoBaseDao updateFundsJobInfoBaseDao;

  @Autowired
  CsvFundInfoService csvFundInfoService;

  @Override
  public boolean checkAndUpdateFunds(String csvFileOriginPath) {
    File csvFile = new File(csvFileOriginPath + File.separator+ csvFileOriginName);
    Long modifiedTime = csvFile.lastModified();
    UpdateFundsJobBaseInfoRedis updateFundsJobBaseInfoRedis = updateFundsJobInfoBaseDao.get
        (modifiedTime);
    if(updateFundsJobBaseInfoRedis != null){
      logger.error("this last modified time:{} is already in handling", modifiedTime.toString());
      return false;
    }else{
      updateFundsJobBaseInfoRedis = new UpdateFundsJobBaseInfoRedis();
      updateFundsJobBaseInfoRedis.setFileUpdateTime(modifiedTime);
      updateFundsJobInfoBaseDao.addUserBaseInfo(updateFundsJobBaseInfoRedis);
      try {
        csvFundInfoService.getFundsInfoFromCsvFile(csvFileOriginPath + File.separator+
            csvFileOriginName);
      } catch (IOException e) {
        logger.error("Exception:", e);
      }
    }
    return false;
  }
}
