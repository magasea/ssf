package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * The persistent class for the ui_trd_log database table.
 *
 */



public class TrdLog implements Serializable {
  private static final long serialVersionUID = 1L;

  
  
  
  private Long id;

  private BigDecimal amount;

  
  private String createdBy;

  
  private BigInteger createdDate;

  
  private String lastModifiedBy;

  
  private BigInteger lastModifiedDate;

  private int operations;

  
  private BigInteger prodId;


  
  private Long tradeDate;

  
  private int tradeStatus;

  
  private Long userId;

  public TrdLog() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public BigInteger getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(BigInteger createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public BigInteger getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(BigInteger lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public int getOperations() {
    return this.operations;
  }

  public void setOperations(int operations) {
    this.operations = operations;
  }

  public BigInteger getProdId() {
    return this.prodId;
  }

  public void setProdId(BigInteger prodId) {
    this.prodId = prodId;
  }

  public Long getTradeDate() {
    return this.tradeDate;
  }

  public void setTradeDate(Long tradeDate) {
    this.tradeDate = tradeDate;
  }

  public int getTradeStatus() {
    return this.tradeStatus;
  }

  public void setTradeStatus(int tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

}