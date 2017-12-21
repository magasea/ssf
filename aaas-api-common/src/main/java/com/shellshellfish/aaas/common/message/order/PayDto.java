package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 20
 */

public class PayDto implements Serializable{
  String trdAccount;
  int trdBrokerId;

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

  String userUuid;
  List<TrdOrderDetail> orderDetailList;

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
