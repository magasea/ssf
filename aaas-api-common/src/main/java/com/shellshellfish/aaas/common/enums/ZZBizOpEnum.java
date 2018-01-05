package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 一月 - 04
 */

public enum ZZBizOpEnum {
  BUY(20, "认购"),
  PURCHASE(22,"申购"),
  REDEEM(24,"普通赎回"),
  CONVERT(36,"转换"),
  FUNDCONVIN(137,"基金转换入"),
  FUNDCONVOUT(138,"基金转换出"),
  FIXINV(39,"定投"),
  FASTREDEM(98,"快速赎回"),
  FASTTRANSFERIN(234,"快速过户入"),
  CANCEL(53,"撤单");

  public int getOptVal() {
    return optVal;
  }

  public void setOptVal(int optVal) {
    this.optVal = optVal;
  }

  public String getOptName() {
    return optName;
  }

  public void setOptName(String optName) {
    this.optName = optName;
  }

  int optVal;
  String optName;
  ZZBizOpEnum(int optVal, String optName){
    this.optVal = optVal;
    this.optName = optName;
  }
}
