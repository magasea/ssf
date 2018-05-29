package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 17
 */

public class ZZFundShareInfo {
  @SerializedName("tradeacco")
  String tradeAcco;
  @SerializedName("fundacco")
  String fundAcco;
  @SerializedName("fundcode")
  String fundCode;
  @SerializedName("fundname")
  String fundName;
  @SerializedName("sharetype")
  String shareType;
  @SerializedName("currentremainshare")
  String currentRemainShare;
  @SerializedName("usableremainshare")
  String usableRemainShare;
  @SerializedName("freezeremainshare")
  String freezeRemainShare;
  @SerializedName("melonmethod")
  String melonMethod;
  @SerializedName("tfreezeremainshare")
  String tfreezeRemainShare;
  @SerializedName("unpaidincome")
  String unpaidIncome;
  @SerializedName("bankacco")
  String bankAcco;
  @SerializedName("bankname")
  String bankName;
  @SerializedName("pernetvalue")
  String perNetvalue;
  @SerializedName("marketvalue")
  String marketValue;
  @SerializedName("navdate")
  String navDate;
  @SerializedName("fundtype")
  String fundType;
  @SerializedName("fundstate")
  String fundState;
  @SerializedName("bankserial")
  String bankSerial;
  @SerializedName("capitalmode")
  String capitalMode;
  @SerializedName("tacode")
  String taCode;

  public String getTradeAcco() {
    return tradeAcco;
  }

  public void setTradeAcco(String tradeAcco) {
    this.tradeAcco = tradeAcco;
  }

  public String getFundAcco() {
    return fundAcco;
  }

  public void setFundAcco(String fundAcco) {
    this.fundAcco = fundAcco;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getShareType() {
    return shareType;
  }

  public void setShareType(String shareType) {
    this.shareType = shareType;
  }

  public String getCurrentRemainShare() {
    return currentRemainShare;
  }

  public void setCurrentRemainShare(String currentRemainShare) {
    this.currentRemainShare = currentRemainShare;
  }

  public String getUsableRemainShare() {
    return usableRemainShare;
  }

  public void setUsableRemainShare(String usableRemainShare) {
    this.usableRemainShare = usableRemainShare;
  }

  public String getFreezeRemainShare() {
    return freezeRemainShare;
  }

  public void setFreezeRemainShare(String freezeRemainShare) {
    this.freezeRemainShare = freezeRemainShare;
  }

  public String getMelonMethod() {
    return melonMethod;
  }

  public void setMelonMethod(String melonMethod) {
    this.melonMethod = melonMethod;
  }

  public String getTfreezeRemainShare() {
    return tfreezeRemainShare;
  }

  public void setTfreezeRemainShare(String tfreezeRemainShare) {
    this.tfreezeRemainShare = tfreezeRemainShare;
  }

  public String getUnpaidIncome() {
    return unpaidIncome;
  }

  public void setUnpaidIncome(String unpaidIncome) {
    this.unpaidIncome = unpaidIncome;
  }

  public String getBankAcco() {
    return bankAcco;
  }

  public void setBankAcco(String bankAcco) {
    this.bankAcco = bankAcco;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getPerNetvalue() {
    return perNetvalue;
  }

  public void setPerNetvalue(String perNetvalue) {
    this.perNetvalue = perNetvalue;
  }

  public String getMarketValue() {
    return marketValue;
  }

  public void setMarketValue(String marketValue) {
    this.marketValue = marketValue;
  }

  public String getNavDate() {
    return navDate;
  }

  public void setNavDate(String navDate) {
    this.navDate = navDate;
  }

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    this.fundType = fundType;
  }

  public String getFundState() {
    return fundState;
  }

  public void setFundState(String fundState) {
    this.fundState = fundState;
  }

  public String getBankSerial() {
    return bankSerial;
  }

  public void setBankSerial(String bankSerial) {
    this.bankSerial = bankSerial;
  }

  public String getCapitalMode() {
    return capitalMode;
  }

  public void setCapitalMode(String capitalMode) {
    this.capitalMode = capitalMode;
  }

  public String getTaCode() {
    return taCode;
  }

  public void setTaCode(String taCode) {
    this.taCode = taCode;
  }
}
