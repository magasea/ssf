package com.shellshellfish.aaas.userinfo.model.dto;

import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class ProdSellPageDTO {

  private long userProId;
  private long prodId;
  private long groupId;
  private long userId;
  private String userUuid;
  List<ProdDtlSellPageDTO> prodDtlSellPageDTOList;

  public long getUserProId() {
    return userProId;
  }

  public void setUserProId(long userProId) {
    this.userProId = userProId;
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

  public List<ProdDtlSellPageDTO> getProdDtlSellPageDTOList() {
    return prodDtlSellPageDTOList;
  }

  public void setProdDtlSellPageDTOList(
      List<ProdDtlSellPageDTO> prodDtlSellPageDTOList) {
    this.prodDtlSellPageDTOList = prodDtlSellPageDTOList;
  }
}
