package com.shellshellfish.aaas.common.enums;

public enum TrdFailtureStatusEnum {
  SHARENOTENOUGH(1309,"1309:可用份额不足"),NETERROR(1018,"1018:网络错误"),APPLYSHARESMALL(1019,"1019:申请值太小"),
  BOOKSHARELESSHOLD(1326,"1326:赎回后全商户下，账面份额低于最低可持有份额"),OUTORDERIDREPEAT(1013,"1013:该外部订单号重复");
  private int status;
  private String comment;

  TrdFailtureStatusEnum(int status, String comment) {
    this.status = status;
    this.comment = comment;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}


