package com.shellshellfish.aaas.userinfo.model.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "getUiSysMsg")
public class UiSysMsg {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String source;

	private String content;

	private Long createdBy;

	private Long createdDate;

	private Long updateBy;

	private Long updateDate;

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateDatepdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
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
				+ '\'' + ", createdDate=" + createdDate + ", updateBy='" + updateBy + '\''
				+ ", lastModifiedDate=" + updateDate + ", date=" + date + ", title='" + title + '\'' + '}';
	}
}
