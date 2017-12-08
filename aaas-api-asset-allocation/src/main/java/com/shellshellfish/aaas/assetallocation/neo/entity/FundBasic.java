package com.shellshellfish.aaas.assetallocation.neo.entity;

/**
 * Created by wangyinuo on 2017/11/17.
 */
public class FundBasic {

    private String name;//基金简称
    private String fname;//基金全称
    private String fund_income_type;//基金投资类型

    public String getFund_income_type() {
        return fund_income_type;
    }

    public void setFund_income_type(String fund_income_type) {
        this.fund_income_type = fund_income_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

}
