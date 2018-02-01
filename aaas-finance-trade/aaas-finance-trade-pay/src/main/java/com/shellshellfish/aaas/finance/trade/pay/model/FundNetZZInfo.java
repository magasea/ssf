package com.shellshellfish.aaas.finance.trade.pay.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by chenwei on 2018- 一月 - 31
 */

public class FundNetZZInfo implements Serializable{
  @SerializedName("FUND_CODE")
  String fundCode;
  @SerializedName("UNIT_NET")
  String unitNet;
  @SerializedName("TENTHOU_UNIT_INCM")
  String tenThouUnitIncm;
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

  public String getTenThouUnitIncm() {
    return tenThouUnitIncm;
  }

  public void setTenThouUnitIncm(String tenThouUnitIncm) {
    this.tenThouUnitIncm = tenThouUnitIncm;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
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
}
