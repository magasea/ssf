package com.shellshellfish.aaas.zhongzhengapi.model;

/**
 * Created by chenwei on 2018- 四月 - 20
 */

public class ZZGeneralErrResp  {
  String status;
  String errno;
  String msg;
  Object data;

  public Object getReturnlist() {
    return returnlist;
  }

  public void setReturnlist(Object returnlist) {
    this.returnlist = returnlist;
  }

  Object returnlist;



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

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }


}
