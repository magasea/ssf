package com.shellshellfish.aaas.common.enums;

/**
 * Created by developer4 on 2018- 六月 - 04
 */

public enum PendingRecordStatusEnum {
  NOTHANDLED(0, "NOTHANDLED"), HANDLED(1, "HANDLED");
  PendingRecordStatusEnum(int status, String comment){
    this.status = status;
    this.comment = comment;
  }
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
}
