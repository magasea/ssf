package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2017- 十二月 - 21
 */
public enum UserRiskLevelEnum {
  CONSERV(0,"保守型"), STABLE(1, "稳健型"), BALANCE(2, "平衡型"),
  INPROVING(3, "成长型"), AGGRESSIVE(9, "进取型");

  public int getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(int RiskLevel) {
    this.riskLevel = RiskLevel;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private int riskLevel;
  private String comment;
  UserRiskLevelEnum(int riskLevel, String comment){
    this.riskLevel = riskLevel;
    this.comment = comment;
  }
  
}
