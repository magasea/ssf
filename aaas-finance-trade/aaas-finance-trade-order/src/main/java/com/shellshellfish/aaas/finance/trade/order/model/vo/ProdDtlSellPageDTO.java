package com.shellshellfish.aaas.userinfo.model.dto;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public class ProdDtlSellPageDTO {

  private long id;
  private long userProdId;
  private String fundCode;
  private String fundName;
  private int fundShare;
  private int fundQuantity;
  private BigDecimal targetSellAmount;


  public BigDecimal getTargetSellAmount() {
    return targetSellAmount;
  }

  public void setTargetSellAmount(BigDecimal targetSellAmount) {
    this.targetSellAmount = targetSellAmount;
  }



  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
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
  public int getFundShare() {
    return fundShare;
  }

  public void setFundShare(int fundShare) {
    this.fundShare = fundShare;
  }
  public int getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(int fundQuantity) {
    this.fundQuantity = fundQuantity;
  }




}
