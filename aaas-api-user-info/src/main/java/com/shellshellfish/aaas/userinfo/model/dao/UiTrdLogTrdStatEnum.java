package com.shellshellfish.aaas.userinfo.model.dao;

public enum UiTrdLogTrdStatEnum {
  CONFIRMING(0),
  CONFIRMED(1),
  OTHER(3);
  private final int value;
  UiTrdLogTrdStatEnum(int value){
    this.value = value;
  }
}
