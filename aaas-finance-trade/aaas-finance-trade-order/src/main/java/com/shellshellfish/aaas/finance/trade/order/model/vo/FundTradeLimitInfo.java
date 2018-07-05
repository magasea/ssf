package com.shellshellfish.aaas.finance.trade.order.model.vo;

public class FundTradeLimitInfo {
  String  fundcode;
  String minshare;
  String  minLimitValue;

  public FundTradeLimitInfo() {
  }

  public FundTradeLimitInfo(String fundcode, String minshare, String minLimitValue) {
    this.fundcode = fundcode;
    this.minshare = minshare;
    this.minLimitValue = minLimitValue;
  }

  public String getFundcode() {
    return fundcode;
  }

  public void setFundcode(String fundcode) {
    this.fundcode = fundcode;
  }

  public String getMinshare() {
    return minshare;
  }

  public void setMinshare(String minshare) {
    this.minshare = minshare;
  }

  public String getMinLimitValue() {
    return minLimitValue;
  }

  public void setMinLimitValue(String minLimitValue) {
    this.minLimitValue = minLimitValue;
  }
}
