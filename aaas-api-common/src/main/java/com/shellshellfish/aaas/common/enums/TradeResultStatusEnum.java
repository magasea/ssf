package com.shellshellfish.aaas.common.enums;

public enum TradeResultStatusEnum {
	SUCCESS(0, "全部申请成功"),FAIL(-1, "全部申请失败"),SOMESUCCESS(-2, "部分申请成功");

	private int operation;
	String comment;


	public String getComment() {
		return comment;
	}


	public int getOperation() {
		return operation;
	}


	TradeResultStatusEnum(int operation, String comment) {
		this.operation = operation;
		this.comment = comment;
	}

  public static String getComment(int operation){

    for (TradeResultStatusEnum enumItem : TradeResultStatusEnum.values()) {
      if (enumItem.getOperation() == operation) {
        return enumItem.getComment();
      }
    }
    throw new IllegalArgumentException("input operation:"+operation+" is illeagal");
  }

}
