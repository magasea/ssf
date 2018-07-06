package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by developer4 on 2018- 六月 - 28
 */

public class TrdPayFlowExt implements Serializable {
  String confirmDateExpected ;

  public String getConfirmDateExpected() {
    if(!StringUtils.isEmpty(confirmDateExpected) && !confirmDateExpected.contains("-")){
      if(confirmDateExpected.length() >= 8){
        return confirmDateExpected.substring(0,4)+"-"+confirmDateExpected.substring(4,6)+"-" +
            confirmDateExpected.substring(6,8);
      }
    }
    return confirmDateExpected;
  }

  public void setConfirmDateExpected(String confirmDateExpected) {
    this.confirmDateExpected = confirmDateExpected;
  }
}
