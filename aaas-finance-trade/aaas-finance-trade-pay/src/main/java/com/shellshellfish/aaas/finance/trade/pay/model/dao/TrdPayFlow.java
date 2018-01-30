package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 29
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
  private long trdConfirmDate;
  private int trdType;
  private Long tradeTargetSum;
  private long tradeTargetShare;
  private long tradeConfirmShare;
  private long tradeConfirmSum;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  public Long getTradeBrokeId() {
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
  @Column(name = "trd_confirm_date")
  public long getTrdConfirmDate() {
    return trdConfirmDate;
  }

  public void setTrdConfirmDate(long trdConfirmDate) {
    this.trdConfirmDate = trdConfirmDate;
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
  @Column(name = "trade_target_sum")
  public Long getTradeTargetSum() {
    return tradeTargetSum;
  }

  public void setTradeTargetSum(Long tradeTargetSum) {
    this.tradeTargetSum = tradeTargetSum;
  }

  @Basic
  @Column(name = "trade_target_share")
  public long getTradeTargetShare() {
    return tradeTargetShare;
  }

  public void setTradeTargetShare(long tradeTargetShare) {
    this.tradeTargetShare = tradeTargetShare;
  }

  @Basic
  @Column(name = "trade_confirm_share")
  public long getTradeConfirmShare() {
    return tradeConfirmShare;
  }

  public void setTradeConfirmShare(long tradeConfirmShare) {
    this.tradeConfirmShare = tradeConfirmShare;
  }

  @Basic
  @Column(name = "trade_confirm_sum")
  public long getTradeConfirmSum() {
    return tradeConfirmSum;
  }

  public void setTradeConfirmSum(long tradeConfirmSum) {
    this.tradeConfirmSum = tradeConfirmSum;
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
    if (trdConfirmDate != that.trdConfirmDate) {
      return false;
    }
    if (trdType != that.trdType) {
      return false;
    }
    if (tradeTargetShare != that.tradeTargetShare) {
      return false;
    }
    if (tradeConfirmShare != that.tradeConfirmShare) {
      return false;
    }
    if (tradeConfirmSum != that.tradeConfirmSum) {
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
    if (tradeTargetSum != null ? !tradeTargetSum.equals(that.tradeTargetSum)
        : that.tradeTargetSum != null) {
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
    result = 31 * result + (int) (trdConfirmDate ^ (trdConfirmDate >>> 32));
    result = 31 * result + trdType;
    result = 31 * result + (tradeTargetSum != null ? tradeTargetSum.hashCode() : 0);
    result = 31 * result + (int) (tradeTargetShare ^ (tradeTargetShare >>> 32));
    result = 31 * result + (int) (tradeConfirmShare ^ (tradeConfirmShare >>> 32));
    result = 31 * result + (int) (tradeConfirmSum ^ (tradeConfirmSum >>> 32));
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
