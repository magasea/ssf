package com.shellshellfish.aaas.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 10
 */

@Entity
@Table(name = "trd_pay_flow", schema = "ssftrdpay", catalog = "")
public class TrdPayFlow {

  private long id;
  private long orderDetailId;
  private String tradeAcco;
  private long tradeBrokeId;
  private String applySerial;
  private String outsideOrderno;
  private String bankCardNum;
  private long userProdId;
  private String fundCode;
  private int trdStatus;
  private long trdDate;
  private int trdType;
  private Long trdMoneyAmount;
  private long fundSum;
  private long fundSumConfirmed;
  private long buyFee;
  private long buyDiscount;
  private long userId;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private String errMsg;
  private String errCode;
  private Integer trdbkerStatusCode;
  private String trdbkerStatusName;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "order_detail_id")
  public long getOrderDetailId() {
    return orderDetailId;
  }

  public void setOrderDetailId(long orderDetailId) {
    this.orderDetailId = orderDetailId;
  }

  @Basic
  @Column(name = "trade_acco")
  public String getTradeAcco() {
    return tradeAcco;
  }

  public void setTradeAcco(String tradeAcco) {
    this.tradeAcco = tradeAcco;
  }

  @Basic
  @Column(name = "trade_broke_id")
  public long getTradeBrokeId() {
    return tradeBrokeId;
  }

  public void setTradeBrokeId(long tradeBrokeId) {
    this.tradeBrokeId = tradeBrokeId;
  }

  @Basic
  @Column(name = "apply_serial")
  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  @Basic
  @Column(name = "outside_orderno")
  public String getOutsideOrderno() {
    return outsideOrderno;
  }

  public void setOutsideOrderno(String outsideOrderno) {
    this.outsideOrderno = outsideOrderno;
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
  @Column(name = "trd_status")
  public int getTrdStatus() {
    return trdStatus;
  }

  public void setTrdStatus(int trdStatus) {
    this.trdStatus = trdStatus;
  }

  @Basic
  @Column(name = "trd_date")
  public long getTrdDate() {
    return trdDate;
  }

  public void setTrdDate(long trdDate) {
    this.trdDate = trdDate;
  }

  @Basic
  @Column(name = "trd_type")
  public int getTrdType() {
    return trdType;
  }

  public void setTrdType(int trdType) {
    this.trdType = trdType;
  }

  @Basic
  @Column(name = "trd_money_amount")
  public Long getTrdMoneyAmount() {
    return trdMoneyAmount;
  }

  public void setTrdMoneyAmount(Long trdMoneyAmount) {
    this.trdMoneyAmount = trdMoneyAmount;
  }

  @Basic
  @Column(name = "fund_sum")
  public long getFundSum() {
    return fundSum;
  }

  public void setFundSum(long fundSum) {
    this.fundSum = fundSum;
  }

  @Basic
  @Column(name = "fund_sum_confirmed")
  public long getFundSumConfirmed() {
    return fundSumConfirmed;
  }

  public void setFundSumConfirmed(long fundSumConfirmed) {
    this.fundSumConfirmed = fundSumConfirmed;
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
  @Column(name = "user_id")
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
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
  @Column(name = "err_msg")
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Basic
  @Column(name = "err_code")
  public String getErrCode() {
    return errCode;
  }

  public void setErrCode(String errCode) {
    this.errCode = errCode;
  }

  @Basic
  @Column(name = "trdbker_status_code")
  public Integer getTrdbkerStatusCode() {
    return trdbkerStatusCode;
  }

  public void setTrdbkerStatusCode(Integer trdbkerStatusCode) {
    this.trdbkerStatusCode = trdbkerStatusCode;
  }

  @Basic
  @Column(name = "trdbker_status_name")
  public String getTrdbkerStatusName() {
    return trdbkerStatusName;
  }

  public void setTrdbkerStatusName(String trdbkerStatusName) {
    this.trdbkerStatusName = trdbkerStatusName;
  }

  @Override
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
    if (trdStatus != that.trdStatus) {
      return false;
    }
    if (trdDate != that.trdDate) {
      return false;
    }
    if (trdType != that.trdType) {
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
    if (trdMoneyAmount != null ? !trdMoneyAmount.equals(that.trdMoneyAmount)
        : that.trdMoneyAmount != null) {
      return false;
    }
    if (errMsg != null ? !errMsg.equals(that.errMsg) : that.errMsg != null) {
      return false;
    }
    if (errCode != null ? !errCode.equals(that.errCode) : that.errCode != null) {
      return false;
    }
    if (trdbkerStatusCode != null ? !trdbkerStatusCode.equals(that.trdbkerStatusCode)
        : that.trdbkerStatusCode != null) {
      return false;
    }
    if (trdbkerStatusName != null ? !trdbkerStatusName.equals(that.trdbkerStatusName)
        : that.trdbkerStatusName != null) {
      return false;
    }

    return true;
  }

  @Override
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
    result = 31 * result + trdStatus;
    result = 31 * result + (int) (trdDate ^ (trdDate >>> 32));
    result = 31 * result + trdType;
    result = 31 * result + (trdMoneyAmount != null ? trdMoneyAmount.hashCode() : 0);
    result = 31 * result + (int) (fundSum ^ (fundSum >>> 32));
    result = 31 * result + (int) (fundSumConfirmed ^ (fundSumConfirmed >>> 32));
    result = 31 * result + (int) (buyFee ^ (buyFee >>> 32));
    result = 31 * result + (int) (buyDiscount ^ (buyDiscount >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (errMsg != null ? errMsg.hashCode() : 0);
    result = 31 * result + (errCode != null ? errCode.hashCode() : 0);
    result = 31 * result + (trdbkerStatusCode != null ? trdbkerStatusCode.hashCode() : 0);
    result = 31 * result + (trdbkerStatusName != null ? trdbkerStatusName.hashCode() : 0);
    return result;
  }
}
