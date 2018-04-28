package com.shellshellfish.aaas.common.enums;

public enum TrdOrderStatusEnum {
	WAITPAY(0, "等待支付"), PAYWAITCONFIRM(1, "已支付等待确认"), CONFIRMED(2, "购买已经确认"), WAITSELL(3, "等待赎回"),
	SELLWAITCONFIRM(4, "赎回等待确认"), PARTIALCONFIRMED(5, "部分确认"), CONVERTWAITCONFIRM(6, "转换等待确认"),
	SELLCONFIRMED(7, "赎回已经确认"), FAILED(-1, "购买失败"), CANCELED(-2, "已取消"), WAITCANCEL(-3, "待取消"),
	REDEEMFAILED(-4, "赎回失败"), NOTRDNEED(-5, "无需交易");

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private int status;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private String comment;

	TrdOrderStatusEnum(int status, String comment) {
		this.status = status;
		this.comment = comment;
	}


	public static String getComment(int status) {
		for (TrdOrderStatusEnum enumItem : TrdOrderStatusEnum.values()) {
			if (enumItem.getStatus() == status) {
				return enumItem.getComment();
			}
		}
		throw new IllegalArgumentException("input operation:" + status + " is illeagal");
	}

	public static TrdOrderStatusEnum getTrdOrderStatusEnum(int status) {
		for (TrdOrderStatusEnum enumItem : TrdOrderStatusEnum.values()) {
			if (enumItem.getStatus() == status) {
				return enumItem;
			}
		}
		throw new IllegalArgumentException("input operation:" + status + " is illeagal");
	}


	/**
	 * 交易处于等待过程中
	 */
	public static boolean isInWaiting(int status) {
		if (WAITPAY.status == status) {
			return true;
		}
		if (WAITSELL.status == status) {
			return true;
		}
		if (PAYWAITCONFIRM.status == status) {
			return true;
		}

		if (SELLWAITCONFIRM.status == status) {
			return true;
		}
		if (CONVERTWAITCONFIRM.status == status) {
			return true;
		}

		return false;
	}

	/**
	 * 交易失败
	 */
	public static boolean isFailed(int status) {
		if (FAILED.status == status) {
			return true;
		}
		if (REDEEMFAILED.status == status) {
			return true;
		}

		return false;
	}
}
