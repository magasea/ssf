package com.shellshellfish.aaas.common.grpc.trade.order;

/**
 * Created by chenwei on 2018- 一月 - 29
 */
//Please make sure this class synchronized with origin TrdOrderDetail
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
  private long fundSum;
  private Long fundNumConfirmed;
  private long fundSumConfirmed;
  private int fundShare;
  private long buyFee;
  private long buyDiscount;
  private int orderDetailStatus;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private String bankCardNum;
  private String errMsg;
  private Long fundQuantity;
  private Long prodId;

  
  
  
  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  
  
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  
  
  public String getTradeApplySerial() {
    return tradeApplySerial;
  }

  public void setTradeApplySerial(String tradeApplySerial) {
    this.tradeApplySerial = tradeApplySerial;
  }

  
  
  public Long getBuysellDate() {
    return buysellDate;
  }

  public void setBuysellDate(long buysellDate) {
    this.buysellDate = buysellDate;
  }

  
  
  public int getTradeType() {
    return tradeType;
  }

  public void setTradeType(int tradeType) {
    this.tradeType = tradeType;
  }

  
  
  public Long getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(long payAmount) {
    this.payAmount = payAmount;
  }

  
  
  public Long getPayFee() {
    return payFee;
  }

  public void setPayFee(long payFee) {
    this.payFee = payFee;
  }

  
  
  public Long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  
  
  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
  }

  
  
  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  
  
  public Long getFundMoneyQuantity() {
    return fundMoneyQuantity;
  }

  public void setFundMoneyQuantity(long fundMoneyQuantity) {
    this.fundMoneyQuantity = fundMoneyQuantity;
  }

  
  
  public Long getFundNum() {
    return fundNum;
  }

  public void setFundNum(long fundNum) {
    this.fundNum = fundNum;
  }

  
  
  public Long getFundSum() {
    return fundSum;
  }

  public void setFundSum(long fundSum) {
    this.fundSum = fundSum;
  }

  
  
  public Long getFundNumConfirmed() {
    return fundNumConfirmed;
  }

  public void setFundNumConfirmed(Long fundNumConfirmed) {
    this.fundNumConfirmed = fundNumConfirmed;
  }

  
  
  public Long getFundSumConfirmed() {
    return fundSumConfirmed;
  }

  public void setFundSumConfirmed(long fundSumConfirmed) {
    this.fundSumConfirmed = fundSumConfirmed;
  }

  
  
  public int getFundShare() {
    return fundShare;
  }

  public void setFundShare(int fundShare) {
    this.fundShare = fundShare;
  }

  
  
  public Long getBuyFee() {
    return buyFee;
  }

  public void setBuyFee(long buyFee) {
    this.buyFee = buyFee;
  }

  
  
  public Long getBuyDiscount() {
    return buyDiscount;
  }

  public void setBuyDiscount(long buyDiscount) {
    this.buyDiscount = buyDiscount;
  }

  
  
  public int getOrderDetailStatus() {
    return orderDetailStatus;
  }

  public void setOrderDetailStatus(int orderDetailStatus) {
    this.orderDetailStatus = orderDetailStatus;
  }

  
  
  public Long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  
  
  public Long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  
  
  public Long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  
  
  public Long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  
  
  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
  }

  
  
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  
  
  public Long getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(Long fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  
  
  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  
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
    if (fundSum != that.fundSum) {
      return false;
    }
    if (fundSumConfirmed != that.fundSumConfirmed) {
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
    if (fundNumConfirmed != null ? !fundNumConfirmed.equals(that.fundNumConfirmed)
        : that.fundNumConfirmed != null) {
      return false;
    }
    if (bankCardNum != null ? !bankCardNum.equals(that.bankCardNum) : that.bankCardNum != null) {
      return false;
    }
    if (errMsg != null ? !errMsg.equals(that.errMsg) : that.errMsg != null) {
      return false;
    }
    if (fundQuantity != null ? !fundQuantity.equals(that.fundQuantity)
        : that.fundQuantity != null) {
      return false;
    }
    if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) {
      return false;
    }

    return true;
  }

  
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
    result = 31 * result + (int) (fundSum ^ (fundSum >>> 32));
    result = 31 * result + (fundNumConfirmed != null ? fundNumConfirmed.hashCode() : 0);
    result = 31 * result + (int) (fundSumConfirmed ^ (fundSumConfirmed >>> 32));
    result = 31 * result + fundShare;
    result = 31 * result + (int) (buyFee ^ (buyFee >>> 32));
    result = 31 * result + (int) (buyDiscount ^ (buyDiscount >>> 32));
    result = 31 * result + orderDetailStatus;
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (errMsg != null ? errMsg.hashCode() : 0);
    result = 31 * result + (fundQuantity != null ? fundQuantity.hashCode() : 0);
    result = 31 * result + (prodId != null ? prodId.hashCode() : 0);
    return result;
  }
}
