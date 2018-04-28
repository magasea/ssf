package com.shellshellfish.aaas.tools.fundcheck.service.impl;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.tools.fundcheck.commons.FundTablesIndexEnum;
import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.redis.UpdateFundsJobBaseInfoRedis;
import com.shellshellfish.aaas.tools.fundcheck.repositories.redis.UpdateFundsJobInfoBaseDao;
import com.shellshellfish.aaas.tools.fundcheck.service.FundGrpcService;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import com.shellshellfish.aaas.tools.fundcheck.service.CsvFundInfoService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
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


  @Autowired
  FundGrpcService fundGrpcService;

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

  public void pullInfoBaseOnFundAndBaseKeyInfo(){
    YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
    yamlFactory.setResources(new ClassPathResource("fundAndBaseInfo.yml"));
    Properties props = yamlFactory.getObject();
    String fundCodes = props.getProperty("fund.code");
    String baseCodes = props.getProperty("base.code");
    logger.info(fundCodes);
    String[] fundCodeArray = fundCodes.split(",");
    String[] baseCodeArray = baseCodes.split(",");
    String endDate = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()).split("T")[0];
    String beginDate = TradeUtil.getReadableDateTime(TradeUtil.getUTCTimeNDayAfter(-10)).split
        ("T")[0];
    logger.info("beginDate:{} endDate:{}", beginDate, endDate);
    fundGrpcService.checkFunds(Arrays.asList(fundCodeArray), beginDate, endDate, FundTablesIndexEnum
        .FUND_YIELDRATE.getIndex());

    fundGrpcService.checkFunds(Arrays.asList(baseCodeArray), beginDate, endDate, FundTablesIndexEnum
        .FUNDBASECLOSE.getIndex());

  }
}
