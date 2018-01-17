package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 20
 */

public class OrderStatusChangeDTO implements Serializable{
  String trdAccount;
  int trdBrokerId;
  Long userProdId;
  String userUuid;
  int orderStatus;
  String userPid;
  Long userId;

  public Long getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Long orderDate) {
    this.orderDate = orderDate;
  }

  Long orderDate;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  int orderType;



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

  public int getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
  }

}
