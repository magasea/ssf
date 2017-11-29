package com.shellshellfish.aaas.userinfo.model.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetDailyRept {

  public BigInteger getUserId() {
    return userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BigDecimal getDailyProfit() {
    return dailyProfit;
  }

  public void setDailyProfit(BigDecimal dailyProfit) {
    this.dailyProfit = dailyProfit;
  }

  public BigDecimal getAccumulateProfitRate() {
    return accumulateProfitRate;
  }

  public void setAccumulateProfitRate(BigDecimal accumulateProfitRate) {
    this.accumulateProfitRate = accumulateProfitRate;
  }

  public BigDecimal getAccumulateProfit() {
    return accumulateProfit;
  }

  public void setAccumulateProfit(BigDecimal accumulateProfit) {
    this.accumulateProfit = accumulateProfit;
  }

  public String getCurrentProducts() {
    return currentProducts;
  }

  public void setCurrentProducts(String currentProducts) {
    this.currentProducts = currentProducts;
  }
  @JsonIgnore
  private BigInteger userId;
  private Date date;
  private BigDecimal dailyProfit;
  private BigDecimal accumulateProfitRate;
  private BigDecimal accumulateProfit;
  private String currentProducts;
}
