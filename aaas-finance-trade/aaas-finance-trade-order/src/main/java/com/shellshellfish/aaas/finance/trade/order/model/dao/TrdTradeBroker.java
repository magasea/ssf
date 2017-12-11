package com.shellshellfish.aaas.finance.trade.order.model.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the trd_trade_broker database table.
 * 
 */
@Entity
@Table(name="trd_trade_broker")
@NamedQuery(name="TrdTradeBroker.findAll", query="SELECT t FROM TrdTradeBroker t")
public class TrdTradeBroker implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="create_by")
	private BigInteger createBy;

	@Column(name="create_date")
	private BigInteger createDate;

	private int priority;

	@Column(name="trade_broker_id")
	private int tradeBrokerId;

	@Column(name="trade_broker_name")
	private String tradeBrokerName;

	@Column(name="update_by")
	private BigInteger updateBy;

	@Column(name="update_date")
	private BigInteger updateDate;

	public TrdTradeBroker() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getTradeBrokerId() {
		return this.tradeBrokerId;
	}

	public void setTradeBrokerId(int tradeBrokerId) {
		this.tradeBrokerId = tradeBrokerId;
	}

	public String getTradeBrokerName() {
		return this.tradeBrokerName;
	}

	public void setTradeBrokerName(String tradeBrokerName) {
		this.tradeBrokerName = tradeBrokerName;
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

}