package com.shellshellfish.aaas.assetallocation.neo.job.service;

import com.shellshellfish.aaas.assetallocation.neo.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.neo.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/12/26
 * Desc:定义 job 执行计划
 */
@Component
public class JobScheduleService {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger= LoggerFactory.getLogger(JobScheduleService.class);

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

    /*
     * 调用每日接口
     */
    @Scheduled(cron = "0 0 1 * * ?")    //每天凌晨1点执行
    public void insertDailyFundJobSchedule(){

        logger.info("调用每日接口获取数据定时任务启动..."+sdf.format(new Date()));
        Boolean flag=false;
        Integer status=SUCCESSFUL_STATUS;
        try{
            flag=dailyFundService.insertDailyFund();
        }catch(Exception e){
            logger.error("调用每日接口获取数据定时任务启动失败..."+sdf.format(new Date()),e);
        }

        if(flag){
            logger.info("调用每日接口获取数据定时任务启动成功..."+sdf.format(new Date()));
        }else {
            status=FAILURED_STATUS;
            logger.info("调用每日接口获取数据定时任务启动失败..."+sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        JobScheduleRecord(INSERT_DAILYFUND_JOBSCHEDULE,status);

    }


    /*
     * 计算每周收益率以及风险率数据
     */
//    @Scheduled(cron = "0 0 2 28 * ?")       //每月 28 号  凌晨 2 点执行
    public void calculateYieldAndRiskOfWeekJobSchedule(){

        logger.info("计算每周收益率以及风险率数据定时任务启动..."+sdf.format(new Date()));
        Boolean flag=false;
        Integer status=SUCCESSFUL_STATUS;
        //计算每周风险率以及收益率等数据
        try{
             flag=fundCalculateService.calculateDataOfWeek();
        }catch(Exception e){
            logger.error("计算每周收益率以及风险率数据 定时任务启动失败..."+sdf.format(new Date()),e);
        }

        if(flag){
            logger.info("计算每周收益率以及风险率数据 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            status=FAILURED_STATUS;
            logger.info("计算每周收益率以及风险率数据 定时任务启动失败..."+sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        JobScheduleRecord(CALCULATE_YIELDANDRISKOFWEEK_JOBSCHEDULE,status);
    }


    /*
     * 计算产品组合数据(产品组合风险率、收益率、权重)
     */
//    @Scheduled(cron = "0 0 22 28 * ?")        //每月 28 号  晚上 10 点执行
    public void insertFundGroupDataJobSchedule(){

        logger.info("计算组合数据(产品组合风险率、收益率、权重)定时任务启动..."+sdf.format(new Date()));
        Boolean flag=false;
        Integer status=SUCCESSFUL_STATUS;
        //计算组合数据
        try{
            flag=fundGroupDataService.insertFundGroupData();
        }catch(Exception e){
            logger.error("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动失败..."+sdf.format(new Date()),e);
        }

        if(flag){
            logger.info("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            status=FAILURED_STATUS;
            logger.info("计算组合数据(产品组合风险率、收益率、权重) 定时任务启动失败..."+sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        JobScheduleRecord(INSERT_FUNDGROUPDATA_JOBSCHEDULE,status);
    }


    /*
     * 计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比
     */
//    @Scheduled(cron = "0 0 5 * * ?")        //每天 凌晨 5 点执行
    public void getAllIdAndSubIdJobSchedule(){

        logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动..."+sdf.format(new Date()));

        Boolean flag=true;
        Integer status=SUCCESSFUL_STATUS;
        try{
            fundGroupService.getAllIdAndSubId();
        }catch(Exception e){
            flag=false;
            logger.error("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动失败..."+sdf.format(new Date()),e);
        }

        if(flag){
            logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            status=FAILURED_STATUS;
            logger.info("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比 定时任务启动失败..."+sdf.format(new Date()));
        }
        //记录 定时任务执行的状态
        JobScheduleRecord(GET_ALLIDANDSUBID_JOBSCHEDULE,status);

    }


    //记录定时任务执行的状态
    public void JobScheduleRecord(String triggerName,Integer status){
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(triggerName);
        JobTimeRecord jobTimeRecordTemp=new JobTimeRecord();
        if(jobTimeRecord==null){

            jobTimeRecordTemp.setJobName(JOB_SCHEDULE_NAME);
            jobTimeRecordTemp.setTriggerName(triggerName);
            jobTimeRecordTemp.setStatus(status);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(triggerName);
            jobTimeRecordTemp.setStatus(status);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }
    }




}
