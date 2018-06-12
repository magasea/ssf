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
@Table(name = "trd_order", schema = "ssftrdorder", catalog = "")
public class TrdOrder {

  private long id;
  private String orderId;
  private long preOrderId;
  private String bankCardNum;

  private int orderStatus;
  private long orderDate;
  private int orderType;
  private long sellPercent;
  private long payAmount;
  private Long payFee;
  private long userId;
  private long prodId;
  private long groupId;
  private long userProdId;
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
  @Column(name = "order_id")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Basic
  @Column(name = "pre_order_id")
  public long getPreOrderId() {
    return preOrderId;
  }

  public void setPreOrderId(long preOrderId) {
    this.preOrderId = preOrderId;
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
  @Column(name = "sell_percent")
  public long getSellPercent() {
    return sellPercent;
  }

  public void setSellPercent(long sellPercent) {
    this.sellPercent = sellPercent;
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
  @Column(name = "group_id")
  public long getGroupId() {
    return groupId;
  }

  public void setGroupId(long groupId) {
    this.groupId = groupId;
  }

  @Basic
  @Column(name = "user_prod_id")
  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
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

    TrdOrder trdOrder = (TrdOrder) o;

    if (id != trdOrder.id) {
      return false;
    }
    if (preOrderId != trdOrder.preOrderId) {
      return false;
    }
    if (orderStatus != trdOrder.orderStatus) {
      return false;
    }
    if (orderDate != trdOrder.orderDate) {
      return false;
    }
    if (orderType != trdOrder.orderType) {
      return false;
    }
    if (payAmount != trdOrder.payAmount) {
      return false;
    }
    if (userId != trdOrder.userId) {
      return false;
    }
    if (prodId != trdOrder.prodId) {
      return false;
    }
    if (groupId != trdOrder.groupId) {
      return false;
    }
    if (userProdId != trdOrder.userProdId) {
      return false;
    }
    if (createBy != trdOrder.createBy) {
      return false;
    }
    if (createDate != trdOrder.createDate) {
      return false;
    }
    if (updateBy != trdOrder.updateBy) {
      return false;
    }
    if (updateDate != trdOrder.updateDate) {
      return false;
    }
    if (orderId != null ? !orderId.equals(trdOrder.orderId) : trdOrder.orderId != null) {
      return false;
    }
    if (bankCardNum != null ? !bankCardNum.equals(trdOrder.bankCardNum)
        : trdOrder.bankCardNum != null) {
      return false;
    }

    if (payFee != null ? !payFee.equals(trdOrder.payFee) : trdOrder.payFee != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
    result = 31 * result + (int) (preOrderId ^ (preOrderId >>> 32));
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);

    result = 31 * result + orderStatus;
    result = 31 * result + (int) (orderDate ^ (orderDate >>> 32));
    result = 31 * result + orderType;
    result = 31 * result + (int) (payAmount ^ (payAmount >>> 32));
    result = 31 * result + (payFee != null ? payFee.hashCode() : 0);
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (prodId ^ (prodId >>> 32));
    result = 31 * result + (int) (groupId ^ (groupId >>> 32));
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    return result;
  }
}
