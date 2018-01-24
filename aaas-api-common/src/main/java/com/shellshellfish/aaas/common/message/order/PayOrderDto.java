package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 20
 */

public class PayOrderDto implements Serializable{
  String trdAccount;
  int trdBrokerId;
  Long userProdId;
  String userUuid;
  int riskLevel;
  String userPid;
  List<TrdOrderDetail> orderDetailList;


  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  public int getTrdBrokerId() {
    return trdBrokerId;
  }

  public void setTrdBrokerId(int trdBrokeId) {
    this.trdBrokerId = trdBrokeId;
  }

  public String getUserUuid() {
    return userUuid;
  }

  public void setUserUuid(String userUuid) {
    this.userUuid = userUuid;
  }

  public String getTrdAccount() {
    return trdAccount;
  }

  public void setTrdAccount(String trdAccount) {
    this.trdAccount = trdAccount;
  }

  public List<TrdOrderDetail> getOrderDetailList() {
    return orderDetailList;
  }

  public void setOrderDetailList(
      List<TrdOrderDetail> orderDetailList) {
    this.orderDetailList = orderDetailList;
  }

  public int getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(int riskLevel) {
    this.riskLevel = riskLevel;
  }
}
