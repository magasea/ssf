package com.shellshellfish.aaas.assetallocation.entity;

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
    private String start_time;//开始时间
    private String end_time;//结束时间
    private Date risk_controller_last_mod_time;//最后修改时间

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

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
