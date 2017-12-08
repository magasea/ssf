package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class FundGroup extends Strategy{
    private String fund_group_name;//基金組合名称
    private String status;//是否启用
    private double income_min_num;//收益最小值
    private double income_max_num;//收益最大值
    private double risk_min_num;//风险最小值
    private double risk_max_num;//风险最大值
    private Date start_time;//启用时间
    private Date stop_time;//禁用时间
    private Date group_add_time;//添加时间
    private Date group_last_mod_time;//最后一次修改时间

    public double getIncome_min_num() {
        return income_min_num;
    }

    public void setIncome_min_num(double income_min_num) {
        this.income_min_num = income_min_num;
    }

    public double getIncome_max_num() {
        return income_max_num;
    }

    public void setIncome_max_num(double income_max_num) {
        this.income_max_num = income_max_num;
    }

    public double getRisk_min_num() {
        return risk_min_num;
    }

    public void setRisk_min_num(double risk_min_num) {
        this.risk_min_num = risk_min_num;
    }

    public double getRisk_max_num() {
        return risk_max_num;
    }

    public void setRisk_max_num(double risk_max_num) {
        this.risk_max_num = risk_max_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getStop_time() {
        return stop_time;
    }

    public void setStop_time(Date stop_time) {
        this.stop_time = stop_time;
    }

    public Date getGroup_add_time() {
        return group_add_time;
    }

    public void setGroup_add_time(Date group_add_time) {
        this.group_add_time = group_add_time;
    }

    public Date getGroup_last_mod_time() {
        return group_last_mod_time;
    }

    public void setGroup_last_mod_time(Date group_last_mod_time) {
        this.group_last_mod_time = group_last_mod_time;
    }

    public String getFund_group_name() {
        return fund_group_name;
    }

    public void setFund_group_name(String fund_group_name) {
        this.fund_group_name = fund_group_name;
    }
}
