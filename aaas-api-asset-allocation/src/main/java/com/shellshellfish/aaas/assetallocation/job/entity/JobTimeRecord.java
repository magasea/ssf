package com.shellshellfish.aaas.assetallocation.job.entity;

import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/20
 * Desc:Job 运行记录 映射
 */
public class JobTimeRecord {
    private Integer id;//实体Id
    private String jobName;//job名称
    private String triggerName;//trigger名称
    private Date triggerTime;//job处理数据的时间记录
    private Date createTime;//记录创建时间
    private Date updateTime;//记录更新时间
    private Integer status;//执行状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobTimeRecord{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerTime=" + triggerTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                '}';
    }
}
