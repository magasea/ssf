package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;

/**
 * Created by developer4 on 2018- 六月 - 28
 */

public class TrdPayFlowExt implements Serializable {
  String confirmDateExpected ;

  public String getConfirmDateExpected() {
    return confirmDateExpected;
  }

  public void setConfirmDateExpected(String confirmDateExpected) {
    this.confirmDateExpected = confirmDateExpected;
  }
}