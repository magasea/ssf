package com.shellshellfish.aaas.zhongzhengapi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developer4 on 2018- 五月 - 18
 */

public class ZZBuyResult {
  @SerializedName("applyserial")
  String applySerial;
  @SerializedName("capitalmodel")
  String capitalModel;
  @SerializedName("requestdate")
  String requestDate;
  @SerializedName("outsideorderno")
  String outsideOrderNo;
  @SerializedName("kkstat")
  String kkStat;

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public String getCapitalModel() {
    return capitalModel;
  }

  public void setCapitalModel(String capitalModel) {
    this.capitalModel = capitalModel;
  }

  public String getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }

  public String getOutsideOrderNo() {
    return outsideOrderNo;
  }

  public void setOutsideOrderNo(String outsideOrderNo) {
    this.outsideOrderNo = outsideOrderNo;
  }

  public String getKkStat() {
    return kkStat;
  }

  public void setKkStat(String kkStat) {
    this.kkStat = kkStat;
  }
}
