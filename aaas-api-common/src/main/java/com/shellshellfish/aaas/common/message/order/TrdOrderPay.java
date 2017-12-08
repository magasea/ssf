package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;


/**
 * The persistent class for the trd_order_detail database table.
 * 
 */

public class TrdOrderPay implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	
	private Long boughtDate;

	

	
	private String fundCode;

	
	private Long fundQuantity;

	
	private String orderId;

	
	private Long prodId;

	private Long quantity;

	
	private String tradeApplySerial;

	
	private int tradeType;



	
	private Long userId;

	public TrdOrderPay() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getBoughtDate() {
		return this.boughtDate;
	}

	public void setBoughtDate(Long boughtDate) {
		this.boughtDate = boughtDate;
	}


	public String getFundCode() {
		return this.fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Long getFundQuantity() {
		return this.fundQuantity;
	}

	public void setFundQuantity(Long fundQuantity) {
		this.fundQuantity = fundQuantity;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getProdId() {
		return this.prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	public Long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getTradeApplySerial() {
		return this.tradeApplySerial;
	}

	public void setTradeApplySerial(String tradeApplySerial) {
		this.tradeApplySerial = tradeApplySerial;
	}

	public int getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}