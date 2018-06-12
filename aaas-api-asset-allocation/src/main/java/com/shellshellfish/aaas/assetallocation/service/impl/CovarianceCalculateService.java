package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.job.service.JobTimeService;
import com.shellshellfish.aaas.assetallocation.mapper.CovarianceMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.util.DateUtil;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.shellshellfish.aaas.assetallocation.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/11/21
 * Desc:计算协方差(日周月年)
 */
@Service
public class CovarianceCalculateService {

    @Autowired
    private FundNetValMapper fundNetValMapper;
    @Autowired
    private CovarianceMapper covarianceMapper;
    @Autowired
    private JobTimeService jobTimeService;
    @Autowired
    private FundCalculateService fundCalculateService;
    @Autowired
    private FundGroupMapper fundGroupMapper;

    private static final Logger logger = LoggerFactory.getLogger(CovarianceCalculateService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private List<String[]> codeListOfDay = new ArrayList<>();

    private List<String[]> codeListOfWeek = new ArrayList<>();

    private List<String[]> codeListOfMonth = new ArrayList<>();

    private List<String[]> codeListOfYear = new ArrayList<>();

    private int countOfWeek = 0;

    private Map<String, CovarianceModel> monthMap = new HashMap<>();

    private Map<String, CovarianceModel> yearMap = new HashMap<>();

    /*
     * 计算基金两两组合之间的协方差(周期：日)insert into table:fund_covariance_day
     */
    @Deprecated
    public void calculateCovarianceOfDay(int oemId) {
        CovarianceModel covarianceModel = new CovarianceModel(); //组合对象

        Date selectDate = null;
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_COVARIANCE_OF_DAY);
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            selectDate = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            selectDate = jobTimeRecord.getTriggerTime();
        }

        //获取组合中code
        List<String> fundNetValArrList = fundGroupMapper.findGroupCode(oemId);
        //基金组合
        if (fundNetValArrList != null && fundNetValArrList.size() > 1) {
            //计算基金组合
            this.combinationSelect(fundNetValArrList.toArray(new String[0]), 0, new String[2], 0, TYPE_OF_DAY);

            //查询参数（取值数量）
            Integer number = fundCalculateService.getNumberFromSysConfig(TYPE_OF_DAY);
            //根据code组合查找基金数据
            for (int i = 0; i < codeListOfDay.size(); i++) {
                String[] listArr = codeListOfDay.get(i);
                String codeA = listArr[0];
                String codeB = listArr[1];

                covarianceModel.setCodeA(codeA);
                covarianceModel.setCodeB(codeB);
                covarianceModel.setNavDate(selectDate);
                //根据code组合查找基金数据
                List<CovarianceModel> covarianceModelList = fundNetValMapper.getDataByCodeAndDate(covarianceModel);
                if (CollectionUtils.isEmpty(covarianceModelList)) {
                    continue;
                }
                for (int j = 0; j < covarianceModelList.size(); j++) {
                    CovarianceModel tempCovarianceModel = covarianceModelList.get(j);
                    if (tempCovarianceModel.getNavadjA() == null || tempCovarianceModel.getNavadjB() == null) {
                        continue;
                    }
                    covarianceModel.setNavDate(tempCovarianceModel.getNavDate());
                    covarianceModel.setNavadjA(tempCovarianceModel.getNavadjA());
                    covarianceModel.setNavadjB(tempCovarianceModel.getNavadjB());
                    //计算协方差
                    Double cov = calculateCovariance(j, covarianceModelList, number);
                    logger.debug("协方差cov:" + cov);
                    covarianceModel.setCovariance(cov);

                    //插入基金组合日协方差数据
                    covarianceMapper.insertCovarianceOfDay(covarianceModel);
                }
            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_COVARIANCE_OF_DAY, maxDate, SUCCESSFUL_STATUS);
    }


    /*
     * 计算基金两两组合之间的协方差(周期：周)insert into table:fund_covariance_week
     */
    public Boolean calculateCovarianceOfWeek(int oemId) {
        Boolean doSuccess = true;
        CovarianceModel covarianceModel = new CovarianceModel();//组合对象

        Date selectDate = null;
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_COVARIANCE_OF_WEEK);
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            selectDate = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            selectDate = jobTimeRecord.getTriggerTime();
        }

        List<String> fundNetValArrList = fundGroupMapper.findGroupCode(oemId);
        //基金组合
        if (fundNetValArrList != null && fundNetValArrList.size() > 1) {
            //计算基金组合
            this.combinationSelect(fundNetValArrList.toArray(new String[0]), 0, new String[2], 0, TYPE_OF_WEEK);

            //查询参数（取值数量）
            Integer number = fundCalculateService.getNumberFromSysConfig(TYPE_OF_WEEK);
            //根据code组合查找基金数据
            for (int i = 0; i < codeListOfWeek.size(); i++) {
                String[] listArr = codeListOfWeek.get(i);
                String codeA = listArr[0];
                String codeB = listArr[1];

                covarianceModel.setCodeA(codeA);
                covarianceModel.setCodeB(codeB);
                covarianceModel.setNavDate(selectDate);
                //根据code组合查找基金数据
                List<CovarianceModel> tempCovarianceModelList = fundNetValMapper.getDataByCodeAndDate(covarianceModel);
                //过滤数据
                List<CovarianceModel> covarianceModelList = this.filterData(tempCovarianceModelList, TYPE_OF_WEEK);
                if (CollectionUtils.isEmpty(covarianceModelList)) {
                    continue;
                }
                for (int j = 0; j < covarianceModelList.size(); j++) {
                    CovarianceModel tempCovarianceModel = covarianceModelList.get(j);
                    if (tempCovarianceModel.getNavadjA() == null && tempCovarianceModel.getNavadjB() == null) {
                        continue;
                    }
                    covarianceModel.setNavDate(tempCovarianceModel.getNavDate());
                    covarianceModel.setNavadjA(tempCovarianceModel.getNavadjA());
                    covarianceModel.setNavadjB(tempCovarianceModel.getNavadjB());
                    //计算协方差
                    Double cov = calculateCovariance(j, covarianceModelList, number);
                    logger.debug("cov:" + cov);
                    covarianceModel.setCovariance(cov);

                    //插入基金组合周协方差数据
                    try {
                        Integer effectRow = covarianceMapper.insertCovarianceOfWeek(covarianceModel);
                        if (effectRow == null) {
                            doSuccess = false;
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("插入基金周协方差计算数据失败：fundCalculateData=" + covarianceModel.toString(), e);
                    }
                }
            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_COVARIANCE_OF_WEEK, maxDate, SUCCESSFUL_STATUS);

        return doSuccess;
    }


    /*
     * 计算基金两两组合之间的协方差(周期：月)insert into table:fund_covariance_month
     */
    @Deprecated
    public void calculateCovarianceOfMonth(int oemId) {
        CovarianceModel covarianceModel = new CovarianceModel(); //组合对象

        Date selectDate = null;
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_COVARIANCE_OF_MONTH);
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            selectDate = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            selectDate = jobTimeRecord.getTriggerTime();
        }

        List<String> fundNetValArrList = fundGroupMapper.findGroupCode(oemId);
        //基金组合
        if (fundNetValArrList != null && fundNetValArrList.size() > 1) {
            //计算基金组合
            this.combinationSelect(fundNetValArrList.toArray(new String[0]), 0, new String[2], 0, TYPE_OF_MONTH);

            //查询参数（取值数量）
            Integer number = fundCalculateService.getNumberFromSysConfig(TYPE_OF_MONTH);
            //根据code组合查找基金数据
            for (int i = 0; i < codeListOfMonth.size(); i++) {
                String[] listArr = codeListOfMonth.get(i);
                String codeA = listArr[0];
                String codeB = listArr[1];

                covarianceModel.setCodeA(codeA);
                covarianceModel.setCodeB(codeB);
                covarianceModel.setNavDate(selectDate);
                //根据code组合查找基金数据
                List<CovarianceModel> tempCovarianceModelList = fundNetValMapper.getDataByCodeAndDate(covarianceModel);
                //过滤数据
                List<CovarianceModel> covarianceModelList = this.filterData(tempCovarianceModelList, TYPE_OF_MONTH);
                if (CollectionUtils.isEmpty(covarianceModelList)) {
                    continue;
                }
                for (int j = 0; j < covarianceModelList.size(); j++) {
                    CovarianceModel tempCovarianceModel = covarianceModelList.get(j);
                    if (tempCovarianceModel.getNavadjA() == null && tempCovarianceModel.getNavadjB() == null) {
                        continue;
                    }
                    covarianceModel.setNavDate(tempCovarianceModel.getNavDate());
                    covarianceModel.setNavadjA(tempCovarianceModel.getNavadjA());
                    covarianceModel.setNavadjB(tempCovarianceModel.getNavadjB());
                    //计算协方差
                    Double cov = calculateCovariance(j, covarianceModelList, number);
                    logger.debug("cov:" + cov);
                    covarianceModel.setCovariance(cov);
                    //插入基金组合月协方差数据
                    covarianceMapper.insertCovarianceOfMonth(covarianceModel);
                }
            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_COVARIANCE_OF_MONTH, maxDate, SUCCESSFUL_STATUS);
    }


    /*
     * 计算基金两两组合之间的协方差(周期：年)insert into table:fund_covariance_year
     */
    public void calculateCovarianceOfYear(int oemId) {
        CovarianceModel covarianceModel = new CovarianceModel();//组合对象

        Date selectDate = null;
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = jobTimeService.selectJobTimeRecord(CALCULATE_COVARIANCE_OF_YEAR);
        if (jobTimeRecord == null || jobTimeRecord.getTriggerTime() == null) {
            selectDate = DateUtil.getDateFromFormatStr(START_QUERY_DATE);
        } else {
            selectDate = jobTimeRecord.getTriggerTime();
        }

        List<String> fundNetValArrList = fundGroupMapper.findGroupCode(oemId);
        //基金组合
        if (fundNetValArrList != null && fundNetValArrList.size() > 1) {
            //计算基金组合
            this.combinationSelect(fundNetValArrList.toArray(new String[0]), 0, new String[2], 0, TYPE_OF_YEAR);

            //查询参数（取值数量）
            Integer number = fundCalculateService.getNumberFromSysConfig(TYPE_OF_YEAR);
            //根据code组合查找基金数据
            for (int i = 0; i < codeListOfYear.size(); i++) {
                String[] listArr = codeListOfYear.get(i);
                String codeA = listArr[0];
                String codeB = listArr[1];

                covarianceModel.setCodeA(codeA);
                covarianceModel.setCodeB(codeB);
                covarianceModel.setNavDate(selectDate);
                //根据code组合查找基金数据
                List<CovarianceModel> tempCovarianceModelList = fundNetValMapper.getDataByCodeAndDate(covarianceModel);
                //过滤数据
                List<CovarianceModel> covarianceModelList = this.filterData(tempCovarianceModelList, TYPE_OF_YEAR);
                if (CollectionUtils.isEmpty(covarianceModelList)) {
                    continue;
                }
                for (int j = 0; j < covarianceModelList.size(); j++) {
                    CovarianceModel tempCovarianceModel = covarianceModelList.get(j);
                    if (tempCovarianceModel.getNavadjA() == null && tempCovarianceModel.getNavadjB() == null) {
                        continue;
                    }
                    covarianceModel.setNavDate(tempCovarianceModel.getNavDate());
                    covarianceModel.setNavadjA(tempCovarianceModel.getNavadjA());
                    covarianceModel.setNavadjB(tempCovarianceModel.getNavadjB());
                    //计算协方差
                    Double cov = calculateCovariance(j, covarianceModelList, number);
                    logger.debug("cov:" + cov);
                    covarianceModel.setCovariance(cov);
                    //插入基金组合年协方差数据
                    covarianceMapper.insertCovarianceOfYear(covarianceModel);
                }
            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate = fundNetValMapper.getMaxNavDateByDate(selectDate);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, FUND_CALCULATE_JOB, CALCULATE_COVARIANCE_OF_YEAR, maxDate, SUCCESSFUL_STATUS);
    }


    /**
     * 组合选择
     *
     * @param dataList    待选列表
     * @param dataIndex   待选开始索引
     * @param resultList  前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     */
    private void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex, String type) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        String tempType = type;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            logger.debug(Arrays.asList(resultList).toString());
            if (TYPE_OF_DAY.equals(type)) {
                codeListOfDay.add(resultList.clone());
            } else if (TYPE_OF_WEEK.equals(type)) {
                codeListOfWeek.add(resultList.clone());
            } else if (TYPE_OF_MONTH.equals(type)) {
                codeListOfMonth.add(resultList.clone());
            } else if (TYPE_OF_YEAR.equals(type)) {
                codeListOfYear.add(resultList.clone());
            }
            return;
        }

        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1, tempType);
        }
    }


    /*
     * 过滤数据
     */
    public List<CovarianceModel> filterData(List<CovarianceModel> covModelList, String type) {
        List<CovarianceModel> tempList = new ArrayList<>();
        if (CollectionUtils.isEmpty(covModelList)) {
            return tempList;
        }

        for (int i = 0; i < covModelList.size(); i++) {
            int tempNum = i;
            CovarianceModel covarianceModel = covModelList.get(i);
            if (covarianceModel != null && covarianceModel.getNavDate() != null
                    && covarianceModel.getNavadjA() != null && covarianceModel.getNavadjB() != null) {
                Date navLatestDate = covarianceModel.getNavDate();
                if (TYPE_OF_WEEK.equals(type)) {
                    //取周五数据
                    CovarianceModel tempCovarianceModel = this.getFriData(covModelList, navLatestDate, tempNum);
                    if (tempCovarianceModel != null) {
                        tempList.add(tempCovarianceModel);
                    }
                } else if (TYPE_OF_MONTH.equals(type)) {
                    //取每月底数据
                    CovarianceModel tempCovarianceModel = this.getMonthData(covModelList, navLatestDate, tempNum);
                    if (tempCovarianceModel != null) {
                        tempList.add(tempCovarianceModel);
                    }
                } else if (TYPE_OF_YEAR.equals(type)) {
                    //取每年年底数据
                    CovarianceModel tempCovarianceModel = this.getYearData(covModelList, navLatestDate, tempNum);
                    if (tempCovarianceModel != null) {
                        tempList.add(tempCovarianceModel);
                    }
                }
            }
        }

        if (TYPE_OF_MONTH.equals(type)) {
            monthMap = new HashMap<>();
        } else if (TYPE_OF_YEAR.equals(type)) {
            yearMap = new HashMap<>();
        }

        return tempList;
    }


    /*
     * 取周五数据，若无则往前递推
     */
    public CovarianceModel getFriData(List<CovarianceModel> covModelList, Date navLatestDate, int tempNum) {
        CovarianceModel covarianceModel = covModelList.get(tempNum);
        String[] navLatestDateArr = navLatestDate.toString().split(" ");
        if (navLatestDateArr.length != 6) {
            return null;
        }

        String weekday = navLatestDateArr[0];
        if (WEEKDAY_OF_FRI.equals(weekday)) {
            countOfWeek = 0;
            return covarianceModel;
        }

        ++countOfWeek;
        if (countOfWeek < 7) { //一周七天
            return null;
        } else {
            countOfWeek = 0;
            CovarianceModel tempCovarianceModel = this.getEffectData(tempNum - 2, covModelList); //一周之内找不到周五数据，则取该天有效数据
            return tempCovarianceModel;
        }
    }

    /*
     * 取每月月底数据
     */
    public CovarianceModel getMonthData(List<CovarianceModel> covModelList, Date navLatestDate, int tempNum) {
        CovarianceModel covarianceModel = covModelList.get(tempNum);
        String navLatestDateStr = sdf.format(navLatestDate);
        String[] navLatestDateArr = navLatestDateStr.split("-");
        if (navLatestDateArr.length != 3) {
            return null;
        }
        String tag = navLatestDateArr[0] + navLatestDateArr[1];
        if (monthMap.get(tag) == null) {
            monthMap.put(tag, covarianceModel);
            return covarianceModel;
        }
        return null;
    }

    /*
     * 取每年年底数据
     */
    public CovarianceModel getYearData(List<CovarianceModel> covModelList, Date navLatestDate, int tempNum) {
        CovarianceModel covarianceModel = covModelList.get(tempNum);
        String navLatestDateStr = sdf.format(navLatestDate);
        String[] navLatestDateArr = navLatestDateStr.split("-");
        if (navLatestDateArr.length != 3) {
            return null;
        }
        String tag = navLatestDateArr[0];
        if (yearMap.get(tag) == null) {
            yearMap.put(tag, covarianceModel);
            return covarianceModel;
        }
        return null;
    }

    /*
     *取到有效净值数据（没有则往之前时间递推）
     */
    public CovarianceModel getEffectData(int i, List<CovarianceModel> covModelList) {
        if (0 <= i && i < covModelList.size()) {
            CovarianceModel covarianceModel = covModelList.get(i);
            while (covarianceModel == null || covarianceModel.getNavadjA() == null || covarianceModel.getNavadjB() == null) {
                int temp = i++;
                if (temp < covModelList.size()) {
                    covarianceModel = covModelList.get(temp);
                } else {
                    covarianceModel = null; //fundList 遍历结束仍无有效数据，赋值为null,结束循环
                    break;
                }
            }
            return covarianceModel;
        }
        return null;
    }


    /*
     * 计算协方差方法
     */
    public Double calculateCovariance(int j, List<CovarianceModel> covarianceModelList, int number) {
        List<Double> listA = new ArrayList<>();
        List<Double> listB = new ArrayList<>();
        Double cov = 0d;
        List<Double> yieldRatioArrA = new ArrayList<>();
        List<Double> yieldRatioArrB = new ArrayList<>();
        //取值
        while (listA.size() < number) {
            CovarianceModel tempCovarianceModel = covarianceModelList.get(j);
            if (tempCovarianceModel.getNavadjA() != null && tempCovarianceModel.getNavadjB() != null) {
                listA.add(tempCovarianceModel.getNavadjA().doubleValue());
                listB.add(tempCovarianceModel.getNavadjB().doubleValue());
                if (listA.size() > 1) {
                    Double yieldRatioA = fundCalculateService.calculateYieldRatio(listA.get(listA.size() - 2), listA.get(listA.size() - 1));
                    yieldRatioArrA.add(yieldRatioA);

                    Double yieldRatioB = fundCalculateService.calculateYieldRatio(listB.get(listB.size() - 2), listB.get(listB.size() - 1));
                    yieldRatioArrB.add(yieldRatioB);
                }
            }
            j++;
            if (j >= covarianceModelList.size()) {
                break;
            }
        }

        if (yieldRatioArrA.size() > 1) {
            cov = getCovariance(yieldRatioArrA.toArray(new Double[0]), yieldRatioArrB.toArray(new Double[0]));
        }
        return cov;
    }


    /*
     * 计算协方差
     */
    public Double getCovariance(Double[] xArray, Double[] yArray) {
        Covariance covariance = new Covariance();
        double[] Xd = new double[xArray.length];
        for (int i = 0; i < xArray.length; i++) {
            Xd[i] = xArray[i];
        }

        double[] Yd = new double[yArray.length];
        for (int i = 0; i < yArray.length; i++) {
            Yd[i] = yArray[i];
        }

        //调用Common Math 计算协方差, 且 须 （Yd）Xd.length>=2
        Double covarianceVal = covariance.covariance(Xd, Yd);
        return covarianceVal;
    }

}
