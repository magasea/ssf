package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The persistent class for the ui_trd_log database table.
 *
 */
@Document(collection = "ui_trdlog")
public class MongoUiTrdLog implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Field( value = "amount")
  private BigDecimal amount;

  @Field( value = "created_by")
  private String createdBy;

  @Field( value = "created_date")
  private Long createdDate;

  @Field( value = "last_modified_by")
  private String lastModifiedBy;

  @Field( value = "last_modified_date")
  private Long lastModifiedDate;

  private int operations;

  @Field( value = "user_prod_id")
  private Long userProdId;


  @Field( value = "trade_date")
  private Long tradeDate;

  @Field( value = "trade_status")
  private int tradeStatus;

  @Field( value = "user_id")
  private Long userId;

  @Field( value = "fund_code")
  private String fundCode;

  @Field( value = "trade_confirm_share")
  private Long tradeConfirmShare;

  @Field( value = "trade_confirm_sum")
  private Long tradeConfirmSum;

  @Field( value = "trade_target_share")
  private Long tradeTargetShare;

  @Field( value = "trade_target_sum")
  private Long tradeTargetSum;

  public MongoUiTrdLog() {
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
}