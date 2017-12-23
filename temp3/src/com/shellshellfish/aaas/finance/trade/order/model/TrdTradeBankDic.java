package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 23
 */

@Entity
@Table(name = "trd_trade_bank_dic", schema = "ssftrdorder", catalog = "")
public class TrdTradeBankDic {

  private long id;
  private String bankName;
  private String bankCode;
  private long traderBrokerId;
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

    if (id != that.id) {
      return false;
    }
    if (traderBrokerId != that.traderBrokerId) {
      return false;
    }
    if (createBy != that.createBy) {
      return false;
    }
    if (createDate != that.createDate) {
      return false;
    }
    if (updateBy != that.updateBy) {
      return false;
    }
    if (updateDate != that.updateDate) {
      return false;
    }
    if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) {
      return false;
    }
    if (bankCode != null ? !bankCode.equals(that.bankCode) : that.bankCode != null) {
      return false;
    }
    if (bankId != null ? !bankId.equals(that.bankId) : that.bankId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
    result = 31 * result + (bankCode != null ? bankCode.hashCode() : 0);
    result = 31 * result + (int) (traderBrokerId ^ (traderBrokerId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (bankId != null ? bankId.hashCode() : 0);
    return result;
  }
}
