package com.shellshellfish.aaas.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trd_order", schema = "ssftrdorder", catalog = "")
public class TrdOrderEntity {

  private long id;
  private String orderId;
  private String bankCardNum;
  private String prodCode;
  private int orderStatus;
  private long orderDate;
  private int orderType;
  private long payAmount;
  private long payFee;
  private long userId;
  private long prodId;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "order_id")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
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
  public long getPayFee() {
    return payFee;
  }

  public void setPayFee(long payFee) {
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

    TrdOrderEntity that = (TrdOrderEntity) o;

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
    if (payFee != that.payFee) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (prodId != that.prodId) {
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
    if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) {
      return false;
    }
    if (bankCardNum != null ? !bankCardNum.equals(that.bankCardNum) : that.bankCardNum != null) {
      return false;
    }
    if (prodCode != null ? !prodCode.equals(that.prodCode) : that.prodCode != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (prodCode != null ? prodCode.hashCode() : 0);
    result = 31 * result + orderStatus;
    result = 31 * result + (int) (orderDate ^ (orderDate >>> 32));
    result = 31 * result + orderType;
    result = 31 * result + (int) (payAmount ^ (payAmount >>> 32));
    result = 31 * result + (int) (payFee ^ (payFee >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (prodId ^ (prodId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    return result;
  }
}
