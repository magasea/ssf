package com.shellshellfish.aaas.tools.fundcheck.service.impl;


import com.shellshellfish.aaas.common.http.HttpJsonResult;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.CSVBaseInfo;
import com.shellshellfish.aaas.tools.fundcheck.model.FundYieldRate;
import com.shellshellfish.aaas.tools.fundcheck.service.FundGrpcService;
import com.shellshellfish.aaas.tools.fundcheck.utils.CSVParser;
import com.shellshellfish.aaas.tools.fundcheck.commons.CheckFundsEnum;
import com.shellshellfish.aaas.tools.fundcheck.commons.FundTablesIndexEnum;
import com.shellshellfish.aaas.tools.fundcheck.model.CSVFundInfo;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.service.CsvFundInfoService;
import com.shellshellfish.aaas.tools.fundcheck.utils.CheckerUtils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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

  @Value("${shellshellfish.asset-allocation-insertdf-url}")
  String assetAllocationInsertdf;

  @Value("${shellshellfish.asset-allocation-inithistory-url}")
  String assetAllocationInithistory;

  @Value("${shellshellfish.asset-allocation-initpyamongo-url}")
  String assetAllocationInitpyamongo;

  @Value("${shellshellfish.data-manager-initcache-url}")
  String assetAllocationInitcache;

  @Value("${shellshellfish.data-manager-initcache-detail-url}")
  String assetAllocationInitcacheDetail;

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

  @Override
  public List<FundCheckRecord> getInconsistFundInfos() {
    Query queryUpdate = new Query();
    Criteria criteriaStatus = Criteria.where("result").lt(CheckFundsEnum.CONSIST.getStatus());
    queryUpdate.addCriteria(criteriaStatus);
    List<FundCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, FundCheckRecord
        .class,"fund_check_record");
    return recordsInDb;
  }

  @Override
  public List<BaseCheckRecord> getInconsistBaseInfos() {
    Query queryUpdate = new Query();
    Criteria criteriaStatus = Criteria.where("result").lt(CheckFundsEnum.CONSIST.getStatus());
    queryUpdate.addCriteria(criteriaStatus);
    List<BaseCheckRecord> recordsInDb =  mongoToolsTemplate.find(queryUpdate, BaseCheckRecord
        .class, "base_check_record");
    return recordsInDb;

  }

  @Override
  public void restApiCall() throws InterruptedException {

    RestTemplate restTemplate = new RestTemplate();
    try{
      HttpJsonResult jsonResult1 = restTemplate.getForObject(assetAllocationInsertdf,
          HttpJsonResult.class);
      Thread.sleep(10);
      if(jsonResult1 != null){
        logger.info(jsonResult1.toString());
      }
      HttpJsonResult jsonResult2 = restTemplate.getForObject(assetAllocationInithistory,
          HttpJsonResult.class);
      Thread.sleep(10);
      if(jsonResult2 != null){
        logger.info(jsonResult2.toString());
      }
      HttpJsonResult jsonResult3 = restTemplate.getForObject(assetAllocationInitpyamongo
          , HttpJsonResult.class);
      Thread.sleep(10);
      if(jsonResult3 != null){
        logger.info(jsonResult3.toString());
      }
      HttpJsonResult jsonResult4 = restTemplate.getForObject(assetAllocationInitcache, HttpJsonResult.class);
      Thread.sleep(10);
      if(jsonResult4 != null){
        logger.info(jsonResult4.toString());
      }
      HttpJsonResult jsonResult5 = restTemplate.getForObject
          (assetAllocationInitcacheDetail, HttpJsonResult.class);
      Thread.sleep(10);
      if(jsonResult5 != null){
        logger.info(jsonResult5.toString());
      }

    }catch (Exception ex){
      logger.error("Exception:", ex);
      throw ex;
    }
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
    checkAndUpdateToolsFundRecords();
  }

  public void updateFundInfo(){

  }

  private void checkAndUpdateToolsFundRecords() {

    Query query = new Query();
    Criteria criteriaResult = Criteria.where("result").lte(CheckFundsEnum.DIFFERENT.getStatus());
    query.addCriteria(criteriaResult);
    List<FundCheckRecord> fundCheckRecords = mongoToolsTemplate.find(query, FundCheckRecord
        .class, "fund_check_record");
    if(CollectionUtils.isEmpty(fundCheckRecords)){
      logger.info("there is no record need check for baseRecords");
      return;
    }
    for(FundCheckRecord fundCheckRecord: fundCheckRecords){
      query = new Query();
      Criteria criteriaCode = Criteria.where("querydatestr").is(fundCheckRecord.getDate().replace
          ("-","/")).and
          ("code").is(fundCheckRecord.getCode());
      query.addCriteria(criteriaCode);
      List<FundYieldRate> results = mongoTemplate.find(query, FundYieldRate.class,
          "fund_yieldrate");
      if(CollectionUtils.isEmpty(results)){
        logger.info("failed to find FundYieldRate for date:{} and code:{}",fundCheckRecord
            .getDate(), fundCheckRecord.getCode());
        continue;
      }
      FundYieldRate fundYieldRate =  results.get(0);
      if(compareRecordAndDbData(fundCheckRecord, fundYieldRate)!= 0){
        fundCheckRecord.setUnitNav(fundYieldRate.getNavunit().toString());
        fundCheckRecord.setAdjustedNav(fundYieldRate.getNavadj().toString());
        fundCheckRecord.setAccumulatedNav(fundYieldRate.getNavaccum().toString());
        fundCheckRecord.setResult(CheckFundsEnum.UPDATEFAILED.getStatus());
      }else{
        fundCheckRecord.setUnitNav(fundYieldRate.getNavunit().toString());
        fundCheckRecord.setAdjustedNav(fundYieldRate.getNavadj().toString());
        fundCheckRecord.setAccumulatedNav(fundYieldRate.getNavaccum().toString());
        fundCheckRecord.setResult(CheckFundsEnum.UPDATESUCCESS.getStatus());
      }
      mongoToolsTemplate.save(fundCheckRecord);
    }

  }

  private int compareRecordAndDbData(FundCheckRecord fundCheckRecord, FundYieldRate fundYieldRate) {
    if(fundCheckRecord.getAccumulatedNav() == null || fundCheckRecord.getAdjustedNav() == null ||
        fundCheckRecord.getUnitNav() == null){
      return -1;
    }
    if(!CheckerUtils.rundUpCheckEq(fundCheckRecord.getCsvAccumulatedNav(), fundYieldRate.getNavaccum
        ().toString())) {
      return -1;
    }
    if(!CheckerUtils.rundUpCheckEq(fundCheckRecord.getCsvAdjustedNav(), fundYieldRate.getNavadj()
        .toString())) {
      return -1;
    }
    if(!CheckerUtils.rundUpCheckEq(fundCheckRecord.getCsvUnitNav(), fundYieldRate.getNavunit()
        .toString())) {
      return -1;
    }
    return 0;
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
//      codes.add(baseCheckRecord.getCode());

    }
    codes.add("all");
    fundGrpcService.checkFunds(codes, beginDate, endDate, FundTablesIndexEnum.FUNDBASECLOSE
        .getIndex());
    checkAndUpdateToolsBaseRecords();
//    updateToolsBaseRecords(codes, beginDate, endDate, FundTablesIndexEnum.FUND_YIELDRATE
//        .getIndex());
  }

  public void checkAndUpdateToolsBaseRecords(){
    Set<String> queryDatesStrs = new HashSet<>();
    Query query = new Query();
    Criteria criteriaResult = Criteria.where("result").lte(CheckFundsEnum.DIFFERENT.getStatus());
    query.addCriteria(criteriaResult);
    List<BaseCheckRecord> baseCheckRecords = mongoToolsTemplate.find(query, BaseCheckRecord
        .class, "base_check_record");
    if(!CollectionUtils.isEmpty(baseCheckRecords)){
      baseCheckRecords.forEach(item-> queryDatesStrs.add(item.getDate()));
    }else{
      logger.info("there is no record need check for baseRecords");
      return;
    }

    Map<String, Map<String, Object>> fundBaseCloses = new HashMap<>();
    for(String queryDate : queryDatesStrs){
      query = new Query();
      Criteria criteriaCode = Criteria.where("querydatestr").is(queryDate);
      query.addCriteria(criteriaCode);
      List<Map> results = mongoTemplate.find(query, Map.class,
          "fundbaseclose");
      if(CollectionUtils.isEmpty(results)){
        logger.info("failed to find fundBaseClose for date:{}",queryDate);
        continue;
      }else{
        logger.info("find {} fundBaseClose for date:{}",results.size(), queryDate);
        fundBaseCloses.put(queryDate, results.get(0));
      }
    }
    for(BaseCheckRecord baseCheckRecord: baseCheckRecords){
      Map<String, Object> fundBaseCloseMap =  fundBaseCloses.get(baseCheckRecord.getDate());
      if(fundBaseCloseMap == null){
        logger.error("there is no fundBaseClose info for date:{}", baseCheckRecord.getDate());
        continue;
      }
      String localTempCode = baseCheckRecord.getCode().replace(".","");
      Double fundBaseCloseInDB = (Double)fundBaseCloseMap.get(localTempCode);
      if(fundBaseCloseInDB != null && baseCheckRecord.getCsvClose() !=null && CheckerUtils
          .rundUpCheckEq(fundBaseCloseInDB.toString(),
          baseCheckRecord.getCsvClose())){
        baseCheckRecord.setClose(fundBaseCloseInDB.toString());
        baseCheckRecord.setResult(CheckFundsEnum.UPDATESUCCESS.getStatus());
      }else{
        if(fundBaseCloseInDB != null){
          baseCheckRecord.setClose(fundBaseCloseInDB.toString());
        }else{
          logger.error("failed find fundBaseCloseInDB for code:{} and date:{}",baseCheckRecord
              .getCode(), baseCheckRecord.getDate());
        }

        baseCheckRecord.setResult(CheckFundsEnum.UPDATEFAILED.getStatus());
      }
      mongoToolsTemplate.save(baseCheckRecord);
    }
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
        if(!CheckerUtils.rundUpCheckEq(item.getClose(),((Double)fundbasecloses.get(0).get(item
            .getCode())).toString())){
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
        baseCheckRecord.setClose(fundbasecloses.get(0).get(item.getCode()).toString());
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
      baseCheckRecord.setClose( baseCheck.get(csvBaseInfo.getCode()).toString());
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
    if(item.getDate().contains("-")){
      query.addCriteria(criteria.is(item.getDate().replace("-","/")));
    }else{
      query.addCriteria(criteria.is(item.getDate()));
    }

    query.addCriteria(criteriaCode.is(item.getCode()));
    List<FundYieldRate> dailyFunds = null;
    dailyFunds = mongoTemplate.find(query, FundYieldRate.class, "fund_yieldrate");
    if(CollectionUtils.isEmpty(dailyFunds)){
      logger.error("this date:{} for fund info of:{} haven't find corresponding records in "
          + "fund_yieldrate", item.getDate(), item.getCode());
      findAndModifyFundCheck(item, null);
    }else{
      FundYieldRate fundYieldRate = dailyFunds.get(0);
      int checkNotLikely = compareCsvAndDbData(item, fundYieldRate);

      if(checkNotLikely != 0){
        findAndModifyFundCheck(item, fundYieldRate);
      }
    }
  }

  private int compareCsvAndDbData(CSVFundInfo item, FundYieldRate fundYieldRate) {
    if(!CheckerUtils.rundUpCheckEq(item.getAccumuLatedNav(), fundYieldRate.getNavaccum
        ().toString())) {
      return -1;
    }
    if(!CheckerUtils.rundUpCheckEq(item.getAdjustedNav(), fundYieldRate.getNavadj().toString())) {
      return -1;
    }
    if(!CheckerUtils.rundUpCheckEq(item.getUnitNav(), fundYieldRate.getNavunit().toString())) {
      return -1;
    }
    return 0;
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


}
