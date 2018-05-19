package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 16
 */

public class ZZSellWltRlt {
  @SerializedName("applyserial")
  String applySerial;
  @SerializedName("acceptdate")
  String acceptDate;
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

  public String getAcceptDate() {
    return acceptDate;
  }

  public void setAcceptDate(String acceptDate) {
    this.acceptDate = acceptDate;
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
