package com.shellshellfish.aaas.finance.trade.order.model.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 12
 */

@Entity
@Table(name = "trd_pre_order", schema = "ssftrdorder", catalog = "")
public class TrdPreOrder {

  private long id;
  private String bankCardNum;
  private String prodCode;
  private int orderStatus;
  private long orderDate;
  private int orderType;
  private long payAmount;
  private Long payFee;
  private long userId;
  private long prodId;
  private String fundCode;
  private long fundShareConfirmed;
  private String errMsg;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;



  @Id
  @Column(name = "id")
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "bank_card_num")
  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
  }

  @Basic
  @Column(name = "prod_code")
  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  @Basic
  @Column(name = "order_status")
  public int getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
  }

  @Basic
  @Column(name = "order_date")
  public long getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(long orderDate) {
    this.orderDate = orderDate;
  }

  @Basic
  @Column(name = "order_type")
  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  @Basic
  @Column(name = "pay_amount")
  public long getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(long payAmount) {
    this.payAmount = payAmount;
  }

  @Basic
  @Column(name = "pay_fee")
  public Long getPayFee() {
    return payFee;
  }

  public void setPayFee(Long payFee) {
    this.payFee = payFee;
  }

  @Basic
  @Column(name = "user_id")
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "prod_id")
  public long getProdId() {
    return prodId;
  }

  public void setProdId(long prodId) {
    this.prodId = prodId;
  }

  @Basic
  @Column(name = "fund_code")
  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  @Basic
  @Column(name = "fund_share_confirmed")
  public long getFundShareConfirmed() {
    return fundShareConfirmed;
  }

  public void setFundShareConfirmed(long fundShareConfirmed) {
    this.fundShareConfirmed = fundShareConfirmed;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrdPreOrder that = (TrdPreOrder) o;

    if (id != that.id) {
      return false;
    }
    if (orderStatus != that.orderStatus) {
      return false;
    }
    if (orderDate != that.orderDate) {
      return false;
    }
    if (orderType != that.orderType) {
      return false;
    }
    if (payAmount != that.payAmount) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (prodId != that.prodId) {
      return false;
    }
    if (fundShareConfirmed != that.fundShareConfirmed) {
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
    if (bankCardNum != null ? !bankCardNum.equals(that.bankCardNum) : that.bankCardNum != null) {
      return false;
    }
    if (prodCode != null ? !prodCode.equals(that.prodCode) : that.prodCode != null) {
      return false;

    }
    if (payFee != null ? !payFee.equals(that.payFee) : that.payFee != null) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(that.fundCode) : that.fundCode != null) {
      return false;
    }

    return true;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (prodCode != null ? prodCode.hashCode() : 0);
    result = 31 * result + orderStatus;
    result = 31 * result + (int) (orderDate ^ (orderDate >>> 32));
    result = 31 * result + orderType;
    result = 31 * result + (int) (payAmount ^ (payAmount >>> 32));
    result = 31 * result + (payFee != null ? payFee.hashCode() : 0);
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (prodId ^ (prodId >>> 32));
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (int) (fundShareConfirmed ^ (fundShareConfirmed >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    return result;
  }
}
