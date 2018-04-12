package com.shellshellfish.aaas.common.enums;

public enum CombinedStatusEnum {
	WAITCONFIRM(0, "确认中"),CONFIRMED(2, "确认成功"),CONFIRMEDFAILED(-1, "确认失败"),SOMECONFIRMED(-1, "部分确认成功");

	private int operation;
	String comment;


	public String getComment() {
		return comment;
	}


	public int getOperation() {
		return operation;
	}


	CombinedStatusEnum(int operation, String comment) {
		this.operation = operation;
		this.comment = comment;
	}

  public static String getComment(int operation){

    for (CombinedStatusEnum enumItem : CombinedStatusEnum.values()) {
      if (enumItem.getOperation() == operation) {
        return enumItem.getComment();
      }
    }
    throw new IllegalArgumentException("input operation:"+operation+" is illeagal");
  }

}
