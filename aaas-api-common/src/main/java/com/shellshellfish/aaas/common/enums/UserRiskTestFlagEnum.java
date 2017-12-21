package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2017- 十二月 - 21
 */
public enum UserRiskTestFlagEnum {
  DONE(1,"已做"), NOTDONE(0, "未作");

  public int getRiskTestFlag() {
    return riskTestFlag;
  }

  public void setRiskTestFlag(int riskTestFlag) {
    this.riskTestFlag = riskTestFlag;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private int riskTestFlag;
  private String comment;
  UserRiskTestFlagEnum(int riskTestFlag, String comment){
    this.riskTestFlag = riskTestFlag;
    this.comment = comment;
  }
  
}
