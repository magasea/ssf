package com.shellshellfish.aaas.userinfo.model.dao;

public enum UiTrdLogOperEnum {
  SELL(0),
  BUY(1),
  REBALANCE(2),
  DIVIDENTS(3),
  OTHER(4);
  private final int value;
  UiTrdLogOperEnum(int value){
    this.value = value;
  }
}
