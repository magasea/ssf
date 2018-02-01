package com.shellshellfish.aaas.common.enums;

public enum TrdOrderStatusEnum {
	WAITPAY(0, "等待支付"), PAYWAITCONFIRM(1, "支付等待确认"), CONFIRMED(2, "已经确认"), WAITSELL(3, "等待赎回"),
	SELLWAITCONFIRM(4, "赎回等待确认"), PARTIALCONFIRMED(5, "部分确认"), CONVERTWAITCONFIRM(6, "转换等待确认"),
	FAILED(-1, "失败"), CANCEL(-2, "取消"), WAITCANCEL(-3, "待取消");
	private int status;
	private String comment;


	public int getStatus() {
		return status;
	}


	public String getComment() {
		return comment;
	}


	TrdOrderStatusEnum(int status, String comment) {
		this.status = status;
		this.comment = comment;
	}


	public static boolean isEntirelyConfirmed(Integer status) {
		if (status == null) {
			return false;
		}

		if (status == CONFIRMED.getStatus() || status == WAITSELL.getStatus()
				|| status == SELLWAITCONFIRM.getStatus() || status == CONVERTWAITCONFIRM.getStatus()) {
			return true;
		}

		return false;

	}
}
