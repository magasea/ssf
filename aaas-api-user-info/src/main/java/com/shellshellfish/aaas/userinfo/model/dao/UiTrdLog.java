package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the ui_trd_log database table.
 *
 */
@Entity
@Table(name="ui_trd_log")
public class UiTrdLog implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  private BigDecimal amount;

  @Column(name="created_by")
  private String createdBy;

  @Column(name="created_date")
  private Long createdDate;

  @Column(name="last_modified_by")
  private String lastModifiedBy;

  @Column(name="last_modified_date")
  private Long lastModifiedDate;

  private int operations;

  @Column(name="user_prod_id")
  private Long userProdId;

  @Column(name="prod_id")
  private Long prodId;
  
  @Column(name="trade_date")
  private Long tradeDate;

  @Column(name="trade_status")
  private int tradeStatus;

  @Column(name="user_id")
  private Long userId;



  public UiTrdLog() {
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

  public Long getProdId() {
	return prodId;
  }

  public void setProdId(Long prodId) {
	this.prodId = prodId;
  }
  
}