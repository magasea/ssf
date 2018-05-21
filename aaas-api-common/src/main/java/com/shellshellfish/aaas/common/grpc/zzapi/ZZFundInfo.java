package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 17
 */

public class ZZFundInfo {
  @SerializedName("fundcode")
  String fundCode;
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
  String declareState;
  @SerializedName("subscribestate")
  String subscribeState;
  @SerializedName("trendstate")
  String trendState;
  @SerializedName("valuagrstate")
  String valuagrState;
  @SerializedName("pernetvalue")
  String pernetValue;
  @SerializedName("navdate")
  String navDate;
  @SerializedName("hfincomeratio")
  String hfincomeRatio;
  @SerializedName("dayinc")
  String dayInc;
  @SerializedName("incomeratio")
  String incomeRatio;
  @SerializedName("totalnetvalue")
  String totalNetvalue;
  @SerializedName("minshare")
  String minShare;
  @SerializedName("transflag")
  String transFlag;
  @SerializedName("manager_company")
  String managerCompany;
  @SerializedName("trup_company")
  String trupCompany;
  @SerializedName("establish_date")
  String establishDate;
  @SerializedName("manager_id")
  String managerId;
  @SerializedName("fund_mananger")
  String fundMananger;
  @SerializedName("asset_totol")
  String assetTotol;

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

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

  public String getDeclareState() {
    return declareState;
  }

  public void setDeclareState(String declareState) {
    this.declareState = declareState;
  }

  public String getSubscribeState() {
    return subscribeState;
  }

  public void setSubscribeState(String subscribeState) {
    this.subscribeState = subscribeState;
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

  public String getHfincomeRatio() {
    return hfincomeRatio;
  }

  public void setHfincomeRatio(String hfincomeRatio) {
    this.hfincomeRatio = hfincomeRatio;
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

  public String getTotalNetvalue() {
    return totalNetvalue;
  }

  public void setTotalNetvalue(String totalNetvalue) {
    this.totalNetvalue = totalNetvalue;
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

  public String getManagerCompany() {
    return managerCompany;
  }

  public void setManagerCompany(String managerCompany) {
    this.managerCompany = managerCompany;
  }

  public String getTrupCompany() {
    return trupCompany;
  }

  public void setTrupCompany(String trupCompany) {
    this.trupCompany = trupCompany;
  }

  public String getEstablishDate() {
    return establishDate;
  }

  public void setEstablishDate(String establishDate) {
    this.establishDate = establishDate;
  }

  public String getManagerId() {
    return managerId;
  }

  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }

  public String getFundMananger() {
    return fundMananger;
  }

  public void setFundMananger(String fundMananger) {
    this.fundMananger = fundMananger;
  }

  public String getAssetTotol() {
    return assetTotol;
  }

  public void setAssetTotol(String assetTotol) {
    this.assetTotol = assetTotol;
  }
}
