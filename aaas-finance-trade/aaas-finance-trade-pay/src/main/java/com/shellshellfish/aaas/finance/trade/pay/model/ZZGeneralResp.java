package com.shellshellfish.aaas.finance.trade.pay.model;

import java.util.List;

/**
 * Created by chenwei on 2018- 四月 - 20
 */

public class ZZGeneralResp<T> {
  String status;
  String errno;
  String msg;
  List<T> data;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrno() {
    return errno;
  }

  public void setErrno(String errno) {
    this.errno = errno;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }
}
