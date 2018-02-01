package com.shellshellfish.aaas.common.enums;

public enum TrdOrderOpTypeEnum {
	BUY(1, "购买"), REDEEM(2, "赎回"), DIVIDINVEST(3, "分红再投"), DIVIDMONEY(4, "现金分红"), CANCEL(5, "取消交易"),
	PREORDER(6, "预购买"), FUNDCONVERT(7, "基金转换"), UNDEFINED(-1, "未定义");

	private int operation;
	String comment;


	public String getComment() {
		return comment;
	}


	public int getOperation() {
		return operation;
	}


	TrdOrderOpTypeEnum(int operation, String comment) {
		this.operation = operation;
		this.comment = comment;
	}

}
