package com.shellshellfish.aaas.zhongzhengapi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 21
 */

public class ZZBonusInfo {
  @SerializedName("identityno")
  String identityNo;
  @SerializedName("fundcode")
  String fundCode;
  @SerializedName("sharetype")
  String shareType;
  @SerializedName("tradeacco")
  String tradeAcco;
  @SerializedName("confirmdate")
  String confirmDate;
  @SerializedName("enrolldate")
  String enrollDate;
  @SerializedName("enrollshare")
  String enrollShare;
  @SerializedName("factbonussum")
  String factBonusSum;
  @SerializedName("freezeshare")
  String freezeShare;
  @SerializedName("freezesum")
  String freezeSum;
  @SerializedName("fundacco")
  String fundAcco;
  @SerializedName("fundname")
  String fundName;
  @SerializedName("incometax")
  String incomeTax;
  @SerializedName("meloncutting")
  String melonCutting;
  @SerializedName("melonmethod")
  String melonMethod;
  @SerializedName("netvalue")
  String netValue;
  @SerializedName("poundage")
  String poundage;
  @SerializedName("shareperbonus")
  String sharePerBonus;
  @SerializedName("sumperbonus")
  String sumperBonus;
  @SerializedName("taconfirmserial")
  String taConfirmSerial;
  @SerializedName("occurbankacco")
  String occurBankAcco;
  @SerializedName("occurbankno")
  String occurBankNo;

  public String getIdentityNo() {
    return identityNo;
  }

  public void setIdentityNo(String identityNo) {
    this.identityNo = identityNo;
  }

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

  public String getSumperBonus() {
    return sumperBonus;
  }

  public void setSumperBonus(String sumperBonus) {
    this.sumperBonus = sumperBonus;
  }

  public String getTaConfirmSerial() {
    return taConfirmSerial;
  }

  public void setTaConfirmSerial(String taConfirmSerial) {
    this.taConfirmSerial = taConfirmSerial;
  }

  public String getOccurBankAcco() {
    return occurBankAcco;
  }

  public void setOccurBankAcco(String occurBankAcco) {
    this.occurBankAcco = occurBankAcco;
  }

  public String getOccurBankNo() {
    return occurBankNo;
  }

  public void setOccurBankNo(String occurBankNo) {
    this.occurBankNo = occurBankNo;
  }

  public String getTradeAcco() {
    return tradeAcco;
  }

  public void setTradeAcco(String tradeAcco) {
    this.tradeAcco = tradeAcco;
  }

  public String getConfirmDate() {
    return confirmDate;
  }

  public void setConfirmDate(String confirmDate) {
    this.confirmDate = confirmDate;
  }

  public String getEnrollDate() {
    return enrollDate;
  }

  public void setEnrollDate(String enrollDate) {
    this.enrollDate = enrollDate;
  }

  public String getEnrollShare() {
    return enrollShare;
  }

  public void setEnrollShare(String enrollShare) {
    this.enrollShare = enrollShare;
  }

  public String getFactBonusSum() {
    return factBonusSum;
  }

  public void setFactBonusSum(String factBonusSum) {
    this.factBonusSum = factBonusSum;
  }

  public String getFreezeShare() {
    return freezeShare;
  }

  public void setFreezeShare(String freezeShare) {
    this.freezeShare = freezeShare;
  }

  public String getFreezeSum() {
    return freezeSum;
  }

  public void setFreezeSum(String freezeSum) {
    this.freezeSum = freezeSum;
  }

  public String getFundAcco() {
    return fundAcco;
  }

  public void setFundAcco(String fundAcco) {
    this.fundAcco = fundAcco;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getIncomeTax() {
    return incomeTax;
  }

  public void setIncomeTax(String incomeTax) {
    this.incomeTax = incomeTax;
  }

  public String getMelonCutting() {
    return melonCutting;
  }

  public void setMelonCutting(String melonCutting) {
    this.melonCutting = melonCutting;
  }

  public String getMelonMethod() {
    return melonMethod;
  }

  public void setMelonMethod(String melonMethod) {
    this.melonMethod = melonMethod;
  }

  public String getNetValue() {
    return netValue;
  }

  public void setNetValue(String netValue) {
    this.netValue = netValue;
  }

  public String getPoundage() {
    return poundage;
  }

  public void setPoundage(String poundage) {
    this.poundage = poundage;
  }

  public String getSharePerBonus() {
    return sharePerBonus;
  }

  public void setSharePerBonus(String sharePerBonus) {
    this.sharePerBonus = sharePerBonus;
  }
}
