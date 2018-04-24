package com.shellshellfish.aaas.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceProdBuyInfo {
  BigDecimal money;//金额单位到分
  Long prodId;//产品ID
  Long userProdId;//用户的理财产品ID
  String prodCode;//产品编码
  Long groupId;//产品分组编号
  Long userId;//用户
  String uuid; //用户 uuid
  String bankAcc;//银行账户
  String orderId;//订单号ID
  Integer oemid = 1;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  int orderType; //1-购买 2-分红 3-赎回

  public BigDecimal getMoney() {
    return money;
  }

  public void setMoney(BigDecimal money) {
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

  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }
  
  public Integer getOemid() {
    return oemid;
  }

  public void setOemid(Integer oemid) {
    this.oemid = oemid;
  }

	@Override
	public String toString() {
		return "FinanceProdBuyInfo [money=" + money + ", prodId=" + prodId + ", userProdId=" + userProdId + ", prodCode="
				+ prodCode + ", groupId=" + groupId + ", userId=" + userId + ", uuid=" + uuid + ", bankAcc=" + bankAcc
				+ ", orderId=" + orderId + ", orderType=" + orderType + "]";
	}
  
  
  
}
