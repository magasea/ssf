package com.shellshellfish.aaas.common.enums;

public enum TrdZZCheckStatusEnum {
  CONFIRMFAILED(0,"确认失败"), CONFIRMSUCCESS(1, "确认成功"),
  REALTIMECONFIRMSUCESS(3, "实时确认成功"), CANCELED(4, "已经撤销"), ACTIONCONFIRMED(5,"行为确认"), NOTHANDLED(9,
      "未处理"), NUMWAITCONFIRM(10, "份额待确认 - 过户订单暂未发起");

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

  private int status;
  private String comment;
  TrdZZCheckStatusEnum(int status, String comment){
    this.status = status;
    this.comment = comment;
  }

  public static String getComment(int status){

    for (TrdZZCheckStatusEnum enumItem : TrdZZCheckStatusEnum.values()) {
      if (enumItem.getStatus() == status) {
        return enumItem.getComment();
      }
    }
    throw new IllegalArgumentException("input status:"+status+" is illeagal");
  }


  public static TrdZZCheckStatusEnum getByStatus(int status){
    for (TrdZZCheckStatusEnum enumItem : TrdZZCheckStatusEnum.values()) {
      if (enumItem.getStatus() == status) {
        return enumItem;
      }
    }
    throw new IllegalArgumentException("input status:"+status+" is illeagal");
  }

}
