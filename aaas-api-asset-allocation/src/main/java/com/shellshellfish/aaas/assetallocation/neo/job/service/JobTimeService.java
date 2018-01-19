package com.shellshellfish.aaas.assetallocation.neo.job.service;

import com.shellshellfish.aaas.assetallocation.neo.job.entity.JobTimeRecord;
import com.shellshellfish.aaas.assetallocation.neo.mapper.JobTimeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
    public JobTimeRecord selectJobTimeRecord(String triggerName) {
        JobTimeRecord jobTimeRecord = null;
        List<JobTimeRecord> jobTimeRecordList = jobTimeRecordMapper.getJobTimeRecord(triggerName);
        if (!CollectionUtils.isEmpty(jobTimeRecordList)) {
            jobTimeRecord = jobTimeRecordList.get(0);
        }
        return jobTimeRecord;
    }

    public void saveOrUpdateJobTimeRecord(JobTimeRecord jobTimeRecord, String jobName, String triggerName, Date triggerTime, Integer status) {
        JobTimeRecord jobTimeRecordTemp = new JobTimeRecord();
        jobTimeRecordTemp.setTriggerName(triggerName);
        jobTimeRecordTemp.setTriggerTime(triggerTime);
        jobTimeRecordTemp.setStatus(status);
        jobTimeRecordTemp.setUpdateTime(new Date());
        if (jobTimeRecord == null) {
            jobTimeRecordTemp.setJobName(jobName);
            jobTimeRecordTemp.setCreateTime(new Date());
            this.insertJobTimeRecord(jobTimeRecordTemp);
        } else {
            this.updateJobTimeRecord(jobTimeRecordTemp);
        }
    }

    /*
     * 根据triggerName插入上次执行时间triggerTime
     */
    public Integer insertJobTimeRecord(JobTimeRecord jobTimeRecord){
        Integer effectRow = jobTimeRecordMapper.insertJobTimeRecord(jobTimeRecord);
        return effectRow;
    }

    /*
     * 根据triggerName更新上次执行时间triggerTime
     */
    public Integer updateJobTimeRecord(JobTimeRecord jobTimeRecord){
        Integer effectRow = jobTimeRecordMapper.updateJobTimeRecord(jobTimeRecord);
        return effectRow;
    }

}
