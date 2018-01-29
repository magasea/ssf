package com.shellshellfish.aaas.userinfo.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the ui_trd_log database table.
 *
 */
public class MongoUiTrdLogDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;

  private BigDecimal amount;

  private String createdBy;

  private Long createdDate;

  private String lastModifiedBy;

  private Long lastModifiedDate;

  private int operations;

  private Long userProdId;


  private Long tradeDate;

  private int tradeStatus;

  private Long userId;

  public MongoUiTrdLogDTO() {
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
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

  public Long getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Long getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public int getOperations() {
    return this.operations;
  }

  public void setOperations(int operations) {
    this.operations = operations;
  }

  public Long getUserProdId() {
    return this.userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
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