package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 17
 */

public class ZZTradeLimit {
  @SerializedName("fundCode")
  String fundCode;
  @SerializedName("shareType")
  String shareType;
  @SerializedName("maxValue")
  String maxValue;
  @SerializedName("minValue")
  String minValue;
  @SerializedName("sndMinValue")
  String sndMinValue;
  @SerializedName("businFlag")
  String businFlag;
  @SerializedName("capitalmode")
  String capitalMode;

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

  public String getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(String maxValue) {
    this.maxValue = maxValue;
  }

  public String getMinValue() {
    return minValue;
  }

  public void setMinValue(String minValue) {
    this.minValue = minValue;
  }

  public String getSndMinValue() {
    return sndMinValue;
  }

  public void setSndMinValue(String sndMinValue) {
    this.sndMinValue = sndMinValue;
  }

  public String getBusinFlag() {
    return businFlag;
  }

  public void setBusinFlag(String businFlag) {
    this.businFlag = businFlag;
  }

  public String getCapitalMode() {
    return capitalMode;
  }

  public void setCapitalMode(String capitalMode) {
    this.capitalMode = capitalMode;
  }
}
