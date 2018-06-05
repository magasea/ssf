package com.shellshellfish.aaas.common.enums;

/**
 * Created by developer4 on 2018- 六月 - 04
 */

public enum PendingRecordStatus {
  NOTHANDLED(0, "NOTHANDLED"), HANDLED(1, "HANDLED");
  PendingRecordStatus(int status, String comment){
    this.status = status;
    this.comment = comment;
  }
  int status;
  String comment;



}
