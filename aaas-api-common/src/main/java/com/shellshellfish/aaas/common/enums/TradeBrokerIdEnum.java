package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2017- 十二月 - 18
 */
public enum TradeBrokerIdEnum {
  ZhongZhenCaifu(0L);

  public Long getTradeBrokerId() {
    return tradeBrokerId;
  }

  public void setTradeBrokerId(Long tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
  }

  private Long tradeBrokerId;
  TradeBrokerIdEnum(Long tradeBrokerId){
    this.tradeBrokerId = tradeBrokerId;
  }
}
