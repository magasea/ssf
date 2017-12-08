package com.shellshellfish.aaas.assetallocation.neo.entity;

import java.util.Arrays;
import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/21
 * Desc: 用于计算基金数据 model
 */
public class CovarianceModel {

    private String codeA;//参与计算的基金A

    private String codeB;//参与计算的基金B

    private Double navadjA;//参与计算的基金A复权单位净值

    private Double navadjB;//参与计算的基金B复权单位净值

    private Date navDate;//净值日期

    private Double covariance;//协方差

    private Double[] yieldRatioArr;//收益率矩阵

    private Double[][] covarianceArr;//协方差矩阵

    private String status;//状态（succeed 成功 / failued 失败）

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public String getCodeB() {
        return codeB;
    }

    public void setCodeB(String codeB) {
        this.codeB = codeB;
    }

    public Date getNavDate() {
        return navDate;
    }

    public void setNavDate(Date navDate) {
        this.navDate = navDate;
    }

    public Double getNavadjA() {
        return navadjA;
    }

    public void setNavadjA(Double navadjA) {
        this.navadjA = navadjA;
    }

    public Double getNavadjB() {
        return navadjB;
    }

    public void setNavadjB(Double navadjB) {
        this.navadjB = navadjB;
    }

    public Double getCovariance() {
        return covariance;
    }

    public void setCovariance(Double covariance) {
        this.covariance = covariance;
    }

    public Double[] getYieldRatioArr() {
        return yieldRatioArr;
    }

    public void setYieldRatioArr(Double[] yieldRatioArr) {
        this.yieldRatioArr = yieldRatioArr;
    }

    public Double[][] getCovarianceArr() {
        return covarianceArr;
    }

    public void setCovarianceArr(Double[][] covarianceArr) {
        this.covarianceArr = covarianceArr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CovarianceModel{" +
                "codeA='" + codeA + '\'' +
                ", codeB='" + codeB + '\'' +
                ", navadjA=" + navadjA +
                ", navadjB=" + navadjB +
                ", navDate=" + navDate +
                ", covariance=" + covariance +
                ", yieldRatioArr=" + Arrays.toString(yieldRatioArr) +
                ", covarianceArr=" + Arrays.toString(covarianceArr) +
                ", status='" + status + '\'' +
                '}';
    }
}
