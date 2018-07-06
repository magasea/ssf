package com.shellshellfish.aaas.common.enums;

public enum ActionTypeEnum {
  BANNERACTION(1,"轮播图跳转");
  private int operation;
  String comment;
  ActionTypeEnum(int operation, String comment) {
    this.operation = operation;
    this.comment = comment;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
