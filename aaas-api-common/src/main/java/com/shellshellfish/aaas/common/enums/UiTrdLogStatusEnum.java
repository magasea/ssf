package com.shellshellfish.aaas.common.enums;

public enum UiTrdLogStatusEnum {
	WAITCONFIRM(0, "确认中"),CONFIRMED(2, "确认成功"),CONFIRMEDFAILED(-1, "确认失败");

	private int operation;
	String comment;


	public String getComment() {
		return comment;
	}


	public int getOperation() {
		return operation;
	}


	UiTrdLogStatusEnum(int operation, String comment) {
		this.operation = operation;
		this.comment = comment;
	}

  public static String getComment(int operation){

    for (UiTrdLogStatusEnum enumItem : UiTrdLogStatusEnum.values()) {
      if (enumItem.getOperation() == operation) {
        return enumItem.getComment();
      }
    }
    throw new IllegalArgumentException("input operation:"+operation+" is illeagal");
  }

}
