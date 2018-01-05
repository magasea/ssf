package com.shellshellfish.aaas.common.enums;
public enum TrdOrderStatusEnum {
  WAITPAY(0), PAYWAITCONFIRM(1), CONFIRMED(2), WAITSELL(3), SELLWAITCONFIRM(4), PARTIALCONFIRMED
      (5), FAILED(-1), CANCEL(-2);

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  private int status;
  TrdOrderStatusEnum(int status){
    this.status = status;
  }

}
