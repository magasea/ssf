package com.shellshellfish.aaas.assetallocation.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class FundGroup extends Strategy{
    private String fund_group_name;//基金組合名称
    /*private String status;//是否启用
    private double income_min_num;//收益最小值
    private double income_max_num;//收益最大值
    private double risk_min_num;//风险最小值
    private double risk_max_num;//风险最大值
    private Date start_time;//启用时间
    private Date stop_time;//禁用时间
    private Date group_add_time;//添加时间*/
    private Date group_last_mod_time;//最后一次修改时间

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
