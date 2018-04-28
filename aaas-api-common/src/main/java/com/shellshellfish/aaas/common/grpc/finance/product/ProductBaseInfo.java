package com.shellshellfish.aaas.common.grpc.finance.product;



public class ProductBaseInfo {

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

  public Integer getOemId() {
    return oemId;
  }

  public void setOemId(Integer oemId) {
    this.oemId = oemId;
  }

  Long prodId;
  Long groupId;
  Integer oemId;

}
