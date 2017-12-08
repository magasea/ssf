package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the trd_pay_flow database table.
 * 
 */
@Entity
@Table(name="trd_pay_flow")
@NamedQuery(name="TrdPayFlow.findAll", query="SELECT t FROM TrdPayFlow t")
public class TrdPayFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRD_PAY_FLOW_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRD_PAY_FLOW_ID_GENERATOR")
	private String id;

	@Column(name="apply_serial")
	private String applySerial;

	@Column(name="apply_sum")
	private BigInteger applySum;

	@Column(name="bank_card_num")
	private String bankCardNum;

	@Column(name="create_by")
	private BigInteger createBy;

	@Column(name="create_date")
	private BigInteger createDate;

	@Column(name="order_id")
	private String orderId;

	@Column(name="outside_orderno")
	private String outsideOrderno;

	@Column(name="pay_date")
	private BigInteger payDate;

	@Column(name="pay_status")
	private int payStatus;

	@Column(name="pay_type")
	private int payType;

	@Column(name="prod_code")
	private String prodCode;

	@Column(name="trade_acco")
	private String tradeAcco;

	@Column(name="trade_broke_id")
	private String tradeBrokeId;

	@Column(name="update_by")
	private BigInteger updateBy;

	@Column(name="update_date")
	private BigInteger updateDate;

	@Column(name="user_id")
	private BigInteger userId;

	public TrdPayFlow() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplySerial() {
		return this.applySerial;
	}

	public void setApplySerial(String applySerial) {
		this.applySerial = applySerial;
	}

	public BigInteger getApplySum() {
		return this.applySum;
	}

	public void setApplySum(BigInteger applySum) {
		this.applySum = applySum;
	}

	public String getBankCardNum() {
		return this.bankCardNum;
	}

	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}

	public BigInteger getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(BigInteger createBy) {
		this.createBy = createBy;
	}

	public BigInteger getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(BigInteger createDate) {
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

	public BigInteger getPayDate() {
		return this.payDate;
	}

	public void setPayDate(BigInteger payDate) {
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

	public BigInteger getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(BigInteger updateBy) {
		this.updateBy = updateBy;
	}

	public BigInteger getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(BigInteger updateDate) {
		this.updateDate = updateDate;
	}

	public BigInteger getUserId() {
		return this.userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

}