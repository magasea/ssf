package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;

public class FundAmount {
    private String fundCode;
    private String fundName;
    private BigDecimal grossAmount;
    private String fundShare;

    public FundAmount(String fundCode, String fundName, BigDecimal grossAmount) {
        this.fundCode = fundCode;
        this.fundName = fundName;
        this.grossAmount = grossAmount;
        this.fundShare = "";
    }
    
    public FundAmount(String fundCode, String fundName, BigDecimal grossAmount, String fundShare) {
      this.fundCode = fundCode;
      this.fundName = fundName;
      this.grossAmount = grossAmount;
      this.fundShare = fundShare;
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

    public String getFundShare() {
      return fundShare;
    }

    public void setFundShare(String fundShare) {
      this.fundShare = fundShare;
    }
    
}
