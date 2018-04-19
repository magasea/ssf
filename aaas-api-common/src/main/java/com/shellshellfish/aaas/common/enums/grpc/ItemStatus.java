package com.shellshellfish.aaas.common.enums.grpc;

/**
 * Created by chenwei on 2018- 四月 - 19
 */
public enum ItemStatus {
  SUCCESS(0, "成功"),FAIL(-1, "失败");
  int status;
  String comment;

  ItemStatus(int status, String comment){
    this.status = status;
    this.comment = comment;
  }

}
