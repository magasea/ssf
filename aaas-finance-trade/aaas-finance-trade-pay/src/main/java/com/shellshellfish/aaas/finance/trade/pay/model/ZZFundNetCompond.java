package com.shellshellfish.aaas.finance.trade.pay.model;

import java.util.List;

/**
 * Created by chenwei on 2018- 一月 - 31
 */

public class ZZFundNetCompond {
  String status;
  String errno;
  String msg;
  List<FundNetZZInfo> data;

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

  public List<FundNetZZInfo> getData() {
    return data;
  }

  public void setData(List<FundNetZZInfo> data) {
    this.data = data;
  }
}
