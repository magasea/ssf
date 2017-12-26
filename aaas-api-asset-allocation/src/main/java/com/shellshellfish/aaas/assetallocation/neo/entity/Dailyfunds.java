package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2017/12/18
 * Desc: 每日接口返回数据model
 */
public class Dailyfunds {


    private Date navLatestDate;//最新净值日期
    private Float fundScale;//基金规模
    private Double navUnit;//单位净值
    private Double navAccum;//累计单位净值
    private Double navAdj;//复权单位净值
    private Float  yieldOf7Days;//7日年化收益率
    private Double millionRevenue;//万份基金单位收益
    private Float bmIndexChgPct;//标的指数涨跌幅
    private String code;//基金代码
    private String fname;//基金简称
    private String fundTypeOne;//一级分类
    private String fundTypeTwo;//二级分类
    private Double close;//日行情收盘价
    private String id;//实体Id


    public Dailyfunds() {
    }

    public Dailyfunds(Date navLatestDate, Float fundScale, Double navUnit,
                      Double navAccum, Double navAdj, Float yieldOf7Days,
                      Double millionRevenue, Float bmIndexChgPct, String code,
                      String fname, String fundTypeOne, String fundTypeTwo, String id) {
        this.navLatestDate = navLatestDate;
        this.fundScale = fundScale;
        this.navUnit = navUnit;
        this.navAccum = navAccum;
        this.navAdj = navAdj;
        this.yieldOf7Days = yieldOf7Days;
        this.millionRevenue = millionRevenue;
        this.bmIndexChgPct = bmIndexChgPct;
        this.code = code;
        this.fname = fname;
        this.fundTypeOne = fundTypeOne;
        this.fundTypeTwo = fundTypeTwo;
        this.id = id;
    }

    public Date getNavLatestDate() {
        return navLatestDate;
    }

    public void setNavLatestDate(Date navLatestDate) {
        this.navLatestDate = navLatestDate;
    }

    public Float getFundScale() {
        return fundScale;
    }

    public void setFundScale(Float fundScale) {
        this.fundScale = fundScale;
    }

    public Double getNavUnit() {
        return navUnit;
    }

    public void setNavUnit(Double navUnit) {
        this.navUnit = navUnit;
    }

    public Double getNavAccum() {
        return navAccum;
    }

    public void setNavAccum(Double navAccum) {
        this.navAccum = navAccum;
    }

    public Double getNavAdj() {
        return navAdj;
    }

    public void setNavAdj(Double navAdj) {
        this.navAdj = navAdj;
    }

    public Float getYieldOf7Days() {
        return yieldOf7Days;
    }

    public void setYieldOf7Days(Float yieldOf7Days) {
        this.yieldOf7Days = yieldOf7Days;
    }

    public Double getMillionRevenue() {
        return millionRevenue;
    }

    public void setMillionRevenue(Double millionRevenue) {
        this.millionRevenue = millionRevenue;
    }

    public Float getBmIndexChgPct() {
        return bmIndexChgPct;
    }

    public void setBmIndexChgPct(Float bmIndexChgPct) {
        this.bmIndexChgPct = bmIndexChgPct;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFundTypeOne() {
        return fundTypeOne;
    }

    public void setFundTypeOne(String fundTypeOne) {
        this.fundTypeOne = fundTypeOne;
    }

    public String getFundTypeTwo() {
        return fundTypeTwo;
    }

    public void setFundTypeTwo(String fundTypeTwo) {
        this.fundTypeTwo = fundTypeTwo;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dailyfunds{" +
                "navLatestDate=" + navLatestDate +
                ", fundScale=" + fundScale +
                ", navUnit=" + navUnit +
                ", navAccum=" + navAccum +
                ", navAdj=" + navAdj +
                ", yieldOf7Days=" + yieldOf7Days +
                ", millionRevenue=" + millionRevenue +
                ", bmIndexChgPct=" + bmIndexChgPct +
                ", code='" + code + '\'' +
                ", fname='" + fname + '\'' +
                ", fundTypeOne='" + fundTypeOne + '\'' +
                ", fundTypeTwo='" + fundTypeTwo + '\'' +
                ", close=" + close +
                ", id='" + id + '\'' +
                '}';
    }


}
