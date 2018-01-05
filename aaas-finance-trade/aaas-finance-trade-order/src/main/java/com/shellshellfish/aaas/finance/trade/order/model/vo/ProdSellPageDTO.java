package com.shellshellfish.aaas.finance.trade.order.model.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class ProdSellPageDTO {



private long userProdId;
  private long prodId;

  private long groupId;
  private long userId;
  private String userUuid;

  public BigDecimal getSellTargetMoney() {
    return sellTargetMoney;
  }

  public void setSellTargetMoney(BigDecimal sellTargetMoney) {
    this.sellTargetMoney = sellTargetMoney;
  }

  private BigDecimal sellTargetMoney;
  List<ProdDtlSellPageDTO> prodDtlSellPageDTOList;

  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProId(long userProdId) {
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

  public List<ProdDtlSellPageDTO> getProdDtlSellPageDTOList() {
    return prodDtlSellPageDTOList;
  }

  public void setProdDtlSellPageDTOList(
      List<ProdDtlSellPageDTO> prodDtlSellPageDTOList) {
    this.prodDtlSellPageDTOList = prodDtlSellPageDTOList;
  }
  
  public void setUserProdId(long userProdId) {
		this.userProdId = userProdId;
	}
}
