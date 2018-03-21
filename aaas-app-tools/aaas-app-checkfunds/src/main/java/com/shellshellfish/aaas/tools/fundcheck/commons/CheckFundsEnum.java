package com.shellshellfish.aaas.tools.fundcheck.commons;

/**
 * Created by chenwei on 2018- 三月 - 09
 */
public enum CheckFundsEnum {
  UPDATEFAILED(-2, "不能同步成功"),DIFFERENT(-1,"不一致"),CONSIST(0, "一致"), UPDATESUCCESS(1,"同步后一致");
  int status;
  String comment;
  CheckFundsEnum(int status, String comment){
    this.comment = comment;
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
