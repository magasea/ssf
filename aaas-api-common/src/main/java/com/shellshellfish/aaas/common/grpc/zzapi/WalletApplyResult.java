package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 15
 */

public class WalletApplyResult {
  @SerializedName("applyserial")
  String applySerial;
  @SerializedName("capitalmodel")
  String capitalModel;
  @SerializedName("requestdate")
  String requestDate;
  @SerializedName("outsideorderno")
  String outsideOrderNo;

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
}
