package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 一月 - 09
 */

public enum ZZKKStatusEnum {
  NOTSTART(0,"未发起"), KKFAILED(1, "扣款失败"), KKSUCCESS(2, "扣款成功 "), WAITCONFIRM(3, "已发起，待确认");
  private int status;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private String comment;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }




  ZZKKStatusEnum(int status, String comment){
    this.status = status;
    this.comment = comment;
  }



  public static ZZKKStatusEnum getByStatus(int status){

    for (ZZKKStatusEnum enumItem : ZZKKStatusEnum.values()) {
      if (enumItem.getStatus() == status) {
        return enumItem;
      }
    }
    throw new IllegalArgumentException("status:" + status + " is not suitable for ZZKKStatusEnum");
  }


  public static ZZKKStatusEnum getByComment(String comment){
    for (ZZKKStatusEnum enumItem : ZZKKStatusEnum.values()) {
      if (enumItem.getComment().equals(comment)) {
        return enumItem;
      }
    }
    throw new IllegalArgumentException("status:" + comment + " is not suitable for ZZKKStatusEnum");
  }
}
