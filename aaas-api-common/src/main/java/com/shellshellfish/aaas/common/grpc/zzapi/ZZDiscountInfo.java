package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developer4 on 2018- 五月 - 21
 */

public class ZZDiscountInfo {
  @SerializedName("fundcode")
  String fundCode;
  @SerializedName("businflag")
  String businFlag;
  @SerializedName("discount")
  String disCount;

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getBusinFlag() {
    return businFlag;
  }

  public void setBusinFlag(String businFlag) {
    this.businFlag = businFlag;
  }

  public String getDisCount() {
    return disCount;
  }

  public void setDisCount(String disCount) {
    this.disCount = disCount;
  }
}
