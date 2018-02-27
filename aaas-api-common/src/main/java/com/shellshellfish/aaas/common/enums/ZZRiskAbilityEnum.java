package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 一月 - 18
 */
public enum ZZRiskAbilityEnum {
    CONSERV(1,"保守型"), STABLE(2, "稳健型"), BALANCE(3, "平衡型"),
    INPROVING(4, "成长型"), AGGRESSIVE(5, "进取型");

  int riskLevel ;
  String comment;

    ZZRiskAbilityEnum(int riskLevel, String comment){
      this.riskLevel = riskLevel;
      this.comment = comment;
    }

  public int getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(int riskLevel) {
    this.riskLevel = riskLevel;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

}
