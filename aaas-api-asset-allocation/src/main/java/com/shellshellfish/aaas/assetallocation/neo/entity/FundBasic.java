package com.shellshellfish.aaas.assetallocation.neo.entity;

/**
 * Created by wangyinuo on 2017/11/17.
 */
public class FundBasic {

    private String name;//基金简称
    private String fname;//基金全称
    private String fund_type_one;//
    private String fund_type_two;//

    public String getFund_type_one() {
        return fund_type_one;
    }

    public void setFund_type_one(String fund_type_one) {
        this.fund_type_one = fund_type_one;
    }

    public String getFund_type_two() {
        return fund_type_two;
    }

    public void setFund_type_two(String fund_type_two) {
        this.fund_type_two = fund_type_two;
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
