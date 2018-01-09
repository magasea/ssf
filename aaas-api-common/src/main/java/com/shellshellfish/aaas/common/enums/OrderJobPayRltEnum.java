package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 一月 - 06
 */

public enum OrderJobPayRltEnum {
  SUCCUSS(1), FAILED(-1);
  int result;


  OrderJobPayRltEnum(int i) {
    result = i;
  }

  int getResult(){
    return result;
  }
}
