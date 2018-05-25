package com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 五月 - 18
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
  private String trdApplyDate;
  private long trdAplydateUv;
  private long trdApplySum;
  private long trdApplyShare;
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
  @Column(name = "trd_apply_date")
  public String getTrdApplyDate() {
    return trdApplyDate;
  }

  public void setTrdApplyDate(String trdApplyDate) {
    this.trdApplyDate = trdApplyDate;
  }

  @Basic
  @Column(name = "trd_aplydate_uv")
  public long getTrdAplydateUv() {
    return trdAplydateUv;
  }

  public void setTrdAplydateUv(long trdAplydateUv) {
    this.trdAplydateUv = trdAplydateUv;
  }

  @Basic
  @Column(name = "trd_apply_sum")
  public long getTrdApplySum() {
    return trdApplySum;
  }

  public void setTrdApplySum(long trdApplySum) {
    this.trdApplySum = trdApplySum;
  }

  @Basic
  @Column(name = "trd_apply_share")
  public long getTrdApplyShare() {
    return trdApplyShare;
  }

  public void setTrdApplyShare(long trdApplyShare) {
    this.trdApplyShare = trdApplyShare;
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
    return id == that.id &&
        orderDetailId == that.orderDetailId &&
        tradeBrokeId == that.tradeBrokeId &&
        userProdId == that.userProdId &&
        trdStatus == that.trdStatus &&
        trdConfirmDate == that.trdConfirmDate &&
        trdType == that.trdType &&
        trdAplydateUv == that.trdAplydateUv &&
        trdApplySum == that.trdApplySum &&
        trdApplyShare == that.trdApplyShare &&
        tradeTargetShare == that.tradeTargetShare &&
        tradeConfirmShare == that.tradeConfirmShare &&
        tradeConfirmSum == that.tradeConfirmSum &&
        buyFee == that.buyFee &&
        buyDiscount == that.buyDiscount &&
        userId == that.userId &&
        createBy == that.createBy &&
        createDate == that.createDate &&
        updateBy == that.updateBy &&
        updateDate == that.updateDate &&
        Objects.equals(tradeAcco, that.tradeAcco) &&
        Objects.equals(applySerial, that.applySerial) &&
        Objects.equals(outsideOrderno, that.outsideOrderno) &&
        Objects.equals(bankCardNum, that.bankCardNum) &&
        Objects.equals(fundCode, that.fundCode) &&
        Objects.equals(trdApplyDate, that.trdApplyDate) &&
        Objects.equals(tradeTargetSum, that.tradeTargetSum) &&
        Objects.equals(errMsg, that.errMsg) &&
        Objects.equals(errCode, that.errCode) &&
        Objects.equals(trdbkerStatusCode, that.trdbkerStatusCode) &&
        Objects.equals(trdbkerStatusName, that.trdbkerStatusName);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(id, orderDetailId, tradeAcco, tradeBrokeId, applySerial, outsideOrderno, bankCardNum,
            userProdId, fundCode, trdStatus, trdConfirmDate, trdType, trdApplyDate, trdAplydateUv,
            trdApplySum, trdApplyShare, tradeTargetSum, tradeTargetShare, tradeConfirmShare,
            tradeConfirmSum, buyFee, buyDiscount, userId, createBy, createDate, updateBy,
            updateDate,
            errMsg, errCode, trdbkerStatusCode, trdbkerStatusName);
  }
}
