package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 20
 */

public class PayPreOrderDto implements Serializable{
  String trdAccount;
  int trdBrokerId;
  Long userProdId;
  String userUuid;
  String originFundCode;
  String userPid;
  List<TrdOrderDetail> orderDetailList;

  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  public String getOriginFundCode() {
    return originFundCode;
  }

  public void setOriginFundCode(String originFundCode) {
    this.originFundCode = originFundCode;
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
}
