package com.shellshellfish.aaas.finance.trade.order.model.dao;

public enum TrdOrderStatusEnum {
  WAITCONFIRM(0), CONFIRMED(1), FAILED(-1);
  private int status;
  TrdOrderStatusEnum(int status){
    this.status = status;
  }

}
