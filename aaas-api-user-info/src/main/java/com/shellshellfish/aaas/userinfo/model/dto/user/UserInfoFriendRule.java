package com.shellshellfish.aaas.userinfo.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoFriendRule {
	private Long id;
	private Long bankId;
	private String content;
	private String bankName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
