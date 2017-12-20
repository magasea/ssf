package com.shellshellfish.aaas.userinfo.model.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "ui_sysmsg")
public class UiSysMsg {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String source;

	private String content;

	private String createdBy;

	private Long createdDate;

	private String lastModifiedBy;

	private Long lastModifiedDate;

	private String date;

	private String title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "UiSysMsg{" + ", content='" + content + '\'' + ", source='" + source + '\'' + ", createdBy='" + createdBy
				+ '\'' + ", createdDate=" + createdDate + ", lastModifiedBy='" + lastModifiedBy + '\''
				+ ", lastModifiedDate=" + lastModifiedDate + ", date=" + date + ", title='" + title + '\'' + '}';
	}
}
