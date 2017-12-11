package com.shellshellfish.aaas.finance.trade.order.model.dao;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the trd_order database table.
 * 
 */
@Entity
@Table(name="trd_order")
@NamedQuery(name="TrdOrder.findAll", query="SELECT t FROM TrdOrder t")
public class TrdOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="bank_card_num")
	private String bankCardNum;

	@Column(name="create_by")
	private Long createBy;

	@Column(name="create_date")
	private Long createDate;

	@Column(name="order_date")
	private Long orderDate;

	@Column(name="order_id")
	private String orderId;

	@Column(name="order_status")
	private int orderStatus;

	@Column(name="order_type")
	private int orderType;

	@Column(name="pay_amount")
	private Long payAmount;

	@Column(name="pay_fee")
	private Long payFee;

	@Column(name="prod_code")
	private String prodCode;

	@Column(name="prod_id")
	private Long prodId;

	@Column(name="update_by")
	private Long updateBy;

	@Column(name="update_date")
	private Long updateDate;

	@Column(name="user_id")
	private Long userId;

	public TrdOrder() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getOrderType() {
		return this.orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
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

	public String getProdCode() {
		return this.prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public Long getProdId() {
		return this.prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
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