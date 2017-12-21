package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Created by webrx on 2017/12/19 0019.
 */
public class EfficientFrontier {
    private String id;//

    private String fund_group_id;//基金组合id
    private String fund_group_sub_id;//基金组合分组id
    private double risk_num;//风险率
    private double income_num;//收益率

    private String efficient_frontier_id;//有效前沿线表id
    private String fund_code;//基金id
    private double fund_proportion;//基金权重

    private Date last_mod_time;//最后修改时间

    public Date getLast_mod_time() {
        return last_mod_time;
    }

    public void setLast_mod_time(Date last_mod_time) {
        this.last_mod_time = last_mod_time;
    }

    public String getFund_group_sub_id() {
        return fund_group_sub_id;
    }

    public void setFund_group_sub_id(String fund_group_sub_id) {
        this.fund_group_sub_id = fund_group_sub_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEfficient_frontier_id() {
        return efficient_frontier_id;
    }

    public void setEfficient_frontier_id(String efficient_frontier_id) {
        this.efficient_frontier_id = efficient_frontier_id;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public double getRisk_num() {
        return risk_num;
    }

    public void setRisk_num(double risk_num) {
        this.risk_num = risk_num;
    }

    public double getIncome_num() {
        return income_num;
    }

    public void setIncome_num(double income_num) {
        this.income_num = income_num;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public double getFund_proportion() {
        return fund_proportion;
    }

    public void setFund_proportion(double fund_proportion) {
        this.fund_proportion = fund_proportion;
    }
}
