package com.shellshellfish.aaas.assetallocation.neo.job.service;

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

    /*
     * 每日接口
     */

    @Scheduled(fixedDelay = 5000)
    public void DailyJobSchedule(){

        System.out.println("定时任务启动中...");

    }



}
