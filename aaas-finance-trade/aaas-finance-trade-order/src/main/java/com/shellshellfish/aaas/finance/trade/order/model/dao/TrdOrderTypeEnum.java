package com.shellshellfish.aaas.finance.trade.order.model.dao;

public enum TrdOrderTypeEnum {
  BUY(1), SELL(3), SHAREDIVID(2);
  private int type;
  TrdOrderTypeEnum(int type){
    this.type = type;
  }

}
