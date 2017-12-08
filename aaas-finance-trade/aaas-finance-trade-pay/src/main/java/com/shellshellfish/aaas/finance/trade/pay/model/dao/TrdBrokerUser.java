package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the trd_broker_user database table.
 * 
 */
@Entity
@Table(name="trd_broker_user")
@NamedQuery(name="TrdBrokerUser.findAll", query="SELECT t FROM TrdBrokerUser t")
public class TrdBrokerUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRD_BROKER_USER_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRD_BROKER_USER_ID_GENERATOR")
	private String id;

	@Column(name="create_by")
	private BigInteger createBy;

	@Column(name="create_date")
	private BigInteger createDate;

	private int priority;

	@Column(name="trade_acco")
	private String tradeAcco;

	@Column(name="trade_broker_id")
	private int tradeBrokerId;

	@Column(name="update_by")
	private BigInteger updateBy;

	@Column(name="update_date")
	private BigInteger updateDate;

	@Column(name="user_id")
	private BigInteger userId;

	public TrdBrokerUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
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

	public String getTradeAcco() {
		return this.tradeAcco;
	}

	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}

	public int getTradeBrokerId() {
		return this.tradeBrokerId;
	}

	public void setTradeBrokerId(int tradeBrokerId) {
		this.tradeBrokerId = tradeBrokerId;
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