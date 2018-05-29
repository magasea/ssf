package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 四月 - 19
 */

 public  class ZZBankInfo {
  @SerializedName("bankname")
  String bankName;
  @SerializedName("bankserial")
  String bankSerial;
  @SerializedName("capitalmodel")
  String capitalModel;
  @SerializedName("money_limit_one")
  String moneyLimitOne;
  @SerializedName("money_limit_day")
  String moneyLimitDay;

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankSerial() {
    return bankSerial;
  }

  public void setBankSerial(String bankSerial) {
    this.bankSerial = bankSerial;
  }

  public String getCapitalModel() {
    return capitalModel;
  }

  public void setCapitalModel(String capitalModel) {
    this.capitalModel = capitalModel;
  }

  public String getMoneyLimitOne() {
    return moneyLimitOne;
  }

  public void setMoneyLimitOne(String moneyLimitOne) {
    this.moneyLimitOne = moneyLimitOne;
  }

  public String getMoneyLimitDay() {
    return moneyLimitDay;
  }

  public void setMoneyLimitDay(String moneyLimitDay) {
    this.moneyLimitDay = moneyLimitDay;
  }
}
