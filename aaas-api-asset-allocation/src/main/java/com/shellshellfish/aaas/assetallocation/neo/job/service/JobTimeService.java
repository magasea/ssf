package com.shellshellfish.aaas.assetallocation.neo.job.service;

import com.shellshellfish.aaas.assetallocation.neo.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.neo.mapper.JobTimeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/20
 * Desc:job运行记录
 */
@Service
public class JobTimeService {

    @Autowired
    JobTimeRecordMapper jobTimeRecordMapper;
    /*
     * 根据triggerName查询上次执行时间triggerTime
     */
    public JobTimeRecord selectJobTimeRecord(String triggerName){
        JobTimeRecord jobTimeRecord=null;

        List<JobTimeRecord> jobTimeRecordList=jobTimeRecordMapper.getJobTimeRecord(triggerName);
        if(jobTimeRecordList!=null && jobTimeRecordList.size()>0){
            jobTimeRecord=jobTimeRecordList.get(0);
        }
        return jobTimeRecord;

    }

    /*
     * 根据triggerName插入上次执行时间triggerTime
     */
    public Integer insertJobTimeRecord(JobTimeRecord jobTimeRecord){

        Integer integer=jobTimeRecordMapper.insertJobTimeRecord(jobTimeRecord);

        return integer;

    }

    /*
     * 根据triggerName更新上次执行时间triggerTime
     */
    public Integer updateJobTimeRecord(JobTimeRecord jobTimeRecord){


        Integer integer=jobTimeRecordMapper.updateJobTimeRecord(jobTimeRecord);

        return integer;

    }


}
