package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:基金计算数据存放表（日周月季年）映射实体
 */
public class FundCalculateData {
    private Integer id;//实体Id
    private String code;//基金代码
    private Double yieldRatio;//收益率
    private Double riskRatio;//风险率
    private Date navDate;//净值日期
    private Double semiVariance;//半方差

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getYieldRatio() {
        return yieldRatio;
    }

    public void setYieldRatio(Double yieldRatio) {
        this.yieldRatio = yieldRatio;
    }

    public Double getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(Double riskRatio) {
        this.riskRatio = riskRatio;
    }

    public Date getNavDate() {
        return navDate;
    }

    public void setNavDate(Date navDate) {
        this.navDate = navDate;
    }

    public Double getSemiVariance() {
        return semiVariance;
    }

    public void setSemiVariance(Double semiVariance) {
        this.semiVariance = semiVariance;
    }

    @Override
    public String toString() {
        return "FundCalculateData{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", yieldRatio=" + yieldRatio +
                ", riskRatio=" + riskRatio +
                ", navDate=" + navDate +
                ", semiVariance=" + semiVariance +
                '}';
    }
}
