package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import java.io.Serializable;
import javax.persistence.*;



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
	@SequenceGenerator(name="TRD_TRADE_BROKER_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRD_TRADE_BROKER_ID_GENERATOR")
	private Long id;

	@Column(name="create_by")
	private Long createBy;

	@Column(name="create_date")
	private Long createDate;

	private int priority;

	@Column(name="trade_broker_id")
	private int tradeBrokerId;

	@Column(name="trade_broker_name")
	private String tradeBrokerName;

	@Column(name="update_by")
	private Long updateBy;

	@Column(name="update_date")
	private Long updateDate;

	public TrdTradeBroker() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

}