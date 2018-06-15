package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 六月 - 04
 */

public class ZZFundNetInfo {
  @SerializedName("FUND_CODE")
  String fundCode;
  @SerializedName("UNIT_NET")
  String unitNet;
  @SerializedName("ACCUM_NET")
  String accumNet;
  @SerializedName("CHNG_PCT")
  String chngPct;
  @SerializedName("TRADEDATE")
  String tradeDate;

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getUnitNet() {
    return unitNet;
  }

  public void setUnitNet(String unitNet) {
    this.unitNet = unitNet;
  }

  public String getAccumNet() {
    return accumNet;
  }

  public void setAccumNet(String accumNet) {
    this.accumNet = accumNet;
  }

  public String getChngPct() {
    return chngPct;
  }

  public void setChngPct(String chngPct) {
    this.chngPct = chngPct;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }
}
