package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DistributionResult {
    private BigDecimal poundage;
    private BigDecimal discountSaving;
    private List<FundAmount> fundAmountList;
    private Map<Object,Object> buyRateMap;
    public List<FundAmount> getFundAmountList() {
        return fundAmountList;
    }

    public void setFundAmountList(List<FundAmount> fundAmountList) {
        this.fundAmountList = fundAmountList;
    }

    public DistributionResult() {

    }

    public DistributionResult(BigDecimal poundage, BigDecimal discountSaving, List<FundAmount> fundAmountList,Map<Object,Object> buyRateMap) {
        this.poundage = poundage;
        this.discountSaving = discountSaving;
        this.fundAmountList = fundAmountList;
        this.buyRateMap=buyRateMap;
    }
    public DistributionResult(BigDecimal poundage, BigDecimal discountSaving, List<FundAmount> fundAmountList) {
        this.poundage = poundage;
        this.discountSaving = discountSaving;
        this.fundAmountList = fundAmountList;
    }
    public BigDecimal getPoundage() {
        return poundage;
    }

    public Map<Object, Object> getBuyRateMap() {
        return buyRateMap;
    }

    public void setBuyRateMap(Map<Object, Object> buyRateMap) {
        this.buyRateMap = buyRateMap;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public BigDecimal getDiscountSaving() {
        return discountSaving;
    }

    public void setDiscountSaving(BigDecimal discountSaving) {
        this.discountSaving = discountSaving;
    }
}
