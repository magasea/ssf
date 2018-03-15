package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 每日收益存储表
 */
@Document(collection = "user_daily_income")
public class MongoUserDailyIncome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field(value = "userId")
	private Long userId;

	@Field(value = "userProdId")
	private Long userProdId;

	@Field(value = "dailyIncome")
	private BigDecimal dailyIncome;

	@Field(value = "accumulativeIncome")
	private BigDecimal accumulativeIncome;
	@Field(value = "createDate")
	private Long createDate;

	@Field(value = "createDateStr")
	private String createDateStr;

	@Field(value = "updateDate")
	private Long updateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(Long userProdId) {
		this.userProdId = userProdId;
	}

	public BigDecimal getDailyIncome() {
		return dailyIncome;
	}

	public void setDailyIncome(BigDecimal dailyIncome) {
		this.dailyIncome = dailyIncome;
	}

	public BigDecimal getAccumulativeIncome() {
		return accumulativeIncome;
	}

	public void setAccumulativeIncome(BigDecimal accumulativeIncome) {
		this.accumulativeIncome = accumulativeIncome;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "MongoUserDailyIncome{" +
				", userId=" + userId +
				", userProdId=" + userProdId +
				", dailyIncome=" + dailyIncome +
				", accumulativeIncome=" + accumulativeIncome +
				", createDate=" + createDate +
				", createDateStr='" + createDateStr + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}