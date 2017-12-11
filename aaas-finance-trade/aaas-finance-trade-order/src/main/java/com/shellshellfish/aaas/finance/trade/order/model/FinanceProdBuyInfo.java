package com.shellshellfish.aaas.finance.trade.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceProdBuyInfo {
  Long money;//金额单位到分
  Long prodId;//产品编号
  Long groupId;//产品分组编号
  Long userId;//用户
  String bankAcc;//银行账户
  String orderId;//订单号ID

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  int orderType; //1-购买 2-分红 3-赎回

  public Long getMoney() {
    return money;
  }

  public void setMoney(Long money) {
    this.money = money;
  }

  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getBankAcc() {
    return bankAcc;
  }

  public void setBankAcc(String bankAcc) {
    this.bankAcc = bankAcc;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
