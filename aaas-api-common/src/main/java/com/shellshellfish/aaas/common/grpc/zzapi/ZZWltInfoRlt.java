package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 15
 */

public class ZZWltInfoRlt {
  @SerializedName("sharetype")
  String shareType;
  @SerializedName("fundname")
  String fundName;
  @SerializedName("fundstate")
  String fundState;
  @SerializedName("fundtype")
  String fundType;
  @SerializedName("tacode")
  String taCode;
  @SerializedName("fundrisklevel")
  String fundRiskLevel;
  @SerializedName("withdrawstate")
  String withDrawState;
  @SerializedName("declarestate")
  String deClareState;
  @SerializedName("subscribestate")
  String subScribeState;
  @SerializedName("trendstate")
  String trendState;
  @SerializedName("valuagrstate")
  String valuagrState;
  @SerializedName("pernetvalue")
  String pernetValue;
  @SerializedName("navdate")
  String navDate;
  @SerializedName("hfincomeratio")
  String hfinComeRatio;
  @SerializedName("dayinc")
  String dayInc;
  @SerializedName("incomeratio")
  String incomeRatio;
  @SerializedName("totalnetvalue")
  String totalNetValue;
  @SerializedName("minshare")
  String minShare;
  @SerializedName("transflag")
  String transFlag;

  public String getShareType() {
    return shareType;
  }

  public void setShareType(String shareType) {
    this.shareType = shareType;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getFundState() {
    return fundState;
  }

  public void setFundState(String fundState) {
    this.fundState = fundState;
  }

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    this.fundType = fundType;
  }

  public String getTaCode() {
    return taCode;
  }

  public void setTaCode(String taCode) {
    this.taCode = taCode;
  }

  public String getFundRiskLevel() {
    return fundRiskLevel;
  }

  public void setFundRiskLevel(String fundRiskLevel) {
    this.fundRiskLevel = fundRiskLevel;
  }

  public String getWithDrawState() {
    return withDrawState;
  }

  public void setWithDrawState(String withDrawState) {
    this.withDrawState = withDrawState;
  }

  public String getDeClareState() {
    return deClareState;
  }

  public void setDeClareState(String deClareState) {
    this.deClareState = deClareState;
  }

  public String getSubScribeState() {
    return subScribeState;
  }

  public void setSubScribeState(String subScribeState) {
    this.subScribeState = subScribeState;
  }

  public String getTrendState() {
    return trendState;
  }

  public void setTrendState(String trendState) {
    this.trendState = trendState;
  }

  public String getValuagrState() {
    return valuagrState;
  }

  public void setValuagrState(String valuagrState) {
    this.valuagrState = valuagrState;
  }

  public String getPernetValue() {
    return pernetValue;
  }

  public void setPernetValue(String pernetValue) {
    this.pernetValue = pernetValue;
  }

  public String getNavDate() {
    return navDate;
  }

  public void setNavDate(String navDate) {
    this.navDate = navDate;
  }

  public String getHfinComeRatio() {
    return hfinComeRatio;
  }

  public void setHfinComeRatio(String hfinComeRatio) {
    this.hfinComeRatio = hfinComeRatio;
  }

  public String getDayInc() {
    return dayInc;
  }

  public void setDayInc(String dayInc) {
    this.dayInc = dayInc;
  }

  public String getIncomeRatio() {
    return incomeRatio;
  }

  public void setIncomeRatio(String incomeRatio) {
    this.incomeRatio = incomeRatio;
  }

  public String getTotalNetValue() {
    return totalNetValue;
  }

  public void setTotalNetValue(String totalNetValue) {
    this.totalNetValue = totalNetValue;
  }

  public String getMinShare() {
    return minShare;
  }

  public void setMinShare(String minShare) {
    this.minShare = minShare;
  }

  public String getTransFlag() {
    return transFlag;
  }

  public void setTransFlag(String transFlag) {
    this.transFlag = transFlag;
  }
}
