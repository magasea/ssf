package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Date;


/**
 * The persistent class for the ui_user database table.
 * 
 */
@Entity
@Table(name="ui_user")
@NamedQuery(name="UiUser.findAll", query="SELECT u FROM UiUser u")
public class UiUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

	private byte activated;

	@Column(name="birth_age")
	private Date birthAge;

	@Column(name="cell_phone")
	private String cellPhone;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	@Column(name="last_reset_date")
	private Date lastResetDate;

	private String occupation;

	@Column(name="password_hash")
	private String passwordHash;

	private String uuid;

	public UiUser() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte getActivated() {
		return this.activated;
	}

	public void setActivated(byte activated) {
		this.activated = activated;
	}

	public Date getBirthAge() {
		return this.birthAge;
	}

	public void setBirthAge(Date birthAge) {
		this.birthAge = birthAge;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastResetDate() {
		return this.lastResetDate;
	}

	public void setLastResetDate(Date lastResetDate) {
		this.lastResetDate = lastResetDate;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}