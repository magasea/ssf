package com.shellshellfish.aaas.assetallocation.neo.job.service;

import com.shellshellfish.aaas.assetallocation.neo.service.CovarianceCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.DailyFundService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /*
     * 调用每日接口
     */
//    @Scheduled(fixedDelay = 5000)
    public void insertDailyFundJobSchedule(){

        logger.info("调用每日接口获取数据定时任务启动..."+sdf.format(new Date()));
        Boolean flag=false;
        try{
            flag=dailyFundService.insertDailyFund();
        }catch(Exception e){
            logger.info("调用每日接口获取数据定时任务启动失败..."+sdf.format(new Date()));
        }

        if(flag){
            logger.info("调用每日接口获取数据定时任务启动成功..."+sdf.format(new Date()));
        }else {
            logger.info("调用每日接口获取数据定时任务启动失败..."+sdf.format(new Date()));
        }

    }


    /*
     * 计算每周相关数据
     */
//    @Scheduled(fixedDelay = 5000)
    public void calculateDataOfWeekJobSchedule(){

        logger.info("计算每周相关数据定时任务启动..."+sdf.format(new Date()));

        //计算每周风险率以及收益率等数据
        Boolean flag1=false;
        try{
             flag1=fundCalculateService.calculateDataOfWeek();
        }catch(Exception e){
            logger.info("计算每周收益率以及风险率数据 定时任务启动失败..."+sdf.format(new Date()));
        }

        if(flag1){
            logger.info("计算每周收益率以及风险率数据 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            logger.info("计算每周收益率以及风险率数据 定时任务启动失败..."+sdf.format(new Date()));
        }


        //计算每周协方差等数据
        Boolean flag2=false;
        try{
            flag2=cvarianceCalculateService.calculateCovarianceOfWeek();
        }catch(Exception e){
            logger.info("计算每周协方差等数据 定时任务启动失败..."+sdf.format(new Date()));
        }

        if(flag2){
            logger.info("计算每周协方差等数据 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            logger.info("计算每周协方差等数据 定时任务启动失败..."+sdf.format(new Date()));
        }

    }


    /*
     * 计算组合数据
     */
//    @Scheduled(fixedDelay = 5000)
    public void insertFundGroupDataJobSchedule(){

        logger.info("计算组合数据定时任务启动..."+sdf.format(new Date()));

        //计算组合数据
        Boolean flag=false;
        try{
            flag=fundGroupDataService.insertFundGroupData();
        }catch(Exception e){
            logger.info("计算组合数据 定时任务启动失败..."+sdf.format(new Date()));
        }

        if(flag){
            logger.info("计算组合数据 定时任务启动成功..."+sdf.format(new Date()));
        }else {
            logger.info("计算组合数据 定时任务启动失败..."+sdf.format(new Date()));
        }

    }




//    @Scheduled(fixedDelay = 5000)
    public void DailyJobSchedule(){

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(1513699200 * 1000);
        String str = sdf.format(date);
        System.out.println(str);


    }


}
