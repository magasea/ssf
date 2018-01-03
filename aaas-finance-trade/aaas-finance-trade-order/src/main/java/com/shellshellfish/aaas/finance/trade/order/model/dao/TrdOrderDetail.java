package com.shellshellfish.aaas.finance.trade.order.model.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 02
 */

@Entity
@Table(name = "trd_order_detail", schema = "ssftrdorder", catalog = "")
public class TrdOrderDetail {


  private long id;
  private String orderId;
  private String tradeApplySerial;
  private long buysellDate;
  private int tradeType;
  private long payAmount;
  private long payFee;
  private long userId;
  private long userProdId;
  private String fundCode;
  private long fundMoneyQuantity;
  private long fundNum;
  private long fundNumConfirmed;
  private int fundShare;
  private long buyFee;
  private long buyDiscount;
  private int orderDetailStatus;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private String bankCardNum;
  private Long fundQuantity;
  private Integer orderStatus;
  private String prodCode;
  private Long prodId;
  private String errMsg;

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
  @Column(name = "trade_apply_serial")
  public String getTradeApplySerial() {
    return tradeApplySerial;
  }

  public void setTradeApplySerial(String tradeApplySerial) {
    this.tradeApplySerial = tradeApplySerial;
  }

  @Basic
  @Column(name = "buysell_date")
  public long getBuysellDate() {
    return buysellDate;
  }

  public void setBuysellDate(long buysellDate) {
    this.buysellDate = buysellDate;
  }

  @Basic
  @Column(name = "trade_type")
  public int getTradeType() {
    return tradeType;
  }

  public void setTradeType(int tradeType) {
    this.tradeType = tradeType;
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
  @Column(name = "user_prod_id")
  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
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
  @Column(name = "fund_money_quantity")
  public long getFundMoneyQuantity() {
    return fundMoneyQuantity;
  }

  public void setFundMoneyQuantity(long fundMoneyQuantity) {
    this.fundMoneyQuantity = fundMoneyQuantity;
  }

  @Basic
  @Column(name = "fund_num")
  public long getFundNum() {
    return fundNum;
  }

  public void setFundNum(long fundNum) {
    this.fundNum = fundNum;
  }

  @Basic
  @Column(name = "fund_num_confirmed")
  public long getFundNumConfirmed() {
    return fundNumConfirmed;
  }

  public void setFundNumConfirmed(long fundNumConfirmed) {
    this.fundNumConfirmed = fundNumConfirmed;
  }

  @Basic
  @Column(name = "fund_share")
  public int getFundShare() {
    return fundShare;
  }

  public void setFundShare(int fundShare) {
    this.fundShare = fundShare;
  }

  @Basic
  @Column(name = "buy_fee")
  public long getBuyFee() {
    return buyFee;
  }

  public void setBuyFee(long buyFee) {
    this.buyFee = buyFee;
  }

  @Basic
  @Column(name = "buy_discount")
  public long getBuyDiscount() {
    return buyDiscount;
  }

  public void setBuyDiscount(long buyDiscount) {
    this.buyDiscount = buyDiscount;
  }

  @Basic
  @Column(name = "order_detail_status")
  public int getOrderDetailStatus() {
    return orderDetailStatus;
  }

  public void setOrderDetailStatus(int orderDetailStatus) {
    this.orderDetailStatus = orderDetailStatus;
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
  @Column(name = "bank_card_num")
  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
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
  @Column(name = "order_status")
  public Integer getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(Integer orderStatus) {
    this.orderStatus = orderStatus;
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
  @Column(name = "prod_id")
  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  @Basic
  @Column(name = "err_msg")
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrdOrderDetail that = (TrdOrderDetail) o;

    if (id != that.id) {
      return false;
    }
    if (buysellDate != that.buysellDate) {
      return false;
    }
    if (tradeType != that.tradeType) {
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
    if (userProdId != that.userProdId) {
      return false;
    }
    if (fundMoneyQuantity != that.fundMoneyQuantity) {
      return false;
    }
    if (fundNum != that.fundNum) {
      return false;
    }
    if (fundNumConfirmed != that.fundNumConfirmed) {
      return false;
    }
    if (fundShare != that.fundShare) {
      return false;
    }
    if (buyFee != that.buyFee) {
      return false;
    }
    if (buyDiscount != that.buyDiscount) {
      return false;
    }
    if (orderDetailStatus != that.orderDetailStatus) {
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
    if (tradeApplySerial != null ? !tradeApplySerial.equals(that.tradeApplySerial)
        : that.tradeApplySerial != null) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(that.fundCode) : that.fundCode != null) {
      return false;
    }
    if (bankCardNum != null ? !bankCardNum.equals(that.bankCardNum) : that.bankCardNum != null) {
      return false;
    }
    if (fundQuantity != null ? !fundQuantity.equals(that.fundQuantity)
        : that.fundQuantity != null) {
      return false;
    }
    if (orderStatus != null ? !orderStatus.equals(that.orderStatus) : that.orderStatus != null) {
      return false;
    }
    if (prodCode != null ? !prodCode.equals(that.prodCode) : that.prodCode != null) {
      return false;
    }
    if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) {
      return false;
    }
    if (errMsg != null ? !errMsg.equals(that.errMsg) : that.errMsg != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
    result = 31 * result + (tradeApplySerial != null ? tradeApplySerial.hashCode() : 0);
    result = 31 * result + (int) (buysellDate ^ (buysellDate >>> 32));
    result = 31 * result + tradeType;
    result = 31 * result + (int) (payAmount ^ (payAmount >>> 32));
    result = 31 * result + (int) (payFee ^ (payFee >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (int) (fundMoneyQuantity ^ (fundMoneyQuantity >>> 32));
    result = 31 * result + (int) (fundNum ^ (fundNum >>> 32));
    result = 31 * result + (int) (fundNumConfirmed ^ (fundNumConfirmed >>> 32));
    result = 31 * result + fundShare;
    result = 31 * result + (int) (buyFee ^ (buyFee >>> 32));
    result = 31 * result + (int) (buyDiscount ^ (buyDiscount >>> 32));
    result = 31 * result + orderDetailStatus;
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (fundQuantity != null ? fundQuantity.hashCode() : 0);
    result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
    result = 31 * result + (prodCode != null ? prodCode.hashCode() : 0);
    result = 31 * result + (prodId != null ? prodId.hashCode() : 0);
    result = 31 * result + (errMsg != null ? errMsg.hashCode() : 0);
    return result;
  }
}
