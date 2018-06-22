package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;

public class FundDetailResult {
  private String fundCode;
  private String fundName;
  private String grossAmount;
  private String fundShare;

  public FundDetailResult() {
  }

  public FundDetailResult(String fundCode, String fundName, String grossAmount,
      String fundShare) {
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

  public String getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(String grossAmount) {
    this.grossAmount = grossAmount;
  }

  public String getFundShare() {
    return fundShare;
  }

  public void setFundShare(String fundShare) {
    this.fundShare = fundShare;
  }
}
