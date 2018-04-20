package com.shellshellfish.aaas.finance.trade.order.model.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class ProdSellPercentDTO {



private long userProdId;
  private long prodId;

  private long groupId;
  private long userId;
  private String userUuid;
  private String userBankNum;
  private BigDecimal sellTargetPercent;

  public BigDecimal getSellTargetPercent() {
    return sellTargetPercent;
  }

  public void setSellTargetPercent(BigDecimal sellTargetPercent) {
    this.sellTargetPercent = sellTargetPercent;
  }

  public long getUserProdId() {
    return userProdId;
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


  
  public void setUserProdId(long userProdId) {
		this.userProdId = userProdId;
	}

  public String getUserBankNum() {
    return userBankNum;
  }

  public void setUserBankNum(String userBankNum) {
    this.userBankNum = userBankNum;
  }
}
