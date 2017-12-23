package com.shellshellfish.aaas.common.grpc.trade.pay;

/**
 * Created by chenwei on 2017- 十二月 - 22
 */

public class BindBankCard {

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
  }

  public String getCellphone() {
    return cellphone;
  }

  public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public String getTradeBrokerName() {
    return tradeBrokerName;
  }

  public void setTradeBrokerName(String tradeBrokerName) {
    this.tradeBrokerName = tradeBrokerName;
  }

  public Long getTradeBrokerId() {
    return tradeBrokerId;
  }

  public void setTradeBrokerId(Long tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
  }

  Long userId;
  String userPid;
  String bankCardNum;
  String cellphone;
  String userName;
  String bankCode;
  String tradeBrokerName;
  Long tradeBrokerId;
}
