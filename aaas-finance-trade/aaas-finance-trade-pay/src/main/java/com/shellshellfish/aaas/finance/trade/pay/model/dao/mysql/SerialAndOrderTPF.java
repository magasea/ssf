package com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chenwei on 2018- 六月 - 13
 */
@Getter
@Setter
public class SerialAndOrderTPF {
   String applySerial;
   String outsideOrderno;
   long orderDetailId;

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public String getOutsideOrderno() {
    return outsideOrderno;
  }

  public void setOutsideOrderno(String outsideOrderno) {
    this.outsideOrderno = outsideOrderno;
  }

  public long getOrderDetailId() {
    return orderDetailId;
  }

  public void setOrderDetailId(long orderDetailId) {
    this.orderDetailId = orderDetailId;
  }
}
