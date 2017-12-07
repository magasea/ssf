package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:fund_net_value 表对应实体类
 */
public class FundNetVal {
    private Float yieldOf7days;//7日年化收益率
    private Double unitNav;//单位净值
    private Float navUnitReturn;//单位净值增长率

    private Double navadj;//复权单位净值
    private Float navadjReturn;//复权单位净值增长率
    private Float adjustEdnav;//复权净值
    private  Double adjustedFactor;//复权因子

    private Double navAccum;//累计单位净值
    private Float navAccumReturn;//累计单位净值增长率
    private Double accumulatEdnav;//累计净值

    private Double millionRevenue;//万份基金单位收益
    private Date navLatestDate;//最新净值日期
    private String navCurrency;//单位净值币种

    private String code;//基金代码
    private Integer id;//实体标识
    private Date lastModTime;//最后一次修改时间



    public Float getYieldOf7days() {
        return yieldOf7days;
    }

    public void setYieldOf7days(Float yieldOf7days) {
        this.yieldOf7days = yieldOf7days;
    }

    public Double getUnitNav() {
        return unitNav;
    }

    public void setUnitNav(Double unitNav) {
        this.unitNav = unitNav;
    }

    public Float getNavUnitReturn() {
        return navUnitReturn;
    }

    public void setNavUnitReturn(Float navUnitReturn) {
        this.navUnitReturn = navUnitReturn;
    }

    public Double getNavadj() {
        return navadj;
    }

    public void setNavadj(Double navadj) {
        this.navadj = navadj;
    }

    public Float getNavadjReturn() {
        return navadjReturn;
    }

    public void setNavadjReturn(Float navadjReturn) {
        this.navadjReturn = navadjReturn;
    }

    public Float getAdjustEdnav() {
        return adjustEdnav;
    }

    public void setAdjustEdnav(Float adjustEdnav) {
        this.adjustEdnav = adjustEdnav;
    }

    public Double getAdjustedFactor() {
        return adjustedFactor;
    }

    public void setAdjustedFactor(Double adjustedFactor) {
        this.adjustedFactor = adjustedFactor;
    }

    public Double getNavAccum() {
        return navAccum;
    }

    public void setNavAccum(Double navAccum) {
        this.navAccum = navAccum;
    }

    public Float getNavAccumReturn() {
        return navAccumReturn;
    }

    public void setNavAccumReturn(Float navAccumReturn) {
        this.navAccumReturn = navAccumReturn;
    }

    public Double getAccumulatEdnav() {
        return accumulatEdnav;
    }

    public void setAccumulatEdnav(Double accumulatEdnav) {
        this.accumulatEdnav = accumulatEdnav;
    }

    public Double getMillionRevenue() {
        return millionRevenue;
    }

    public void setMillionRevenue(Double millionRevenue) {
        this.millionRevenue = millionRevenue;
    }

    public Date getNavLatestDate() {
        return navLatestDate;
    }

    public void setNavLatestDate(Date navLatestDate) {
        this.navLatestDate = navLatestDate;
    }

    public String getNavCurrency() {
        return navCurrency;
    }

    public void setNavCurrency(String navCurrency) {
        this.navCurrency = navCurrency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(Date lastModTime) {
        this.lastModTime = lastModTime;
    }

    @Override
    public String toString() {
        return "FundNetVal{" +
                "yieldOf7days=" + yieldOf7days +
                ", unitNav=" + unitNav +
                ", navUnitReturn=" + navUnitReturn +
                ", navadj=" + navadj +
                ", navadjReturn=" + navadjReturn +
                ", adjustEdnav=" + adjustEdnav +
                ", adjustedFactor=" + adjustedFactor +
                ", navAccum=" + navAccum +
                ", navAccumReturn=" + navAccumReturn +
                ", accumulatEdnav=" + accumulatEdnav +
                ", millionRevenue=" + millionRevenue +
                ", navLatestDate=" + navLatestDate +
                ", navCurrency='" + navCurrency + '\'' +
                ", code='" + code + '\'' +
                ", id=" + id +
                ", lastModTime=" + lastModTime +
                '}';
    }
}
