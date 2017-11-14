package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the ui_bankcard database table.
 * 
 */
@Entity
@Table(name="ui_bankcard")
@NamedQuery(name="UiBankcard.findAll", query="SELECT u FROM UiBankcard u")
public class UiBankcard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UI_BANKCARD_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_BANKCARD_ID_GENERATOR")
	private String id;

	@Column(name="bank_name")
	private String bankName;

	@Column(name="card_number")
	private String cardNumber;

	private String cellphone;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private BigInteger createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name="expire_date")
	private Date expireDate;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private BigInteger lastModifiedDate;

	@Column(name="user_id")
	private BigInteger userId;

	@Column(name="user_name")
	private String userName;

	@Column(name="user_pid")
	private String userPid;

	public UiBankcard() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(BigInteger createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public BigInteger getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(BigInteger lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public BigInteger getUserId() {
		return this.userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPid() {
		return this.userPid;
	}

	public void setUserPid(String userPid) {
		this.userPid = userPid;
	}

}