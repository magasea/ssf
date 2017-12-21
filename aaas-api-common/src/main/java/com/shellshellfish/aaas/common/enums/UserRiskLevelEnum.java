package com.shellshellfish.aaas.common.enums;

import java.util.HashMap;
import java.util.Map;

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
  private static final Map lookup =
      new HashMap();
  static {
    //Create reverse lookup hash map
    for(UserRiskLevelEnum d : UserRiskLevelEnum.values())
      lookup.put(d.getComment(), d);
  }
  public static UserRiskLevelEnum get(String userRiskLevelStr) {
    //the reverse lookup by simply getting
    //the value from the lookup HsahMap.
    return (UserRiskLevelEnum) lookup.get(userRiskLevelStr);
  }
  private int riskLevel;
  private String comment;
  UserRiskLevelEnum(int riskLevel, String comment){
    this.riskLevel = riskLevel;
    this.comment = comment;
  }
  
}
