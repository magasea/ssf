package com.shellshellfish.aaas.assetallocation.neo.entity;


import java.util.Date;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/12/22
 * Desc:接受 MVO 输出结果的 model
 */
public class FundCombination {
    private Integer groupId;//组合Id
    private String subGroupId;//子组合Id
    private Float subGroupRisk;//子组合风险
    private Float subGroupYield;//子组合收益
    private String code;//基金代码
    private double proportion;//权重
    private List<FundCombination>  subGroupDetails;//子组合具体数据

    private Float expectedAnnualizedReturn;//预期年化收益
    private Float simulateHistoricalYearPerformance;//模拟历史年化业绩

    private Float expectedMaxRetracement;//预期最大回撤
    private Float simulateHistoricalVolatility;//模拟历史年化波动率

    private Integer isValid; //数据是否有效
    private Date createDate;//数据创建时间
    private Date combinationDate;//组合成立日

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(String subGroupId) {
        this.subGroupId = subGroupId;
    }

    public Float getSubGroupRisk() {
        return subGroupRisk;
    }

    public void setSubGroupRisk(Float subGroupRisk) {
        this.subGroupRisk = subGroupRisk;
    }

    public Float getSubGroupYield() {
        return subGroupYield;
    }

    public void setSubGroupYield(Float subGroupYield) {
        this.subGroupYield = subGroupYield;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FundCombination> getSubGroupDetails() {
        return subGroupDetails;
    }

    public void setSubGroupDetails(List<FundCombination> subGroupDetails) {
        this.subGroupDetails = subGroupDetails;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public Float getExpectedAnnualizedReturn() {
        return expectedAnnualizedReturn;
    }

    public void setExpectedAnnualizedReturn(Float expectedAnnualizedReturn) {
        this.expectedAnnualizedReturn = expectedAnnualizedReturn;
    }

    public Float getSimulateHistoricalYearPerformance() {
        return simulateHistoricalYearPerformance;
    }

    public void setSimulateHistoricalYearPerformance(Float simulateHistoricalYearPerformance) {
        this.simulateHistoricalYearPerformance = simulateHistoricalYearPerformance;
    }

    public Float getExpectedMaxRetracement() {
        return expectedMaxRetracement;
    }

    public void setExpectedMaxRetracement(Float expectedMaxRetracement) {
        this.expectedMaxRetracement = expectedMaxRetracement;
    }

    public Float getSimulateHistoricalVolatility() {
        return simulateHistoricalVolatility;
    }

    public void setSimulateHistoricalVolatility(Float simulateHistoricalVolatility) {
        this.simulateHistoricalVolatility = simulateHistoricalVolatility;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCombinationDate() {
        return combinationDate;
    }

    public void setCombinationDate(Date combinationDate) {
        this.combinationDate = combinationDate;
    }

    @Override
    public String toString() {
        return "FundCombination{" +
                "groupId=" + groupId +
                ", subGroupId='" + subGroupId + '\'' +
                ", subGroupRisk=" + subGroupRisk +
                ", subGroupYield=" + subGroupYield +
                ", code='" + code + '\'' +
                ", proportion=" + proportion +
                ", subGroupDetails=" + subGroupDetails +
                ", expectedAnnualizedReturn=" + expectedAnnualizedReturn +
                ", simulateHistoricalYearPerformance=" + simulateHistoricalYearPerformance +
                ", expectedMaxRetracement=" + expectedMaxRetracement +
                ", simulateHistoricalVolatility=" + simulateHistoricalVolatility +
                ", isValid=" + isValid +
                ", createDate=" + createDate +
                ", combinationDate=" + combinationDate +
                '}';
    }
}
