package com.shellshellfish.aaas.common.message.order;

import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class ProdSellDTO {

  private long userProdId;
  private long prodId;
  private long groupId;
  private long userId;
  private Long sellNum;
  private String userUuid;

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

  public Long getSellNum() {
    return sellNum;
  }

  public void setSellNum(Long sellNum) {
    this.sellNum = sellNum;
  }
}