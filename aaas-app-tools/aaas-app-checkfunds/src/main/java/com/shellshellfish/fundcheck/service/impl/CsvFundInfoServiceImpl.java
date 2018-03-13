package com.shellshellfish.fundcheck.service.impl;


import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.fundcheck.commons.CheckFundsEnum;
import com.shellshellfish.fundcheck.commons.FundTablesIndexEnum;
import com.shellshellfish.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.fundcheck.model.CSVBaseInfo;
import com.shellshellfish.fundcheck.model.CSVFundInfo;
import com.shellshellfish.fundcheck.model.DailyFunds;
import com.shellshellfish.fundcheck.model.FundCheckRecord;
import com.shellshellfish.fundcheck.model.FundYieldRate;
import com.shellshellfish.fundcheck.service.CsvFundInfoService;
import com.shellshellfish.fundcheck.service.FundGrpcService;
import com.shellshellfish.fundcheck.utils.CSVParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Service
public class CsvFundInfoServiceImpl implements CsvFundInfoService {

  Logger logger = LoggerFactory.getLogger(CsvFundInfoServiceImpl.class);

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  MongoTemplate mongoToolsTemplate;

  @Autowired
  FundGrpcService fundGrpcService;

  final static String CNST_BASE = "CLOSE";

  @Override
  public List<CSVFundInfo> getFundsInfoFromCsvFile(String csvFile) throws IOException {

//    CharsetDecoder decoder = Charset.forName("UTF8").newDecoder();
//    decoder.onMalformedInput(CodingErrorAction.IGNORE);
    FileInputStream readerHelp = new FileInputStream(csvFile);

    Reader reader = new InputStreamReader(readerHelp, "utf-8");
    BufferedReader bufferedReader = new BufferedReader(reader);


//    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(reader, "UTF8") );

    String line = bufferedReader.readLine();
    readerHelp.getChannel().position(0);
    reader = new InputStreamReader(readerHelp, "utf-8");
    bufferedReader = new BufferedReader(reader);

    logger.info("first line: \\n{}", line);
    if(line.contains(CNST_BASE)){
      handleBaseInfo(bufferedReader);
      processBaseInfoSync();
    }else{
      handleFundInfo(bufferedReader);
      processFundInfoSync();
    }

    return null;
  }

  private void processFundInfoSync() {
    Query queryUpdate = new Query();
    Criteria criteriaStatus = Criteria.where("result").lt(CheckFundsEnum.CONSIST.getStatus());
    queryUpdate.addCriteria(criteriaStatus);
    List<FundCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, FundCheckRecord
        .class, "fund_check_record");
    String beginDate = null;
    String endDate = null;
    List<String> codes = new ArrayList<>();
    for(FundCheckRecord fundCheckRecord: recordsInDb){
      if(beginDate == null){
        beginDate = fundCheckRecord.getDate();
      }
      if(endDate == null){
        endDate = fundCheckRecord.getDate();
      }
      if(!StringUtils.isEmpty(beginDate) && beginDate.compareTo(fundCheckRecord.getDate())> 0){
        beginDate = fundCheckRecord.getDate();
      }
      if(!StringUtils.isEmpty(endDate) && endDate.compareTo(fundCheckRecord.getDate()) < 0){
        endDate = fundCheckRecord.getDate();
      }
      codes.add(fundCheckRecord.getCode());
    }
    fundGrpcService.checkFunds(codes, beginDate, endDate, FundTablesIndexEnum.FUND_YIELDRATE
        .getIndex());
  }

  private void processBaseInfoSync() {
    Query queryUpdate = new Query();
    Criteria criteriaStatus = Criteria.where("result").lt(CheckFundsEnum.CONSIST.getStatus());
    queryUpdate.addCriteria(criteriaStatus);
    List<BaseCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, BaseCheckRecord
        .class, "base_check_record");
    String beginDate = null;
    String endDate = null;
    List<String> codes = new ArrayList<>();
    for(BaseCheckRecord baseCheckRecord: recordsInDb){
      if(beginDate == null){
        beginDate = baseCheckRecord.getDate();
      }
      if(endDate == null){
        endDate = baseCheckRecord.getDate();
      }
      if(!StringUtils.isEmpty(beginDate) && beginDate.compareTo(baseCheckRecord.getDate())> 0){
        beginDate = baseCheckRecord.getDate();
      }
      if(!StringUtils.isEmpty(endDate) && endDate.compareTo(baseCheckRecord.getDate()) < 0){
        endDate = baseCheckRecord.getDate();
      }
      codes.add(baseCheckRecord.getCode());
    }
    fundGrpcService.checkFunds(codes, beginDate, endDate, FundTablesIndexEnum.FUNDBASECLOSE
        .getIndex());

  }


  private void handleBaseInfo(BufferedReader reader) {
    List<CSVBaseInfo> csvBaseInfos = new ArrayList<>();
    reader.lines().forEach(line -> {
      CSVBaseInfo csvBaseInfo = CSVParser.getCSVBaseInfo(line);
      if(csvBaseInfo!= null){
        csvBaseInfos.add(csvBaseInfo);
      }
    }
    );



    csvBaseInfos.forEach(
        item->checkBaseInfo(item)
    );


  }

  private void checkBaseInfo(CSVBaseInfo item) {
    Query query = new Query();
    Criteria criteria = Criteria.where("querydatestr");
    query.addCriteria(criteria.is(item.getDate()));
    List<Map> fundbasecloses = null;
    fundbasecloses = mongoTemplate.find(query, Map.class, "fundbaseclose");
    boolean needMakeRecord = false;
    if(CollectionUtils.isEmpty(fundbasecloses)){
      logger.error("this date:{} for base close haven't find corresponding records in "
          + "fundbaseclose", item.getDate());
      needMakeRecord = true;
    }else{
      if(!fundbasecloses.get(0).containsKey(item.getCode())){
        logger.error("this date:{} for base close haven't find corresponding records of code:{} in "
            + "fundbaseclose", item.getDate(), item.getCode());
        needMakeRecord = true;
      }else{
        if(!item.getClose().equals(
        ((Double)fundbasecloses.get(0).get(item.getCode())).toString())){
          logger.error("this date:{} code:{} in fundbaseclose is inconsistent csv:{}, db:{}", item
              .getDate(), item.getCode(), item.getClose(),fundbasecloses.get(0).get(item.getCode()) );

          needMakeRecord = true;
        }
      }
    }
    if(needMakeRecord){
      BaseCheckRecord baseCheckRecord = new BaseCheckRecord();
      baseCheckRecord.setCsvClose(item.getClose());
      baseCheckRecord.setCode(item.getCode());
      if(!CollectionUtils.isEmpty(fundbasecloses)){
        baseCheckRecord.setClose((String) fundbasecloses.get(0).get(item.getCode()));
        findAndModifyBaseCheck(item, fundbasecloses.get(0));
        return;
      }
      baseCheckRecord.setDate(item.getDate());
      baseCheckRecord.setResult(CheckFundsEnum.DIFFERENT.getStatus());
      findAndModifyBaseCheck(item, null);
    }
  }

  private void findAndModifyBaseCheck(CSVBaseInfo csvBaseInfo, Map baseCheck){
    Query queryUpdate = new Query();
    Criteria criteriaDate = Criteria.where("date").is(csvBaseInfo.getDate());
    Criteria criteriaCodeCheck = Criteria.where("code").is(csvBaseInfo.getCode());
    queryUpdate.addCriteria(criteriaDate).addCriteria(criteriaCodeCheck);
    List<BaseCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, BaseCheckRecord
        .class, "base_check_record");
    BaseCheckRecord baseCheckRecord;
    if(CollectionUtils.isEmpty(recordsInDb)){
      baseCheckRecord = new BaseCheckRecord();
    }else{
      baseCheckRecord =recordsInDb.get(0);
    }
    if(baseCheck != null){
      baseCheckRecord.setClose((String) baseCheck.get(csvBaseInfo.getCode()));
    }

    baseCheckRecord.setCsvClose(csvBaseInfo.getClose());
    baseCheckRecord.setCode(csvBaseInfo.getCode());

    baseCheckRecord.setDate(csvBaseInfo.getDate());
    baseCheckRecord.setResult(CheckFundsEnum.DIFFERENT.getStatus());
    mongoToolsTemplate.save(baseCheckRecord);
  }

  private void handleFundInfo(BufferedReader reader) {
    List<CSVFundInfo> csvFundInfos = new ArrayList<>();
    reader.lines().forEach(line -> {
        CSVFundInfo csvFundInfo= CSVParser.getCSVFundInfo(line);
        if(csvFundInfo != null){
          csvFundInfos.add(CSVParser.getCSVFundInfo(line));
        }
      }
    );
    csvFundInfos.forEach(item->checkFundInfo(item));

  }

  private void checkFundInfo(CSVFundInfo item) {
    Query query = new Query();
    Criteria criteria = Criteria.where("querydatestr");
    Criteria criteriaCode = Criteria.where("code");
    query.addCriteria(criteria.is(item.getDate()));
    query.addCriteria(criteriaCode.is(item.getCode()));
    List<FundYieldRate> dailyFunds = null;
    dailyFunds = mongoTemplate.find(query, FundYieldRate.class, "fund_yieldrate");
    if(CollectionUtils.isEmpty(dailyFunds)){
      logger.error("this date:{} for fund info of:{} haven't find corresponding records in "
          + "fund_yieldrate", item.getDate(), item.getCode());
      findAndModifyFundCheck(item, null);
    }else{
      FundYieldRate fundYieldRate = dailyFunds.get(0);
        if(!item.getAccumuLatedNav().equals(fundYieldRate.getNavaccum().toString())|| !item.getAdjustedNav()
            .equals(fundYieldRate.getNavadj().toString())||!item.getUnitNav().equals(fundYieldRate
            .getNavunit().toString()))
        {
          logger.error("this date:{} code:{} in fund_yieldrate is inconsistent csv:{} {} {}, "
                  + "db:{} {} {}",
              item.getDate(), item.getCode(), item.getAccumuLatedNav(),item.getAdjustedNav(), item
                  .getUnitNav(), fundYieldRate.getNavaccum(), fundYieldRate.getNavadj(),
              fundYieldRate.getNavunit() );
          int checkNotLikely = 0;
          if(!item.getAdjustedNav().equals(fundYieldRate.getNavadj().toString())){
            String fyrNavAdj = fundYieldRate.getNavadj().toString();
            String subStr = fyrNavAdj.substring(0, fyrNavAdj.length() -1);
            if(item.getAdjustedNav().startsWith(subStr)){
              int dbNumber = Integer.parseInt(fyrNavAdj.substring(fyrNavAdj.length() -
                  1,fyrNavAdj.length()));
              int checkNumber = Integer.parseInt(item.getAdjustedNav().substring(fyrNavAdj.length
                  () - 1,fyrNavAdj.length() ));
              if(Math.abs(dbNumber - checkNumber) <= 1){
                logger.info("fyrNavAdj:{} is like  checkAdjNav:{}", fyrNavAdj, item.getAdjustedNav());
              }else{
                checkNotLikely =+ 1;
              }
            }else{
              checkNotLikely =+ 1;
            }
          }
          else if(!item.getUnitNav().equals(fundYieldRate
            .getNavunit().toString())){
            String fyrNavUnit = fundYieldRate.getNavunit().toString();
            String subStr = fyrNavUnit.substring(0, fyrNavUnit.length() -1);
            if(item.getUnitNav().startsWith(subStr)){
              int dbNumber = Integer.parseInt(fyrNavUnit.substring(fyrNavUnit.length() -
                  1,fyrNavUnit.length()));
              int checkNumber = Integer.parseInt(item.getAdjustedNav().substring(fyrNavUnit.length
                  () - 1,fyrNavUnit.length()));
              if(Math.abs(dbNumber - checkNumber) <= 1){
                logger.info("fyrNavUnit:{} is like  checkNavUnit:{}", fyrNavUnit, item.getUnitNav());
              }else{
                checkNotLikely =+ 1;
              }
          }else{
              checkNotLikely =+ 1;
            }
          }
          else if(!item.getAccumuLatedNav().equals(fundYieldRate.getNavaccum().toString())){
            String fyrNavaccum = fundYieldRate.getNavaccum().toString();
            String subStr = fyrNavaccum.substring(0, fyrNavaccum.length() -1);
            if(item.getUnitNav().startsWith(subStr)){
              int dbNumber = Integer.parseInt(fyrNavaccum.substring(fyrNavaccum.length() -
                  1,fyrNavaccum.length()));
              int checkNumber = Integer.parseInt(item.getAccumuLatedNav().substring(fyrNavaccum.length
                  () - 1,fyrNavaccum.length()));
              if(Math.abs(dbNumber - checkNumber) <= 1){
                logger.info("fyrNavaccum:{} is like  checkAccumUnit:{}", fyrNavaccum, item.getAccumuLatedNav());
              }else{
                checkNotLikely =+ 1;
              }
            }else{
              checkNotLikely =+ 1;
            }
          }
          if(checkNotLikely > 0){
            findAndModifyFundCheck(item, fundYieldRate);
          }
        }
    }
  }

  private void findAndModifyFundCheck(CSVFundInfo csvFundInfo, FundYieldRate fundYieldRate){
    Query queryUpdate = new Query();
    Criteria criteriaDate = Criteria.where("date").is(csvFundInfo.getDate());
    Criteria criteriaCodeCheck = Criteria.where("code").is(csvFundInfo.getCode());
    queryUpdate.addCriteria(criteriaDate).addCriteria(criteriaCodeCheck);
    List<FundCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, FundCheckRecord
        .class, "fund_check_record");
    FundCheckRecord fundCheckRecord;
    if(CollectionUtils.isEmpty(recordsInDb)){
      fundCheckRecord = new FundCheckRecord();
    }else{
      fundCheckRecord =recordsInDb.get(0);
    }
    if(fundYieldRate != null){
      fundCheckRecord.setAccumulatedNav(fundYieldRate.getNavaccum().toString());
      fundCheckRecord.setAdjustedNav(fundYieldRate.getNavadj().toString());
      fundCheckRecord.setUnitNav(fundYieldRate.getNavunit().toString());
    }


    fundCheckRecord.setCode(csvFundInfo.getCode());
    fundCheckRecord.setCsvAccumulatedNav(csvFundInfo.getAccumuLatedNav());
    fundCheckRecord.setCsvAdjustedNav(csvFundInfo.getAdjustedNav());
    fundCheckRecord.setCsvUnitNav(csvFundInfo.getUnitNav());
    fundCheckRecord.setDate(csvFundInfo.getDate());
    fundCheckRecord.setResult(CheckFundsEnum.DIFFERENT.getStatus());

    mongoToolsTemplate.save(fundCheckRecord);
  }
  @Override
  public List<CSVFundInfo> getInconsistFundInfos(List<CSVFundInfo> origCsvFundInfos) {
    return null;
  }
}
