package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;
import java.util.List;

public class DistributionResult {
    private BigDecimal poundage;
    private BigDecimal discountSaving;
    private List<FundAmount> fundAmountList;

    public List<FundAmount> getFundAmountList() {
        return fundAmountList;
    }

    public void setFundAmountList(List<FundAmount> fundAmountList) {
        this.fundAmountList = fundAmountList;
    }

    public DistributionResult() {

    }

    public DistributionResult(BigDecimal poundage, BigDecimal discountSaving, List<FundAmount> fundAmountList) {
        this.poundage = poundage;
        this.discountSaving = discountSaving;
        this.fundAmountList = fundAmountList;
    }

    public BigDecimal getPoundage() {
        return poundage;
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
