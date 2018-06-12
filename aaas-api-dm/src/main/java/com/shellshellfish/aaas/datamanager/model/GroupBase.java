package com.shellshellfish.aaas.datamanager.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "group_base")
public class GroupBase {

	@Id
	private String id;
	@Field("groupId")
	private Long groupId; //基金代码
	@Field("baseName")
	private String baseName; //基金基准

	@Field("baseLine")
	private String baseLine; //基金基准


	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String name) {
		this.baseName = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseLine() {
		return baseLine;
	}

	public void setBaseLine(String baseLine) {
		this.baseLine = baseLine;
	}

	@Override
	public String toString() {
		return "GroupBase{" +
				"id='" + id + '\'' +
				", groupId=" + groupId +
				", baseName='" + baseName + '\'' +
				", baseLine='" + baseLine + '\'' +
				'}';
	}
}
