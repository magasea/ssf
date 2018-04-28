package com.shellshellfish.aaas.assetallocation.entity;

public class FundGroupIndex {

    private Long id;
    private int oemId;
    private String fundGroupId;
    private String fundGroupSubId;

    //历史年化收益
    private Double historicalAnnualYield;
    //历史年化波动率
    private Double historicalAnnualVolatility;

    public FundGroupIndex() {
    }

    public FundGroupIndex(String fundGroupId, String fundGroupSubId, Double historicalAnnualYield, Double historicalAnnualVolatility) {
        this.fundGroupId = fundGroupId;
        this.fundGroupSubId = fundGroupSubId;
        this.historicalAnnualYield = historicalAnnualYield;
        this.historicalAnnualVolatility = historicalAnnualVolatility;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOemId() {
        return oemId;
    }

    public void setOemId(int oemId) {
        this.oemId = oemId;
    }

    public String getFundGroupId() {
        return fundGroupId;
    }

    public void setFundGroupId(String fundGroupId) {
        this.fundGroupId = fundGroupId;
    }

    public String getFundGroupSubId() {
        return fundGroupSubId;
    }

    public void setFundGroupSubId(String fundGroupSubId) {
        this.fundGroupSubId = fundGroupSubId;
    }

    public Double getHistoricalAnnualYield() {
        return historicalAnnualYield;
    }

    public void setHistoricalAnnualYield(Double historicalAnnualYield) {
        this.historicalAnnualYield = historicalAnnualYield;
    }

    public Double getHistoricalAnnualVolatility() {
        return historicalAnnualVolatility;
    }

    public void setHistoricalAnnualVolatility(Double historicalAnnualVolatility) {
        this.historicalAnnualVolatility = historicalAnnualVolatility;
    }

    @Override
    public String toString() {
        return "FundGroupIndex{" +
                "id=" + id +
                ", fundGroupId='" + fundGroupId + '\'' +
                ", fundGroupSubId='" + fundGroupSubId + '\'' +
                ", historicalAnnualYeild=" + historicalAnnualYield +
                ", historicalAnnualVolatility=" + historicalAnnualVolatility +
                '}';
    }
}
