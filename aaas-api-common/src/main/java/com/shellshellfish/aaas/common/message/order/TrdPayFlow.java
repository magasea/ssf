package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by chenwei on 2018- 五月 - 18
 */



public class TrdPayFlow implements Serializable {

  private long id;
  private long orderDetailId;
  private String tradeAcco;
  private long tradeBrokeId;
  private String applySerial;
  private String outsideOrderno;
  private String bankCardNum;
  private long userProdId;
  private String fundCode;
  private int trdStatus = Integer.MIN_VALUE;
  private long trdConfirmDate;
  private int trdType;
  private String trdApplyDate;
  private long applydateUnitvalue;
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
  private TrdPayFlowExt trdPayFlowExt;

  public TrdPayFlow(){
    this.trdPayFlowExt = new TrdPayFlowExt();
  }
  
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

  
  
  public int getTrdStatus() {
    return trdStatus;
  }

  public void setTrdStatus(int trdStatus) {
    this.trdStatus = trdStatus;
  }

  
  
  public long getTrdConfirmDate() {
    return trdConfirmDate;
  }

  public void setTrdConfirmDate(long trdConfirmDate) {
    this.trdConfirmDate = trdConfirmDate;
  }

  
  
  public int getTrdType() {
    return trdType;
  }

  public void setTrdType(int trdType) {
    this.trdType = trdType;
  }

  
  
  public String getTrdApplyDate() {
    return trdApplyDate;
  }

  public void setTrdApplyDate(String trdApplyDate) {
    this.trdApplyDate = trdApplyDate;
  }

  
  
  public long getApplydateUnitvalue() {
    return applydateUnitvalue;
  }

  public void setApplydateUnitvalue(long applydateUnitvalue) {
    this.applydateUnitvalue = applydateUnitvalue;
  }

  
  
  public long getTrdApplySum() {
    return trdApplySum;
  }

  public void setTrdApplySum(long trdApplySum) {
    this.trdApplySum = trdApplySum;
  }

  
  
  public long getTrdApplyShare() {
    return trdApplyShare;
  }

  public void setTrdApplyShare(long trdApplyShare) {
    this.trdApplyShare = trdApplyShare;
  }

  
  
  public Long getTradeTargetSum() {
    return tradeTargetSum;
  }

  public void setTradeTargetSum(Long tradeTargetSum) {
    this.tradeTargetSum = tradeTargetSum;
  }

  
  
  public long getTradeTargetShare() {
    return tradeTargetShare;
  }

  public void setTradeTargetShare(long tradeTargetShare) {
    this.tradeTargetShare = tradeTargetShare;
  }

  
  
  public long getTradeConfirmShare() {
    return tradeConfirmShare;
  }

  public void setTradeConfirmShare(long tradeConfirmShare) {
    this.tradeConfirmShare = tradeConfirmShare;
  }

  
  
  public long getTradeConfirmSum() {
    return tradeConfirmSum;
  }

  public void setTradeConfirmSum(long tradeConfirmSum) {
    this.tradeConfirmSum = tradeConfirmSum;
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

  
  
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  
  
  public String getErrCode() {
    return errCode;
  }

  public void setErrCode(String errCode) {
    this.errCode = errCode;
  }

  
  
  public Integer getTrdbkerStatusCode() {
    return trdbkerStatusCode;
  }

  public void setTrdbkerStatusCode(Integer trdbkerStatusCode) {
    this.trdbkerStatusCode = trdbkerStatusCode;
  }

  
  
  public String getTrdbkerStatusName() {
    return trdbkerStatusName;
  }

  public void setTrdbkerStatusName(String trdbkerStatusName) {
    this.trdbkerStatusName = trdbkerStatusName;
  }

  public TrdPayFlowExt getTrdPayFlowExt() {
    return trdPayFlowExt;
  }

  public void setTrdPayFlowExt(TrdPayFlowExt trdPayFlowExt) {
    this.trdPayFlowExt = trdPayFlowExt;
  }

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
        applydateUnitvalue == that.applydateUnitvalue &&
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

  
  public int hashCode() {

    return Objects
        .hash(id, orderDetailId, tradeAcco, tradeBrokeId, applySerial, outsideOrderno, bankCardNum,
            userProdId, fundCode, trdStatus, trdConfirmDate, trdType, trdApplyDate,
            applydateUnitvalue, trdApplySum, trdApplyShare, tradeTargetSum, tradeTargetShare,
            tradeConfirmShare, tradeConfirmSum, buyFee, buyDiscount, userId, createBy, createDate,
            updateBy, updateDate, errMsg, errCode, trdbkerStatusCode, trdbkerStatusName);
  }
}
