package com.shellshellfish.aaas.assetallocation.neo.entity;

/**
 * Created by wangyinuo on 2017/11/22.
 */
public class Interval extends FundGroupDetails {
    private String total;
    private String income_risk_type;//判断类别
    private double income_risk_num;//具体值
    private double expected_annualized_return;//预期年化收益
    private double expected_max_retracement;//预期最大回撤

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIncome_risk_type() {
        return income_risk_type;
    }

    public void setIncome_risk_type(String income_risk_type) {
        this.income_risk_type = income_risk_type;
    }

    public double getIncome_risk_num() {
        return income_risk_num;
    }

    public void setIncome_risk_num(double income_risk_num) {
        this.income_risk_num = income_risk_num;
    }

    public double getExpected_annualized_return() {
        return expected_annualized_return;
    }

    public void setExpected_annualized_return(double expected_annualized_return) {
        this.expected_annualized_return = expected_annualized_return;
    }

    public double getExpected_max_retracement() {
        return expected_max_retracement;
    }

    public void setExpected_max_retracement(double expected_max_retracement) {
        this.expected_max_retracement = expected_max_retracement;
    }
}
