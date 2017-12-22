package com.shellshellfish.aaas.common.grpc.finance.product;

public class ProductMakeUpInfo {


  Long prodId;
  Long groupId;
  String prodName;
  String fundCode;

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  String fundName;
  Integer fundShare;

  public ProductMakeUpInfo() {

  }

  public ProductMakeUpInfo(Long prodId, Long groupId, String prodName, String fundCode, Integer fundShare) {
    this.prodId = prodId;
    this.groupId = groupId;
    this.prodName = prodName;
    this.fundCode = fundCode;
    this.fundShare = fundShare;
  }

  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getProdName() {
    return prodName;
  }

  public void setProdName(String prodName) {
    this.prodName = prodName;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public Integer getFundShare() {
    return fundShare;
  }

  public void setFundShare(Integer fundShare) {
    this.fundShare = fundShare;
  }
}
