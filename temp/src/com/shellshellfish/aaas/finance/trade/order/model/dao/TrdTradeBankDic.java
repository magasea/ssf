package com.shellshellfish.aaas.finance.trade.order.model.dao;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by developer4 on 2018- 五月 - 28
 */

@Entity
@Table(name = "trd_trade_bank_dic", schema = "ssftrdorder", catalog = "")
public class TrdTradeBankDic {

  private long id;
  private String bankName;
  private String bankCode;
  private long traderBrokerId;
  private String bankShortName;
  private String capitalModel;
  private String moneyLimitOne;
  private String moneyLimitDay;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private Integer bankId;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "bank_name")
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  @Basic
  @Column(name = "bank_code")
  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  @Basic
  @Column(name = "trader_broker_id")
  public long getTraderBrokerId() {
    return traderBrokerId;
  }

  public void setTraderBrokerId(long traderBrokerId) {
    this.traderBrokerId = traderBrokerId;
  }

  @Basic
  @Column(name = "bank_short_name")
  public String getBankShortName() {
    return bankShortName;
  }

  public void setBankShortName(String bankShortName) {
    this.bankShortName = bankShortName;
  }

  @Basic
  @Column(name = "capital_model")
  public String getCapitalModel() {
    return capitalModel;
  }

  public void setCapitalModel(String capitalModel) {
    this.capitalModel = capitalModel;
  }

  @Basic
  @Column(name = "money_limit_one")
  public String getMoneyLimitOne() {
    return moneyLimitOne;
  }

  public void setMoneyLimitOne(String moneyLimitOne) {
    this.moneyLimitOne = moneyLimitOne;
  }

  @Basic
  @Column(name = "money_limit_day")
  public String getMoneyLimitDay() {
    return moneyLimitDay;
  }

  public void setMoneyLimitDay(String moneyLimitDay) {
    this.moneyLimitDay = moneyLimitDay;
  }

  @Basic
  @Column(name = "create_by")
  public long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  @Basic
  @Column(name = "create_date")
  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "update_by")
  public long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  @Basic
  @Column(name = "update_date")
  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  @Basic
  @Column(name = "bank_id")
  public Integer getBankId() {
    return bankId;
  }

  public void setBankId(Integer bankId) {
    this.bankId = bankId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrdTradeBankDic that = (TrdTradeBankDic) o;
    return id == that.id &&
        traderBrokerId == that.traderBrokerId &&
        createBy == that.createBy &&
        createDate == that.createDate &&
        updateBy == that.updateBy &&
        updateDate == that.updateDate &&
        Objects.equals(bankName, that.bankName) &&
        Objects.equals(bankCode, that.bankCode) &&
        Objects.equals(bankShortName, that.bankShortName) &&
        Objects.equals(capitalModel, that.capitalModel) &&
        Objects.equals(moneyLimitOne, that.moneyLimitOne) &&
        Objects.equals(moneyLimitDay, that.moneyLimitDay) &&
        Objects.equals(bankId, that.bankId);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(id, bankName, bankCode, traderBrokerId, bankShortName, capitalModel, moneyLimitOne,
            moneyLimitDay, createBy, createDate, updateBy, updateDate, bankId);
  }
}
