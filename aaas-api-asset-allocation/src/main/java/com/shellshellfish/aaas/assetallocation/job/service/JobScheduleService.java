package com.shellshellfish.aaas.assetallocation.job.service;


import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shellshellfish.aaas.assetallocation.entity.Interval;
import com.shellshellfish.aaas.assetallocation.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.returnType.ReturnType;
import com.shellshellfish.aaas.assetallocation.service.impl.*;
import com.shellshellfish.aaas.assetallocation.util.ConstantUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.shellshellfish.aaas.assetallocation.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/12/26
 * Desc:定义 job 执行计划
 */
@Component
public class JobScheduleService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(JobScheduleService.class);

    @Autowired
    private DailyFundService dailyFundService;
    @Autowired
    private FundCalculateService fundCalculateService;
    @Autowired
    private CovarianceCalculateService cvarianceCalculateService;
    @Autowired
    private FundGroupDataService fundGroupDataService;
    @Autowired
    private FundGroupService fundGroupService;
    @Autowired
    private JobTimeService jobTimeService;

    @Autowired
    private FundGroupMapper fundGroupMapper;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    MongoDatabase mongoDatabase;

    @Value("${spring.data.mongodb.collection}")
    String collectionName;

    /*
     * 调用每日接口
     */
//    @Scheduled(cron = "0 0 1 * * ?")    //每天凌晨1点执行
    public void insertDailyFundJobSchedule(int oemId) {
        logger.info("调用每日接口获取数据定时任务启动..." + sdf.format(new Date()));
        Boolean doSuccess = false;
        Integer status = SUCCESSFUL_STATUS;
        try {
            doSuccess = dailyFundService.insertDailyFund(oemId);
        } catch (Exception e) {
            logger.error("调用每日接口获取数据定时任务启动失败..." + sdf.format(new Date()), e);
        }

        if (doSuccess) {
            logger.info("调用每日接口获取数据定时任务启动成功..." + sdf.format(new Date()));
        } else {
            status = FAILURED_STATUS;
            logger.info("调用每日接口获取数据定时任务启动失败..." + sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        saveJobScheduleRecord(INSERT_DAILYFUND_JOBSCHEDULE, status);
    }

    /*
     * 计算每周收益率以及风险率数据
     */
//    @Scheduled(cron = "0 0 2 * 5 ?")       //每周五  凌晨 2 点执行
    public void calculateYieldAndRiskOfWeekJobSchedule(int oemId) {
        logger.info("计算每周收益率以及风险率数据定时任务启动..." + sdf.format(new Date()));
        Boolean doSuccess = false;
        Integer status = SUCCESSFUL_STATUS;
        //计算每周风险率以及收益率等数据
        try {
            doSuccess = fundCalculateService.calculateDataOfWeek(oemId);
        } catch (Exception e) {
            logger.error("计算每周收益率以及风险率数据 定时任务启动失败..." + sdf.format(new Date()), e);
        }

        if (doSuccess) {
            logger.info("计算每周收益率以及风险率数据 定时任务启动成功..." + sdf.format(new Date()));
        } else {
            status = FAILURED_STATUS;
            logger.info("计算每周收益率以及风险率数据 定时任务启动失败..." + sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        saveJobScheduleRecord(CALCULATE_YIELDANDRISKOFWEEK_JOBSCHEDULE, status);
    }

    /*
     * 计算产品组合数据(产品组合风险率、收益率、权重)
     */
//    @Scheduled(cron = "0 0 22 28 * ?")        //每月 28 号  晚上 10 点执行
    public void insertFundGroupDataJobSchedule(int oemId) {
        logger.info("计算组合数据(产品组合风险率、收益率、权重)定时任务启动..." + sdf.format(new Date()));
        Boolean doSuccess = false;
        Integer status = SUCCESSFUL_STATUS;
        //计算组合数据
        try {
            doSuccess = fundGroupDataService.insertFundGroupData(oemId);
        } catch (Exception e) {
            logger.error("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动失败..." + sdf.format(new Date()), e);
        }

        if (doSuccess) {
            logger.info("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动成功..." + sdf.format(new Date()));
        } else {
            status = FAILURED_STATUS;
            logger.info("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动失败..." + sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        saveJobScheduleRecord(INSERT_FUNDGROUPDATA_JOBSCHEDULE, status);
    }

    /*
     * 计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比
     */
//    @Scheduled(cron = "0 0 4 * * ?")				//每天 凌晨 4 点执行
    public void getAllIdAndSubIdJobSchedule(int oemId) {
        logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动..." + sdf.format(new Date()));
        Boolean doSuccess = true;
        Integer status = SUCCESSFUL_STATUS;
        try {
            fundGroupService.getAllIdAndSubId(oemId);
        } catch (Exception e) {
            doSuccess = false;
            logger.error("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动失败..." + sdf.format(new Date()), e);
        }

        if (doSuccess) {
            logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动成功..." + sdf.format(new Date()));
        } else {
            status = FAILURED_STATUS;
            logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动失败..." + sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        saveJobScheduleRecord(GET_ALLIDANDSUBID_JOBSCHEDULE, status);
    }

    /*
     * 更新所有基金组合的最大亏损额
     */
//    @Scheduled(cron = "0 0 6 * * ?")        //每天 凌晨 6 点执行
    public void updateAllMaximumLossesJobSchedule(int oemId) {
        logger.info("计算 更新所有基金组合的最大亏损额 定时任务启动..." + sdf.format(new Date()));
        Boolean doSuccess = true;
        Integer status = SUCCESSFUL_STATUS;
        try {
            fundGroupService.updateAllMaximumLosses(oemId);
        } catch (Exception e) {
            doSuccess = false;
            logger.error("计算 更新所有基金组合的最大亏损额 定时任务启动失败..." + sdf.format(new Date()), e);
        }

        if (doSuccess) {
            logger.info("计算 更新所有基金组合的最大亏损额 定时任务启动成功..." + sdf.format(new Date()));
        } else {
            status = FAILURED_STATUS;
            logger.info("计算 更新所有基金组合的最大亏损额 定时任务启动失败..." + sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        saveJobScheduleRecord(UPDATE_ALLMAXIMUMLOSSES_JOBSCHEDULE, status);
    }

    /*
     * 组合收益率(最大回撤)走势图-自组合基金成立以来的每天
     */
//    @Scheduled(cron = "0 30 6 * * ?")        //每天 凌晨 6:30 点 执行
    public void getFundGroupIncomeAllJobSchedule(int oemId) {
        try {
            List<Date> dateList = fundGroupService.getRecentDateInfo(oemId);
            List<Date> arrayList = new ArrayList<>();
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            logger.info(collectionName + "集合选择成功");

            List<Document> documents = new ArrayList<>();
            String returnType = "income";
//            String subfix = SUB_GROUP_ID_SUBFIX;

            List<Interval> intervals = fundGroupMapper.getGroupIdAndSubId(oemId);

            for (Interval interval : intervals){
                arrayList = new ArrayList<>();
                arrayList.addAll(dateList);
                String key = interval.getFund_group_id() + "_" + interval.getId();
                ReturnType rt = fundGroupService.getFundGroupIncomeAll(interval.getFund_group_id(), interval.getId(), oemId,
                        returnType, arrayList);
                Document document = returnTypeToDocument(key, oemId,  rt);
                documents.add(document);
            }

//            for (int index = 1; index <= ConstantUtil.FUND_GROUP_COUNT; index++) {
//                arrayList = new ArrayList<>();
//                arrayList.addAll(dateList);

//                String groupId = String.valueOf(index);
//                String subGroupId = String.valueOf(index + subfix);
//                String key = groupId + "_" + subGroupId;
//                ReturnType rt = fundGroupService.getFundGroupIncomeAll(groupId, subGroupId, oemId,
//                    returnType, arrayList);
//                Document document = returnTypeToDocument(key, oemId,  rt);
//                documents.add(document);
//            }
            // 删除所有符合条件的文档
            Document filter = new Document(); 
            filter.append("title", collectionName);
            filter.append("oemId", oemId);
//            collection.deleteMany(Filters.eq("title", collectionName), Filters.eq("oemId", oemId));
            collection.deleteMany(filter);
            collection.insertMany(documents);
            logger.info("文档插入成功");
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ":" + e.getMessage());
        }
    }

    private Document returnTypeToDocument(String key, int oemId, ReturnType rt) {
        String _total = JSON.toJSONString(rt.get_total());
        String _items = JSON.toJSONString(rt.get_items());
        String name = JSON.toJSONString(rt.getName());
        String _links = JSON.toJSONString(rt.get_links());
        String maxMinMap = JSON.toJSONString(rt.getMaxMinMap());
        String maxMinBenchmarkMap = JSON.toJSONString(rt.getMaxMinBenchmarkMap());
        String expectedIncomeSizeMap = JSON.toJSONString(rt.getExpectedIncomeSizeMap());
        String highPercentMaxIncomeSizeMap = JSON.toJSONString(rt.getHighPercentMaxIncomeSizeMap());
        String highPercentMinIncomeSizeMap = JSON.toJSONString(rt.getHighPercentMinIncomeSizeMap());
        String lowPercentMaxIncomeSizeMap = JSON.toJSONString(rt.getLowPercentMaxIncomeSizeMap());
        String lowPercentMinIncomeSizeMap = JSON.toJSONString(rt.getLowPercentMinIncomeSizeMap());
        String _schemaVersion = JSON.toJSONString(rt.get_schemaVersion());
        String _serviceId = JSON.toJSONString(rt.get_serviceId());

        Document document = new Document("title", MONGO_DB_COLLECTION).
                append("key", key).
                append("_total", _total).
                append("_items", _items).
                append("name", name).
                append("_links", _links).
                append("maxMinMap", maxMinMap).
                append("maxMinBenchmarkMap", maxMinBenchmarkMap).
                append("expectedIncomeSizeMap", expectedIncomeSizeMap).
                append("highPercentMaxIncomeSizeMap", highPercentMaxIncomeSizeMap).
                append("highPercentMinIncomeSizeMap", highPercentMinIncomeSizeMap).
                append("lowPercentMaxIncomeSizeMap", lowPercentMaxIncomeSizeMap).
                append("lowPercentMinIncomeSizeMap", lowPercentMinIncomeSizeMap).
                append("_schemaVersion", _schemaVersion).
                append("_serviceId", _serviceId).
		        append("oemId", oemId);

        return document;
    }

    //记录定时任务执行的状态
    private void saveJobScheduleRecord(String triggerName, Integer status) {
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord = jobTimeService.selectJobTimeRecord(triggerName);
        jobTimeService.saveOrUpdateJobTimeRecord(jobTimeRecord, JOB_SCHEDULE_NAME, triggerName, null, status);
    }

}
