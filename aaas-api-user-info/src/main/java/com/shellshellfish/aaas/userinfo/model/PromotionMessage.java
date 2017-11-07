package com.shellshellfish.aaas.userinfo.model;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.User;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the promotion_message database table.
 * 
 */
@Entity
@Table(name="promotion_message")
@NamedQuery(name="PromotionMessage.findAll", query="SELECT p FROM PromotionMessage p")
public class PromotionMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private String id;

	@Column(length=255)
	private String content;

	@Column(name="created_by", length=50)
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="last_modified_by", length=50)
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	@Column(length=45)
	private String title;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	public PromotionMessage() {
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

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}