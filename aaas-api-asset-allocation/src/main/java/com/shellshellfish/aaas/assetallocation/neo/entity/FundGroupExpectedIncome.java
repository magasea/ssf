package com.shellshellfish.aaas.assetallocation.neo.entity;

/**
 * Created by webrx on 2017/12/14 0014.
 */
public class FundGroupExpectedIncome {
    private String fund_group_id;//基金组合id
    private String fund_group_sub_id;//基金组合分组id
    private float expected_income;//预期收益
    private float high_percent_max_income;//高概率最高收益
    private float high_percent_min_income;//高概率最低收益
    private float low_percent_max_income;//低概率最高收益
    private float low_percent_min_income;//低概率最低收益
    private String income_mounth_time;//收益月份

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public String getFund_group_sub_id() {
        return fund_group_sub_id;
    }

    public void setFund_group_sub_id(String fund_group_sub_id) {
        this.fund_group_sub_id = fund_group_sub_id;
    }

    public float getExpected_income() {
        return expected_income;
    }

    public void setExpected_income(float expected_income) {
        this.expected_income = expected_income;
    }

    public float getHigh_percent_max_income() {
        return high_percent_max_income;
    }

    public void setHigh_percent_max_income(float high_percent_max_income) {
        this.high_percent_max_income = high_percent_max_income;
    }

    public float getHigh_percent_min_income() {
        return high_percent_min_income;
    }

    public void setHigh_percent_min_income(float high_percent_min_income) {
        this.high_percent_min_income = high_percent_min_income;
    }

    public float getLow_percent_max_income() {
        return low_percent_max_income;
    }

    public void setLow_percent_max_income(float low_percent_max_income) {
        this.low_percent_max_income = low_percent_max_income;
    }

    public float getLow_percent_min_income() {
        return low_percent_min_income;
    }

    public void setLow_percent_min_income(float low_percent_min_income) {
        this.low_percent_min_income = low_percent_min_income;
    }

    public String getIncome_mounth_time() {
        return income_mounth_time;
    }

    public void setIncome_mounth_time(String income_mounth_time) {
        this.income_mounth_time = income_mounth_time;
    }
}
