package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Created by webrx on 2017/12/12 0012.
 */
public class FundGroupHistory {
    private String fund_group_sub_id;//基金组合分组id
    private String fund_group_id;//基金组合id
    private double income_num;//收益率
    private double maximum_retracement;//最大回撤
    private Date time;//时间

    public String getFund_group_sub_id() {
        return fund_group_sub_id;
    }

    public void setFund_group_sub_id(String fund_group_sub_id) {
        this.fund_group_sub_id = fund_group_sub_id;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public double getIncome_num() {
        return income_num;
    }

    public void setIncome_num(double income_num) {
        this.income_num = income_num;
    }

    public double getMaximum_retracement() {
        return maximum_retracement;
    }

    public void setMaximum_retracement(double maximum_retracement) {
        this.maximum_retracement = maximum_retracement;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public FundGroupHistory() {
    }

    public FundGroupHistory(String fund_group_id, String fund_group_sub_id, double income_num, double maximum_retracement, Date time) {
        this.fund_group_sub_id = fund_group_sub_id;
        this.fund_group_id = fund_group_id;
        this.income_num = income_num;
        this.maximum_retracement = maximum_retracement;
        this.time = time;
    }

    @Override
    public String toString() {
        return "FundGroupHistory{" +
                "fund_group_sub_id='" + fund_group_sub_id + '\'' +
                ", fund_group_id='" + fund_group_id + '\'' +
                ", income_num=" + income_num +
                ", maximum_retracement=" + maximum_retracement +
                ", time=" + time +
                '}';
    }
}
