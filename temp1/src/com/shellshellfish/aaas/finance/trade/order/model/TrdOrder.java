package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 22
 */

@Entity
@Table(name = "trd_order", schema = "ssftrdorder", catalog = "")
public class TrdOrder {

  private long id;
  private String orderId;
  private String bankCardNum;
  private String prodCode;
  private int orderStatus;
  private long orderDate;
  private int orderType;
  private long payAmount;
  private Long payFee;
  private long userId;
  private long prodId;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private Long buyDiscount;
  private Long buyFee;
  private Long boughtDate;
  private Integer tradeType;
  private String fundCode;
  private Long fundNum;
  private Long fundNumConfirmed;
  private Long fundQuantity;
  private Integer orderDetailStatus;
  private String tradeApplySerial;

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
  @Column(name = "buy_discount")
  public Long getBuyDiscount() {
    return buyDiscount;
  }

  public void setBuyDiscount(Long buyDiscount) {
    this.buyDiscount = buyDiscount;
  }

  @Basic
  @Column(name = "buy_fee")
  public Long getBuyFee() {
    return buyFee;
  }

  public void setBuyFee(Long buyFee) {
    this.buyFee = buyFee;
  }

  @Basic
  @Column(name = "bought_date")
  public Long getBoughtDate() {
    return boughtDate;
  }

  public void setBoughtDate(Long boughtDate) {
    this.boughtDate = boughtDate;
  }

  @Basic
  @Column(name = "trade_type")
  public Integer getTradeType() {
    return tradeType;
  }

  public void setTradeType(Integer tradeType) {
    this.tradeType = tradeType;
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
  @Column(name = "fund_num")
  public Long getFundNum() {
    return fundNum;
  }

  public void setFundNum(Long fundNum) {
    this.fundNum = fundNum;
  }

  @Basic
  @Column(name = "fund_num_confirmed")
  public Long getFundNumConfirmed() {
    return fundNumConfirmed;
  }

  public void setFundNumConfirmed(Long fundNumConfirmed) {
    this.fundNumConfirmed = fundNumConfirmed;
  }

  @Basic
  @Column(name = "fund_quantity")
  public Long getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(Long fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  @Basic
  @Column(name = "order_detail_status")
  public Integer getOrderDetailStatus() {
    return orderDetailStatus;
  }

  public void setOrderDetailStatus(Integer orderDetailStatus) {
    this.orderDetailStatus = orderDetailStatus;
  }

  @Basic
  @Column(name = "trade_apply_serial")
  public String getTradeApplySerial() {
    return tradeApplySerial;
  }

  public void setTradeApplySerial(String tradeApplySerial) {
    this.tradeApplySerial = tradeApplySerial;
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
    if (prodCode != null ? !prodCode.equals(trdOrder.prodCode) : trdOrder.prodCode != null) {
      return false;
    }
    if (payFee != null ? !payFee.equals(trdOrder.payFee) : trdOrder.payFee != null) {
      return false;
    }
    if (buyDiscount != null ? !buyDiscount.equals(trdOrder.buyDiscount)
        : trdOrder.buyDiscount != null) {
      return false;
    }
    if (buyFee != null ? !buyFee.equals(trdOrder.buyFee) : trdOrder.buyFee != null) {
      return false;
    }
    if (boughtDate != null ? !boughtDate.equals(trdOrder.boughtDate)
        : trdOrder.boughtDate != null) {
      return false;
    }
    if (tradeType != null ? !tradeType.equals(trdOrder.tradeType) : trdOrder.tradeType != null) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(trdOrder.fundCode) : trdOrder.fundCode != null) {
      return false;
    }
    if (fundNum != null ? !fundNum.equals(trdOrder.fundNum) : trdOrder.fundNum != null) {
      return false;
    }
    if (fundNumConfirmed != null ? !fundNumConfirmed.equals(trdOrder.fundNumConfirmed)
        : trdOrder.fundNumConfirmed != null) {
      return false;
    }
    if (fundQuantity != null ? !fundQuantity.equals(trdOrder.fundQuantity)
        : trdOrder.fundQuantity != null) {
      return false;
    }
    if (orderDetailStatus != null ? !orderDetailStatus.equals(trdOrder.orderDetailStatus)
        : trdOrder.orderDetailStatus != null) {
      return false;
    }
    if (tradeApplySerial != null ? !tradeApplySerial.equals(trdOrder.tradeApplySerial)
        : trdOrder.tradeApplySerial != null) {
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
    result = 31 * result + (payFee != null ? payFee.hashCode() : 0);
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (prodId ^ (prodId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (buyDiscount != null ? buyDiscount.hashCode() : 0);
    result = 31 * result + (buyFee != null ? buyFee.hashCode() : 0);
    result = 31 * result + (boughtDate != null ? boughtDate.hashCode() : 0);
    result = 31 * result + (tradeType != null ? tradeType.hashCode() : 0);
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (fundNum != null ? fundNum.hashCode() : 0);
    result = 31 * result + (fundNumConfirmed != null ? fundNumConfirmed.hashCode() : 0);
    result = 31 * result + (fundQuantity != null ? fundQuantity.hashCode() : 0);
    result = 31 * result + (orderDetailStatus != null ? orderDetailStatus.hashCode() : 0);
    result = 31 * result + (tradeApplySerial != null ? tradeApplySerial.hashCode() : 0);
    return result;
  }
}
