package com.shellshellfish.aaas.finance.trade.pay.model.dao;

import java.io.Serializable;
import javax.persistence.*;



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
	private Long id;

	@Column(name="create_by")
	private Long createBy;

	@Column(name="create_date")
	private Long createDate;

	private int priority;

	@Column(name="trade_acco")
	private String tradeAcco;

	@Column(name="trade_broker_id")
	private int tradeBrokerId;

	@Column(name="update_by")
	private Long updateBy;

	@Column(name="update_date")
	private Long updateDate;

	@Column(name="user_id")
	private Long userId;

	public TrdBrokerUser() {
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