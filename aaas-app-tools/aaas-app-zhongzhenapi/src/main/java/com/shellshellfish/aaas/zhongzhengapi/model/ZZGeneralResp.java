package com.shellshellfish.aaas.zhongzhengapi.model;

/**
 * Created by chenwei on 2018- 四月 - 20
 */

public class ZZGeneralResp<T>  {
  String status;
  String errno;
  String msg;
  T data;



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

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }


}
