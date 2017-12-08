package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the ui_trade_account database table.
 *
 */
@Entity
@Table(name="ui_trade_account")
@NamedQuery(name="UiTradeAccount.findAll", query="SELECT u FROM UiTradeAccount u")
public class UiTradeAccount implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name="UI_TRADE_ACCOUNT_ID_GENERATOR" )
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_TRADE_ACCOUNT_ID_GENERATOR")
  private String id;

  @Column(name="acccount_num")
  private String acccountNum;

  @Column(name="account_type")
  private int accountType;

  @Column(name="create_by")
  private BigInteger createBy;

  @Column(name="create_date")
  private BigInteger createDate;

  @Column(name="trade_broker_id")
  private int tradeBrokerId;

  @Column(name="trade_status")
  private int tradeStatus;

  @Column(name="update_by")
  private BigInteger updateBy;

  @Column(name="update_date")
  private BigInteger updateDate;

  @Column(name="user_id")
  private BigInteger userId;

  public UiTradeAccount() {
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAcccountNum() {
    return this.acccountNum;
  }

  public void setAcccountNum(String acccountNum) {
    this.acccountNum = acccountNum;
  }

  public int getAccountType() {
    return this.accountType;
  }

  public void setAccountType(int accountType) {
    this.accountType = accountType;
  }

  public BigInteger getCreateBy() {
    return this.createBy;
  }

  public void setCreateBy(BigInteger createBy) {
    this.createBy = createBy;
  }

  public BigInteger getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(BigInteger createDate) {
    this.createDate = createDate;
  }

  public int getTradeBrokerId() {
    return this.tradeBrokerId;
  }

  public void setTradeBrokerId(int tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
  }

  public int getTradeStatus() {
    return this.tradeStatus;
  }

  public void setTradeStatus(int tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public BigInteger getUpdateBy() {
    return this.updateBy;
  }

  public void setUpdateBy(BigInteger updateBy) {
    this.updateBy = updateBy;
  }

  public BigInteger getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(BigInteger updateDate) {
    this.updateDate = updateDate;
  }

  public BigInteger getUserId() {
    return this.userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }

}
