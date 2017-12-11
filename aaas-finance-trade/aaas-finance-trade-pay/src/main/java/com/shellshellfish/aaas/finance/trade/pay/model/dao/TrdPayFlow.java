package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import java.io.Serializable;
import javax.persistence.*;



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
	private Long id;

	@Column(name="apply_serial")
	private String applySerial;

	@Column(name="apply_sum")
	private Long applySum;

	@Column(name="bank_card_num")
	private String bankCardNum;

	@Column(name="create_by")
	private Long createBy;

	@Column(name="create_date")
	private Long createDate;

	@Column(name="order_id")
	private String orderId;

	@Column(name="outside_orderno")
	private String outsideOrderno;

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	@Column(name="fund_code")
	private String fundCode;

	@Column(name="pay_date")
	private Long payDate;

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
	private Long updateBy;

	@Column(name="update_date")
	private Long updateDate;

	@Column(name="user_id")
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