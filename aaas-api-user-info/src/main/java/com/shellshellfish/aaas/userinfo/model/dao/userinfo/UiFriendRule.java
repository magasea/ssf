package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the ui_friend_rule database table.
 * 
 */
@Entity
@Table(name="ui_friend_rule")
@NamedQuery(name="UiFriendRule.findAll", query="SELECT u FROM UiFriendRule u")
public class UiFriendRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UI_FRIEND_RULE_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_FRIEND_RULE_ID_GENERATOR")
	private String id;

	@Column(name="bank_id")
	private BigInteger bankId;

	@Column(name="bank_name")
	private BigInteger bankName;

	private int content;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private BigInteger createdDate;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private BigInteger lastModifiedDate;

	public UiFriendRule() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getBankId() {
		return this.bankId;
	}

	public void setBankId(BigInteger bankId) {
		this.bankId = bankId;
	}

	public BigInteger getBankName() {
		return this.bankName;
	}

	public void setBankName(BigInteger bankName) {
		this.bankName = bankName;
	}

	public int getContent() {
		return this.content;
	}

	public void setContent(int content) {
		this.content = content;
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

}