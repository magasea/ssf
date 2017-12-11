package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;


/**
 * The persistent class for the trd_pay_flow database table.
 * 
 */

public class TrdPayFlow implements Serializable {
	private static final long serialVersionUID = 1L;


	private Long id;

	
	private String applySerial;

	
	private Long applySum;

	
	private String bankCardNum;

	
	private Long createBy;

	
	private Long createDate;

	
	private String orderId;

	
	private String outsideOrderno;

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	
	private String fundCode;

	
	private Long payDate;

	
	private int payStatus;

	
	private int payType;

	
	private String prodCode;

	
	private String tradeAcco;

	
	private String tradeBrokeId;

	
	private Long updateBy;

	
	private Long updateDate;

	
	private Long userId;

	public TrdPayFlow() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplySerial() {
		return this.applySerial;
	}

	public void setApplySerial(String applySerial) {
		this.applySerial = applySerial;
	}

	public Long getApplySum() {
		return this.applySum;
	}

	public void setApplySum(Long applySum) {
		this.applySum = applySum;
	}

	public String getBankCardNum() {
		return this.bankCardNum;
	}

	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}

	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOutsideOrderno() {
		return this.outsideOrderno;
	}

	public void setOutsideOrderno(String outsideOrderno) {
		this.outsideOrderno = outsideOrderno;
	}

	public Long getPayDate() {
		return this.payDate;
	}

	public void setPayDate(Long payDate) {
		this.payDate = payDate;
	}

	public int getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getPayType() {
		return this.payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getProdCode() {
		return this.prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getTradeAcco() {
		return this.tradeAcco;
	}

	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}

	public String getTradeBrokeId() {
		return this.tradeBrokeId;
	}

	public void setTradeBrokeId(String tradeBrokeId) {
		this.tradeBrokeId = tradeBrokeId;
	}

	public Long getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}