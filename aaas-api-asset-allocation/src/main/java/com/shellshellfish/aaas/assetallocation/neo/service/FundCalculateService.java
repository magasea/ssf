package com.shellshellfish.aaas.assetallocation.neo.service;

import com.shellshellfish.aaas.assetallocation.neo.entity.FundCalculateData;
import com.shellshellfish.aaas.assetallocation.neo.entity.FundNetVal;
import com.shellshellfish.aaas.assetallocation.neo.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.neo.job.service.JobTimeService;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundCalculateDataMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil;
import com.shellshellfish.aaas.assetallocation.neo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:计算基金收益率及风险率等（日周月年）
 */
@Service
public class FundCalculateService {

    @Autowired
    private FundNetValMapper fundNetValMapper;

    @Autowired
    private FundCalculateDataMapper fundCalculateDataMapper;

    @Autowired
    private JobTimeService jobTimeService;
    @Autowired
    private FundGroupMapper fundGroupMapper;

    private static final Logger logger = LoggerFactory.getLogger(FundCalculateService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private int countOfWeek=0;

    private Map<String,FundNetVal> monthMap=new HashMap<>();

    private Map<String,FundNetVal> yearMap=new HashMap<>();

    /*
     * 根据时间查询净值表中复权单位净值数据
     */
    public Map<String, List<FundNetVal>> selectFundNetValueByDate(Date selectDate) {
        List<FundNetVal> fundNetValList = null;
        //查询产品组合中 code
        List<String> codeList = fundGroupMapper.findGroupCode();
        HashMap<String,Object> codeMap = new HashMap<>();
        codeMap.put("codeList", codeList);
        codeMap.put("selectDate", selectDate);
        try {
            fundNetValList = fundNetValMapper.getAllDataByCodeAndDate(codeMap);
        } catch (Exception e) {
            logger.error("查询净值数据失败!");
            e.printStackTrace();
        }
        //根据基金代码分组(按净值日期倒序排列)
        Map<String, List<FundNetVal>> fundListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(fundNetValList)) {
            return fundListMap;
        }

        for (FundNetVal fundNetVal : fundNetValList) {
            List<FundNetVal> fundNetVals = fundListMap.get(fundNetVal.getCode());
            if (fundNetVals == null) {
                List<FundNetVal> tempList = new ArrayList<>();
                tempList.add(fundNetVal);
                fundListMap.put(fundNetVal.getCode(), tempList);
            } else {
                fundNetVals.add(fundNetVal);
                fundListMap.put(fundNetVal.getCode(), fundNetVals);
            }
        }

        return fundListMap;
    }


    /*
     * 计算每日的收益率以及风险率,insert into table:fund_calculate_data_day
     */
    public void calculateDataOfData() {
        //查询计算风险率所需参数（取值数量）
        Integer number = getNumberFromSysConfig(TYPE_OF_DAY);
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = null;
        try {
            jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_DATA_OF_DAY);
        } catch(Exception e) {
            logger.error("查询 计算每日的收益率以及风险率记录时间 失败");
        }

        Date selectDate = new Date();
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            selectDate = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            selectDate = jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String, List<FundNetVal>> fundListMap = selectFundNetValueByDate(selectDate);
        if (!CollectionUtils.isEmpty(fundListMap)) {
            Iterator<Map.Entry<String, List<FundNetVal>>> entries = fundListMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<FundNetVal>> entry = entries.next();
                String code = entry.getKey();
                List<FundNetVal> fundList = entry.getValue();
                Double navadj1; //当天复权单位净值
                Double navadj2; //前一天复权单位净值
                Double yieldRatio = 0d; //收益率
                Double riskRatio = 0d; //风险率
                Double semiVariance = 0d; //半方差

                if (CollectionUtils.isEmpty(fundList)) {
                    continue;
                }

                for(int i = 0; i < fundList.size() - 1; i++) {
                    try {
                        int tempNum = i;
                        //取该天数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1 = getEffectData(tempNum, fundList);
                        //取该天前一天数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2 = getEffectData(++tempNum, fundList);
                        //计算收益率
                        if (fundNetVal1 != null && fundNetVal2 != null) {
                            navadj1 = fundNetVal1.getNavadj();
                            navadj2 = fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1, navadj2);
                        }

                        //计算风险率
                        riskRatio = calculateRiskRatio(i, fundList, number);

                        //计算半方差
                        semiVariance = calculateSemiVariance(i, fundList, number);

                        FundCalculateData fundCalculateData = new FundCalculateData();
                        fundCalculateData.setCode(code); //基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate()); //净值日期
                        fundCalculateData.setYieldRatio(yieldRatio == null ? 0d : yieldRatio); //收益率
                        fundCalculateData.setRiskRatio(riskRatio == null ? 0d: riskRatio); //风险率
                        fundCalculateData.setSemiVariance(semiVariance == null ? 0d : semiVariance); //半方差
                        fundCalculateData.setNavadj(fundNetVal1.getNavadj()); //复权单位净值
                        try {
                            fundCalculateDataMapper.insertFundCalculateDataDay(fundCalculateData);
                        } catch (Exception e) {
                            logger.error("插入基金日计算数据失败：fundCalculateData=" + fundCalculateData.toString());
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        logger.error("计算基金日收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }

                }

            }

        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_DATA_OF_DAY, maxDate, SUCCESSFUL_STATUS);
    }


    /*
     * 计算每周的收益率以及风险率,insert into table:fund_calculate_data_week
     */
    public Boolean calculateDataOfWeek() {
        Boolean doSuccess = true;
        //查询计算风险率所需参数（取值数量）
        Integer number = this.getNumberFromSysConfig(TYPE_OF_WEEK);

        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = null;
        try {
            jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_DATA_OF_WEEK);
        } catch(Exception e) {
            logger.error("查询 计算每周的收益率以及风险率记录时间 失败",e);
        }

        Date triggerTime = null;
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            triggerTime = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            triggerTime = jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String, List<FundNetVal>> fundNetValListMap = this.selectFundNetValueByDate(triggerTime);
        if (!CollectionUtils.isEmpty(fundNetValListMap)) {
            //过滤数据（取周五数据）
            Map<String, List<FundNetVal>> fundFriListMap = this.filterData(fundNetValListMap, TYPE_OF_WEEK);
            Iterator<Map.Entry<String, List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<FundNetVal>> entry = entries.next();
                String code = entry.getKey();
                List<FundNetVal> fundList = entry.getValue();
                if (CollectionUtils.isEmpty(fundList)) {
                    continue;
                }

                Double navadj1 = null;//该周周五净值
                Double navadj2 = null;//前一周周五净值
                Double yieldRatio = null;//收益率
                Double riskRatio = null;//风险率
                Double semiVariance = 0d;//半方差
                for (int i = 0; i < fundList.size() - 1; i++) {
                    try {
                        int tempNum = i;
                        //取该周周五数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1 = getEffectData(tempNum, fundList);
                        //取前一周周五数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2 = getEffectData(++tempNum, fundList);

                        //计算收益率
                        if (fundNetVal1 != null && fundNetVal2 != null) {
                            navadj1 = fundNetVal1.getNavadj();
                            navadj2 = fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1, navadj2);
                        }
                        //计算风险率
                        riskRatio = calculateRiskRatio(i, fundList, number);
                        //计算半方差
                        semiVariance = calculateSemiVariance(i, fundList, number);

                        FundCalculateData fundCalculateData = new FundCalculateData();
                        fundCalculateData.setCode(code); //基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate()); //净值日期
                        fundCalculateData.setYieldRatio(yieldRatio == null ? 0d : yieldRatio); //收益率
                        fundCalculateData.setRiskRatio(riskRatio == null ? 0d : riskRatio); //风险率
                        fundCalculateData.setSemiVariance(semiVariance == null ? 0d : semiVariance); //半方差
                        fundCalculateData.setNavadj(fundNetVal1.getNavadj()); //复权单位净值
                        fundCalculateData.setCreateDate(new Date()); //数据产生时间

                        try{
                            Integer effectRow = fundCalculateDataMapper.insertFundCalculateDataWeek(fundCalculateData);
                            if (effectRow == null) {
                                doSuccess = false;
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("插入基金周计算数据失败：fundCalculateData=" + fundCalculateData.toString());
                            e.printStackTrace();
                        }

                    } catch(Exception e) {
                        logger.error("计算基金周收益率以及风险率失败：code=" + code);
                        e.printStackTrace();
                    }

                }
            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(triggerTime);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_DATA_OF_WEEK, maxDate, SUCCESSFUL_STATUS);

        return doSuccess;
    }

    /*
     * 计算每月的收益率以及风险率,insert into table:fund_calculate_data_month
     */
    public void calculateDataOfMonth(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_MONTH);

        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=null;
        try{
            jobTimeRecord=jobTimeService.selectJobTimeRecord(CALCULATE_DATA_OF_MONTH);
        }catch(Exception e){
            logger.error("查询 计算每月的收益率以及风险率记录时间 失败");
        }
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        if(fundListMap!=null && fundListMap.size()>0){

            //过滤数据（取每月底数据）
            Map<String,List<FundNetVal>> fundFriListMap=filterData(fundListMap, TYPE_OF_MONTH);

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1=null;//该月月底净值
                Double navadj2=null;//前一月月底净值
                Double yieldRatio=null;//收益率
                Double riskRatio=null;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该月月底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取前一月月底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);
                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差
                        fundCalculateData.setNavadj(fundNetVal1.getNavadj());//复权单位净值

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataMonth(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金月计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        logger.error("计算基金月收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }


                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_DATA_OF_MONTH, maxDate, SUCCESSFUL_STATUS);
    }


    /*
     * 计算每年的收益率以及风险率,insert into table:fund_calculate_data_year
     */
    public void calculateDataOfYear(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_YEAR);
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=null;
        try{
            jobTimeRecord=jobTimeService.selectJobTimeRecord(CALCULATE_DATA_OF_YEAR);
        }catch(Exception e){
            logger.error("查询 计算每年的收益率以及风险率记录时间 失败");
        }
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        //查询基金净值数据
        if(fundListMap!=null && fundListMap.size()>0){

            //过滤数据（取每年年底数据）
            Map<String,List<FundNetVal>> fundFriListMap=filterData(fundListMap, TYPE_OF_YEAR);

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1=null;//该年年底净值
                Double navadj2=null;//前一年年底净值
                Double yieldRatio=null;//收益率
                Double riskRatio=null;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该年年底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取前一年年底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);
                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差
                        fundCalculateData.setNavadj(fundNetVal1.getNavadj());//复权单位净值

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataYear(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金年计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        logger.error("计算基金年收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }


                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_DATA_OF_YEAR, maxDate, SUCCESSFUL_STATUS);
    }


    /*
     * 查询配置参数
     */
    public Integer getNumberFromSysConfig(String type) {
        Integer number = null;
        try {
            number = fundNetValMapper.getNumberFromSysConfig(type);
        } catch(Exception e) {
            logger.error("查询配置数据失败：type=" + type);
        }
        if (number == null) {
            number = ConstantUtil.DEFAULT_TYPE_DAY_NUMBER; // 默认周期类型时, 计算风险率时取值数量
            logger.debug("默认查询配置数据：type=" + type + "number:" + number);
        }

        return number;
    }


    /*
     *取到有效净值数据（没有则往之前时间递推）
     */

    public FundNetVal getEffectData(int i,List<FundNetVal> fundList){
        if(0<=i && i<fundList.size()){
            FundNetVal fundNetVal=fundList.get(i);
            while(fundNetVal==null || fundNetVal.getNavadj()==null ) {
                int temp=i++;
                if(temp<fundList.size()){
                    fundNetVal=fundList.get(temp);
                }else{
                    fundNetVal=null;//fundList 遍历结束仍无有效数据，赋值为null,结束循环
                    break;
                }
            }
            return fundNetVal;

        }else{
            return null;
        }

    }


    /*
     * 过滤数据
     */
    public  Map<String,List<FundNetVal>> filterData(Map<String,List<FundNetVal>> fundListMap,String type){
        Map<String,List<FundNetVal>> fundFriDataMap=new HashMap<>();
        Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundListMap.entrySet().iterator();
        while (entries.hasNext()) {

            Map.Entry<String, List<FundNetVal>> entry = entries.next();
            String code = entry.getKey();
            List<FundNetVal> fundList = entry.getValue();
            //对fundList 按时间进项过滤筛选
            if(fundList!=null){
                for(int i=0;i<fundList.size();i++){
                    int tempNum=i;
                    FundNetVal fundNetVal=fundList.get(i);
                    if(fundNetVal!=null && fundNetVal.getNavLatestDate()!=null && fundNetVal.getNavadj()!=null){
                        List<FundNetVal> list=fundFriDataMap.get(code);
                        Date navLatestDate=fundNetVal.getNavLatestDate();
                        if(list==null){
                            List<FundNetVal> tempList=new ArrayList<>();

                            if(TYPE_OF_WEEK.equals(type)){
                                //取周五数据
                                FundNetVal tempFundNetVal=getFriData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_MONTH.equals(type)){
                                //取每月底数据
                                FundNetVal tempFundNetVal=getMonthData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_YEAR.equals(type)){
                                //取每年年底数据
                                FundNetVal tempFundNetVal=getYearData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }


                            fundFriDataMap.put(code,tempList);
                        }else{

                            if(TYPE_OF_WEEK.equals(type)){
                                //取周五数据
                                FundNetVal tempFundNetVal=getFriData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_MONTH.equals(type)){
                                //取每月底数据
                                FundNetVal tempFundNetVal=getMonthData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_YEAR.equals(type)){
                                //取每年年底数据
                                FundNetVal tempFundNetVal=getYearData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }


                            fundFriDataMap.put(code,list);
                        }

                    }

                }
                if(TYPE_OF_MONTH.equals(type)){
                    monthMap=new HashMap<>();
                }else if(TYPE_OF_YEAR.equals(type)){
                    yearMap=new HashMap<>();
                }

            }


        }

        return fundFriDataMap;
    }


    /*
     * 取周五数据，若无则往前递推
     */
    public FundNetVal  getFriData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        //navLatestDate: Tue Nov 14 00:00:00 CST 2017
        String[] navLatestDateArr =navLatestDate.toString().split(" ");
        if(navLatestDateArr.length==6){
            String weekday=navLatestDateArr[0];
            if(WEEKDAY_OF_FRI.equals(weekday)){
                countOfWeek=0;
                return fundNetVal;
            }else{
                ++countOfWeek;
                if(countOfWeek<7){//一周七天
                    return null;
                }else{
                    countOfWeek=0;
                    FundNetVal tempFundNetVal=getEffectData(tempNum-2,fundList);//一周之内找不到周五数据，则取该周周尾有效数据
                    return tempFundNetVal;
                }

            }
        }else{
            return null;
        }


    }

    /*
     * 取每月月底数据
     */
    public FundNetVal  getMonthData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        String navLatestDateStr =sdf.format(navLatestDate);
        String[] navLatestDateArr =navLatestDateStr.split("-");
        if(navLatestDateArr.length==3){
            String tag=navLatestDateArr[0]+navLatestDateArr[1];
            if( monthMap.get(tag)==null){
                monthMap.put(tag,fundNetVal);
                return fundNetVal;
            }

        }else{
            return null;
        }
        return null;
    }

    /*
     * 取每年年底数据
     */
    public FundNetVal  getYearData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        String navLatestDateStr =sdf.format(navLatestDate);
        String[] navLatestDateArr =navLatestDateStr.split("-");
        if(navLatestDateArr.length==3){
            String tag=navLatestDateArr[0];
            if( yearMap.get(tag)==null){
                yearMap.put(tag,fundNetVal);
                return fundNetVal;
            }

        }else{
            return null;
        }
        return null;
    }

    /*
     * 计算收益率方法
     */
    public Double calculateYieldRatio(Double navadj1, Double navadj2) {
        Double yieldRatio = 0d;
        if (navadj2 != 0) {
            yieldRatio = Math.log(navadj1/navadj2);

        }
        return yieldRatio;
    }

    /*
     * 计算风险率方法
     */
    public Double calculateRiskRatio(int i, List<FundNetVal> fundList, int number){
        List<Double> tempList = new ArrayList<>();
        //取值
        List<Double> yieldRatioArr = new ArrayList<>();
        while (tempList.size() < number) {
            FundNetVal fundNetVal = getEffectData(i,fundList);
            if (fundNetVal != null) {
                tempList.add(fundNetVal.getNavadj());
                if (tempList.size() > 1) {
                    Double yieldRatio = calculateYieldRatio(tempList.get(tempList.size() - 2), tempList.get(tempList.size() - 1));
                    yieldRatioArr.add(yieldRatio);
                }
            }
            i++;
            if (i >= fundList.size()) {
                break;
            }
        }
        //计算风险率(样本标准差)
        Double riskRatio = StandardDiviation(yieldRatioArr.toArray(new Double[0]));

        return riskRatio;
    }


    /*
     * 计算半方差方法
     */
    public Double calculateSemiVariance(int i, List<FundNetVal> fundList, int number) {
        List<Double> tempList = new ArrayList<>();
        //取值
        while (tempList.size() < number) {
            FundNetVal fundNetVal = getEffectData(i, fundList);
            if (fundNetVal != null) {
                tempList.add(fundNetVal.getNavadj());
            }
            i++;
            if (i >= fundList.size()) {
                break;
            }
        }
        Double paramVal = 1.1;//传入参数
        //计算半方差
        Double semiVariance = semiVariance(tempList.toArray(new Double[0]), paramVal);

        return semiVariance;
    }


    //计算风险率(样本标准差 (n-1) )
    public  Double StandardDiviation(Double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        if(m>1){
            return Math.sqrt(dVar/(m-1));
        }
        return 0d;
    }


    /*
     * 计算半方差
     */
    public Double semiVariance(Double[] x,Double paramVal){

        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;

        for(int i=0;i<m;i++){//求方差
            if(x[i]<paramVal){
                dVar+=(x[i]-dAve)*(x[i]-dAve);
            }
        }
        if(m>1){
            return Math.sqrt(dVar/(m-1));
        }
        return 0d;

    }




}
