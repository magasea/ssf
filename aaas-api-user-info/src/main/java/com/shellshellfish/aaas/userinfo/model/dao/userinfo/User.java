package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import com.shellshellfish.aaas.userinfo.model.BankCard;
import com.shellshellfish.aaas.userinfo.model.PromotionMessage;
import com.shellshellfish.aaas.userinfo.model.UserAsset;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false)
	private byte activated;

	@Column(name="birth_age", length=50)
	private String birthAge;

	@Column(name="cell_phone", nullable=false, length=50)
	private String cellPhone;



	@Column(name="uuid", nullable=false, length=50)
	private String uuid;

	@Column(name="created_by", nullable=false, length=50)
	private String createdBy;

	@Column(name="created_date", nullable=false)
	private Timestamp createdDate;

	@Column(name="last_modified_by", length=50)
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="last_reset_date")
	private Timestamp lastResetDate;

	@Column(length=50)
	private String occupation;

	@Column(name="password_hash", length=60)
	private String passwordHash;

	//bi-directional many-to-one association to BankCard
	@OneToMany(mappedBy="user")
	private List<BankCard> bankCards;

	//bi-directional many-to-one association to PromotionMessage
	@OneToMany(mappedBy="user")
	private List<PromotionMessage> promotionMessages;

	//bi-directional many-to-one association to UserAsset
	@OneToMany(mappedBy="user")
	private List<UserAsset> userAssets;

	public User() {
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

	public String getBirthAge() {
		return this.birthAge;
	}

	public void setBirthAge(String birthAge) {
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

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Timestamp getLastResetDate() {
		return this.lastResetDate;
	}

	public void setLastResetDate(Timestamp lastResetDate) {
		this.lastResetDate = lastResetDate;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



}