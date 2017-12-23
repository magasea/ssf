package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;

public class FundAmount {
    private String fundCode;
    private String fundName;
    private BigDecimal grossAmount;

    public FundAmount(String fundCode, String fundName, BigDecimal grossAmount) {
        this.fundCode = fundCode;
        this.fundName = fundName;
        this.grossAmount = grossAmount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }
}
