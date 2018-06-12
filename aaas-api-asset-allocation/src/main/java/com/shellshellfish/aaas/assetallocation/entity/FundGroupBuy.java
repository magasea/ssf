package com.shellshellfish.aaas.assetallocation.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/12/1.
 */
public class FundGroupBuy {
    private String fund_id;//
    private double proportion;//
    private double buy_num;//
    private Date buy_time;//

    public Date getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(Date buy_time) {
        this.buy_time = buy_time;
    }

    public String getFund_id() {
        return fund_id;
    }

    public void setFund_id(String fund_id) {
        this.fund_id = fund_id;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public double getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(double buy_num) {
        this.buy_num = buy_num;
    }
}
