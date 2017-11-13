package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the ui_agmt database table.
 * 
 */
@Entity
@Table(name="ui_agmt")
@NamedQuery(name="UiAgmt.findAll", query="SELECT u FROM UiAgmt u")
public class UiAgmt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UI_AGMT_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_AGMT_ID_GENERATOR")
	private String id;

	private String content;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private BigInteger createdDate;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private BigInteger lastModifiedDate;

	private String uuid;

	public UiAgmt() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
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

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}