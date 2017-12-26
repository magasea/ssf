package com.shellshellfish.aaas.common.enums;

public enum TrdOrderOpTypeEnum {
  BUY(1), SELL(2), DIVIDINVEST(3), DIVIDMONEY(4);

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  private int operation;
  TrdOrderOpTypeEnum(int operation){
    this.operation = operation;
  }

}
