package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/11/29.
 */
public class RiskController {
    private int id;//
    private String fund_group_id;//基金组合id
    private String name;//股灾，熊市
    private double risk_controller;//风险控制
    private double benchmark;//比较基准
    private Date risk_controller_last_mod_time;//最后修改时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRisk_controller() {
        return risk_controller;
    }

    public void setRisk_controller(double risk_controller) {
        this.risk_controller = risk_controller;
    }

    public double getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(double benchmark) {
        this.benchmark = benchmark;
    }

    public Date getRisk_controller_last_mod_time() {
        return risk_controller_last_mod_time;
    }

    public void setRisk_controller_last_mod_time(Date risk_controller_last_mod_time) {
        this.risk_controller_last_mod_time = risk_controller_last_mod_time;
    }
}
