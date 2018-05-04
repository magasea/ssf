package com.shellshellfish.aaas.zhongzhengapi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developer4 on 2018- 五月 - 03
 */

public class CancelTradeResult {
  @SerializedName("applyserial")
  String applySerial;

  @SerializedName("requestdate")
  String requestDate;

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public String getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }
}
