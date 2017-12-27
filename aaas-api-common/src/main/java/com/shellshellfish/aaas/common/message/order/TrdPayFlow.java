package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */




public class TrdPayFlow implements Serializable{

  private long id;
  private long orderDetailId;
  private String tradeAcco;
  private long tradeBrokeId;
  private String applySerial;
  private String outsideOrderno;
  private String bankCardNum;
  private long userProdId;
  private String fundCode;
  private int payStatus;
  private long payDate;
  private int payType;
  private Long payAmount;
  private long fundSum;
  private long fundSumConfirmed;
  private long buyFee;
  private long buyDiscount;
  private long userId;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private String prodCode;
  private String errMsg;

  
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  
  
  public long getOrderDetailId() {
    return orderDetailId;
  }

  public void setOrderDetailId(long orderDetailId) {
    this.orderDetailId = orderDetailId;
  }

  
  
  public String getTradeAcco() {
    return tradeAcco;
  }

  public void setTradeAcco(String tradeAcco) {
    this.tradeAcco = tradeAcco;
  }

  
  
  public long getTradeBrokeId() {
    return tradeBrokeId;
  }

  public void setTradeBrokeId(long tradeBrokeId) {
    this.tradeBrokeId = tradeBrokeId;
  }

  
  
  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  
  
  public String getOutsideOrderno() {
    return outsideOrderno;
  }

  public void setOutsideOrderno(String outsideOrderno) {
    this.outsideOrderno = outsideOrderno;
  }

  
  
  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
  }

  
  
  public long getUserProdId() {
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

  
  
  public int getPayStatus() {
    return payStatus;
  }

  public void setPayStatus(int payStatus) {
    this.payStatus = payStatus;
  }

  
  
  public long getPayDate() {
    return payDate;
  }

  public void setPayDate(long payDate) {
    this.payDate = payDate;
  }

  
  
  public int getPayType() {
    return payType;
  }

  public void setPayType(int payType) {
    this.payType = payType;
  }

  
  
  public Long getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(Long payAmount) {
    this.payAmount = payAmount;
  }

  
  
  public long getFundSum() {
    return fundSum;
  }

  public void setFundSum(long fundSum) {
    this.fundSum = fundSum;
  }

  
  
  public long getFundSumConfirmed() {
    return fundSumConfirmed;
  }

  public void setFundSumConfirmed(long fundSumConfirmed) {
    this.fundSumConfirmed = fundSumConfirmed;
  }

  
  
  public long getBuyFee() {
    return buyFee;
  }

  public void setBuyFee(long buyFee) {
    this.buyFee = buyFee;
  }

  
  
  public long getBuyDiscount() {
    return buyDiscount;
  }

  public void setBuyDiscount(long buyDiscount) {
    this.buyDiscount = buyDiscount;
  }

  
  
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  
  
  public long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  
  
  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  
  
  public long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  
  
  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  
  
  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  
  
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrdPayFlow that = (TrdPayFlow) o;

    if (id != that.id) {
      return false;
    }
    if (orderDetailId != that.orderDetailId) {
      return false;
    }
    if (tradeBrokeId != that.tradeBrokeId) {
      return false;
    }
    if (userProdId != that.userProdId) {
      return false;
    }
    if (payStatus != that.payStatus) {
      return false;
    }
    if (payDate != that.payDate) {
      return false;
    }
    if (payType != that.payType) {
      return false;
    }
    if (fundSum != that.fundSum) {
      return false;
    }
    if (fundSumConfirmed != that.fundSumConfirmed) {
      return false;
    }
    if (buyFee != that.buyFee) {
      return false;
    }
    if (buyDiscount != that.buyDiscount) {
      return false;
    }
    if (userId != that.userId) {
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
    if (tradeAcco != null ? !tradeAcco.equals(that.tradeAcco) : that.tradeAcco != null) {
      return false;
    }
    if (applySerial != null ? !applySerial.equals(that.applySerial) : that.applySerial != null) {
      return false;
    }
    if (outsideOrderno != null ? !outsideOrderno.equals(that.outsideOrderno)
        : that.outsideOrderno != null) {
      return false;
    }
    if (bankCardNum != null ? !bankCardNum.equals(that.bankCardNum) : that.bankCardNum != null) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(that.fundCode) : that.fundCode != null) {
      return false;
    }
    if (payAmount != null ? !payAmount.equals(that.payAmount) : that.payAmount != null) {
      return false;
    }
    if (prodCode != null ? !prodCode.equals(that.prodCode) : that.prodCode != null) {
      return false;
    }

    return true;
  }

  
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (orderDetailId ^ (orderDetailId >>> 32));
    result = 31 * result + (tradeAcco != null ? tradeAcco.hashCode() : 0);
    result = 31 * result + (int) (tradeBrokeId ^ (tradeBrokeId >>> 32));
    result = 31 * result + (applySerial != null ? applySerial.hashCode() : 0);
    result = 31 * result + (outsideOrderno != null ? outsideOrderno.hashCode() : 0);
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + payStatus;
    result = 31 * result + (int) (payDate ^ (payDate >>> 32));
    result = 31 * result + payType;
    result = 31 * result + (payAmount != null ? payAmount.hashCode() : 0);
    result = 31 * result + (int) (fundSum ^ (fundSum >>> 32));
    result = 31 * result + (int) (fundSumConfirmed ^ (fundSumConfirmed >>> 32));
    result = 31 * result + (int) (buyFee ^ (buyFee >>> 32));
    result = 31 * result + (int) (buyDiscount ^ (buyDiscount >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (prodCode != null ? prodCode.hashCode() : 0);
    return result;
  }
}
