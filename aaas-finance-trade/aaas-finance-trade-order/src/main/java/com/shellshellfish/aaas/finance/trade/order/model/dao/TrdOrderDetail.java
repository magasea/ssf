package com.shellshellfish.aaas.finance.trade.order.model.dao;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the trd_order_detail database table.
 * 
 */
@Entity
@Table(name="trd_order_detail")
@NamedQuery(name="TrdOrderDetail.findAll", query="SELECT t FROM TrdOrderDetail t")
public class TrdOrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="bought_date")
	private Long boughtDate;

	@Column(name="create_by")
	private Long createBy;

	@Column(name="create_date")
	private Long createDate;

	@Column(name="fund_code")
	private String fundCode;

	@Column(name="fund_quantity")
	private Long fundQuantity;

	@Column(name="order_detail_status")
	private int orderDetailStatus;

	@Column(name="order_id")
	private String orderId;

	@Column(name="pay_amount")
	private Long payAmount;

	@Column(name="pay_fee")
	private Long payFee;

	@Column(name="prod_id")
	private Long prodId;

	@Column(name="trade_apply_serial")
	private String tradeApplySerial;

	@Column(name="trade_type")
	private int tradeType;

	@Column(name="update_by")
	private Long updateBy;

	@Column(name="update_date")
	private Long updateDate;

	@Column(name="user_id")
	private Long userId;

	public TrdOrderDetail() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBoughtDate() {
		return this.boughtDate;
	}

	public void setBoughtDate(Long boughtDate) {
		this.boughtDate = boughtDate;
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

	public int getOrderDetailStatus() {
		return this.orderDetailStatus;
	}

	public void setOrderDetailStatus(int orderDetailStatus) {
		this.orderDetailStatus = orderDetailStatus;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getPayAmount() {
		return this.payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public Long getPayFee() {
		return this.payFee;
	}

	public void setPayFee(Long payFee) {
		this.payFee = payFee;
	}

	public Long getProdId() {
		return this.prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
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