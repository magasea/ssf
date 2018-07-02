package com.shellshellfish.aaas.finance.trade.order.model.dao;

import java.math.BigDecimal;

public class TrdLog {
  private String id;


  private BigDecimal amount;


  private String createdBy;


  private Long createdDate;

  public TrdLog() {
  }

  private String lastModifiedBy;


  private Long lastModifiedDate;

  private int operations;


  private Long userProdId;



  private Long tradeDate;


  private int tradeStatus;


  private Long userId;


  private String fundCode;


  private Long tradeConfirmShare;


  private Long tradeConfirmSum;


  private Long tradeTargetShare;


  private Long tradeTargetSum;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public int getOperations() {
    return operations;
  }

  public void setOperations(int operations) {
    this.operations = operations;
  }

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  public Long getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Long tradeDate) {
    this.tradeDate = tradeDate;
  }

  public int getTradeStatus() {
    return tradeStatus;
  }

  public void setTradeStatus(int tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public Long getTradeConfirmShare() {
    return tradeConfirmShare;
  }

  public void setTradeConfirmShare(Long tradeConfirmShare) {
    this.tradeConfirmShare = tradeConfirmShare;
  }

  public Long getTradeConfirmSum() {
    return tradeConfirmSum;
  }

  public void setTradeConfirmSum(Long tradeConfirmSum) {
    this.tradeConfirmSum = tradeConfirmSum;
  }

  public Long getTradeTargetShare() {
    return tradeTargetShare;
  }

  public void setTradeTargetShare(Long tradeTargetShare) {
    this.tradeTargetShare = tradeTargetShare;
  }

  public Long getTradeTargetSum() {
    return tradeTargetSum;
  }

  public void setTradeTargetSum(Long tradeTargetSum) {
    this.tradeTargetSum = tradeTargetSum;
  }

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public String getOrderId() {
    return OrderId;
  }

  public void setOrderId(String orderId) {
    OrderId = orderId;
  }

  public String getConfirmDateExp() {
    return confirmDateExp;
  }

  public void setConfirmDateExp(String confirmDateExp) {
    this.confirmDateExp = confirmDateExp;
  }

  private String applySerial;

  private String OrderId;

  private String confirmDateExp;
}
