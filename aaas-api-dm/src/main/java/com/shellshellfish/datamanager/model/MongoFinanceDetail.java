package com.shellshellfish.datamanager.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ui_finance_detail")
public class MongoFinanceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field(value = "date")
	private String date;
	
	@Field(value = "groupId")
	private String groupId;
	
	@Field(value = "subGroupId")
	private String subGroupId;

	@Field(value = "head")
	private Head head;

	@Field(value = "jonResult")
	private Object result;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}
}