package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class ProdSellDTO  implements Serializable{

  private long userProdId;
  private long prodId;
  private long groupId;
  private long userId;
  private long trdBrokerId;
  private String orderId;
  //精确到分的整型
  private Long sellTargetMoney;
  private String userUuid;
  private String userBankNum;


  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  private String userPid;

  public String getTrdAcco() {
    return trdAcco;
  }

  public void setTrdAcco(String trdAcco) {
    this.trdAcco = trdAcco;
  }

  public List<ProdDtlSellDTO> getProdDtlSellDTOList() {
    return prodDtlSellDTOList;
  }

  public void setProdDtlSellDTOList(
      List<ProdDtlSellDTO> prodDtlSellDTOList) {
    this.prodDtlSellDTOList = prodDtlSellDTOList;
  }

  private String trdAcco;
  List<ProdDtlSellDTO> prodDtlSellDTOList;

  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
  }

  public long getProdId() {
    return prodId;
  }

  public void setProdId(long prodId) {
    this.prodId = prodId;
  }

  public long getGroupId() {
    return groupId;
  }

  public void setGroupId(long groupId) {
    this.groupId = groupId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUserUuid() {
    return userUuid;
  }

  public void setUserUuid(String userUuid) {
    this.userUuid = userUuid;
  }

  public Long getSellTargetMoney() {
    return sellTargetMoney;
  }

  public void setSellTargetMoney(Long sellTargetMoney) {
    this.sellTargetMoney = sellTargetMoney;
  }

  public long getTrdBrokerId() {
    return trdBrokerId;
  }

  public void setTrdBrokerId(long trdBrokerId) {
    this.trdBrokerId = trdBrokerId;
  }

  public String getUserBankNum() {
    return userBankNum;
  }

  public void setUserBankNum(String userBankNum) {
    this.userBankNum = userBankNum;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
