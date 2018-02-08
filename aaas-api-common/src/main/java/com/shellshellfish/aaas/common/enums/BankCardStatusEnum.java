package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 一月 - 18
 */

public enum BankCardStatusEnum {
  VALID(1, "生效"), INVALID(-1, "失效");
  int status;
  String comment;

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

  BankCardStatusEnum(int status, String comment){
    this.status = status;
    this.comment = comment;
  }

}
