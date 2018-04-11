package com.shellshellfish.aaas.assetallocation.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class Strategy extends FundBasic{
    private String id;
    private String cust_risk;//客户风险值
    private String investment_horizon;//投资年限
    private String fund_group_id;//基金组合ID

    public String getInvestment_horizon() {
        return investment_horizon;
    }

    public void setInvestment_horizon(String investment_horizon) {
        this.investment_horizon = investment_horizon;
    }

    public Date getStrategy_last_mod_time() {
        return strategy_last_mod_time;
    }

    public void setStrategy_last_mod_time(Date strategy_last_mod_time) {
        this.strategy_last_mod_time = strategy_last_mod_time;
    }

    private Date strategy_last_mod_time;//最后一次修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCust_risk() {
        return cust_risk;
    }

    public void setCust_risk(String cust_risk) {
        this.cust_risk = cust_risk;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

}
