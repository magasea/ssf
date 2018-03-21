package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 三月 - 15
 */

public enum AdjustTypeEnum {
  ESTABLISH(1, "组合建立"), INCREASE_DEBET(2,"调高偏债类资产配置"), REBALANCE(3,"资产配置再平衡"),
  RISKSTART_USA(4,"启动美股风控"), RISKCONTINUE_USA(5, "美股风险持续"), RISKSTOK(6, "股票市场波动较大"),
  RISKDISMISS_END(7, "美股波动减弱，本轮风控结束");

  int typeId;
  String typeComment;

  AdjustTypeEnum(int typeId, String typeComment){
    this.typeId = typeId;
    this.typeComment = typeComment;
  }

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public String getTypeComment() {
    return typeComment;
  }

  public void setTypeComment(String typeComment) {
    this.typeComment = typeComment;
  }
}
