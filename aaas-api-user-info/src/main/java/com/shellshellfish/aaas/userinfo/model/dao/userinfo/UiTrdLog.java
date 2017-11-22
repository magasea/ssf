package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;


/**
 * The persistent class for the ui_trd_log database table.
 *
 */
@Entity
@Table(name="ui_trd_log")
@NamedQuery(name="UiTrdLog.findAll", query="SELECT u FROM UiTrdLog u")
public class UiTrdLog implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name="UI_TRD_LOG_ID_GENERATOR" )
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_TRD_LOG_ID_GENERATOR")
  private Long id;

  private BigDecimal amount;

  @Column(name="created_by")
  private String createdBy;

  @Column(name="created_date")
  private BigInteger createdDate;

  @Column(name="last_modified_by")
  private String lastModifiedBy;

  @Column(name="last_modified_date")
  private BigInteger lastModifiedDate;

  private int operations;

  @Column(name="prod_id")
  private BigInteger prodId;


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