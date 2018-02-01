package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Id;
import org.junit.FixMethodOrder;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The persistent class for the test1.mongoUiTrdZZInfo database table.
 */
@Document(collection = "mongoUiTrdZZInfo")
public class MongoUiTrdZZinfo implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	private String id;

	@Field(value = "operations")
	private Integer operations;

	@Field(value = "userProdId")
	private Integer userProdId;

	@Field(value = "fundCode")
	private String fundCode;

	@Field(value = "applyDate")
	private String applyDate;

	@Field(value = "melonMethod")
	private String melonMethod;

	@Field(value = "tradeStatus")
	private Integer tradeStatus;

	@Field(value = "userId")
	private Integer userId;

	@Field(value = "confirmDate")
	private String confirmDate;

	@Field(value = "bankAcco")
	private String bankAcco;

	@Field(value = "bankName")
	private String bankName;

	@Field(value = "businFlagStr")
	private String businFlagStr;

	@Field(value = "outSideOrderNo")
	private String outSideOrderNo;

	@Field(value = "tradeType")
	private Integer tradeType;

	@Field(value = "tradeConfirmShare")
	private BigDecimal tradeConfirmShare;

	@Field(value = "tradeConfirmSum")
	private BigDecimal tradeConfirmSum;

	@Field(value = "tradeTargetShare")
	private BigDecimal tradeTargetShare;

	@Field(value = "tradeTargetSum")
	private BigDecimal tradeTargetSum;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOperations() {
		return operations;
	}

	public void setOperations(Integer operations) {
		this.operations = operations;
	}

	public Integer getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(Integer userProdId) {
		this.userProdId = userProdId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getMelonMethod() {
		return melonMethod;
	}

	public void setMelonMethod(String melonMethod) {
		this.melonMethod = melonMethod;
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getBankAcco() {
		return bankAcco;
	}

	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBusinFlagStr() {
		return businFlagStr;
	}

	public void setBusinFlagStr(String businFlagStr) {
		this.businFlagStr = businFlagStr;
	}

	public String getOutSideOrderNo() {
		return outSideOrderNo;
	}

	public void setOutSideOrderNo(String outSideOrderNo) {
		this.outSideOrderNo = outSideOrderNo;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getTradeConfirmShare() {
		return tradeConfirmShare;
	}

	public void setTradeConfirmShare(BigDecimal tradeConfirmShare) {
		this.tradeConfirmShare = tradeConfirmShare;
	}

	public BigDecimal getTradeConfirmSum() {
		return tradeConfirmSum;
	}

	public void setTradeConfirmSum(BigDecimal tradeConfirmSum) {
		this.tradeConfirmSum = tradeConfirmSum;
	}

	public BigDecimal getTradeTargetShare() {
		return tradeTargetShare;
	}

	public void setTradeTargetShare(BigDecimal tradeTargetShare) {
		this.tradeTargetShare = tradeTargetShare;
	}

	public BigDecimal getTradeTargetSum() {
		return tradeTargetSum;
	}

	public void setTradeTargetSum(BigDecimal tradeTargetSum) {
		this.tradeTargetSum = tradeTargetSum;
	}

	@Override
	public String toString() {
		return "MongoUiTrdZZinfo{" +
				"id='" + id + '\'' +
				", operations=" + operations +
				", userProdId=" + userProdId +
				", fundCode='" + fundCode + '\'' +
				", applyDate='" + applyDate + '\'' +
				", melonMethod='" + melonMethod + '\'' +
				", tradeStatus=" + tradeStatus +
				", userId=" + userId +
				", confirmDate='" + confirmDate + '\'' +
				", bankAcco='" + bankAcco + '\'' +
				", bankName='" + bankName + '\'' +
				", businFlagStr='" + businFlagStr + '\'' +
				", outSideOrderNo='" + outSideOrderNo + '\'' +
				", tradeType=" + tradeType +
				", tradeConfirmShare=" + tradeConfirmShare +
				", tradeConfirmSum=" + tradeConfirmSum +
				", tradeTargetShare=" + tradeTargetShare +
				", tradeTargetSum=" + tradeTargetSum +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MongoUiTrdZZinfo that = (MongoUiTrdZZinfo) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(operations, that.operations) &&
				Objects.equals(userProdId, that.userProdId) &&
				Objects.equals(fundCode, that.fundCode) &&
				Objects.equals(applyDate, that.applyDate) &&
				Objects.equals(melonMethod, that.melonMethod) &&
				Objects.equals(tradeStatus, that.tradeStatus) &&
				Objects.equals(userId, that.userId) &&
				Objects.equals(confirmDate, that.confirmDate) &&
				Objects.equals(bankAcco, that.bankAcco) &&
				Objects.equals(bankName, that.bankName) &&
				Objects.equals(businFlagStr, that.businFlagStr) &&
				Objects.equals(outSideOrderNo, that.outSideOrderNo) &&
				Objects.equals(tradeType, that.tradeType) &&
				Objects.equals(tradeConfirmShare, that.tradeConfirmShare) &&
				Objects.equals(tradeConfirmSum, that.tradeConfirmSum) &&
				Objects.equals(tradeTargetShare, that.tradeTargetShare) &&
				Objects.equals(tradeTargetSum, that.tradeTargetSum);
	}

	@Override
	public int hashCode() {

		return Objects
				.hash(id, operations, userProdId, fundCode, applyDate, melonMethod, tradeStatus, userId,
						confirmDate, bankAcco, bankName, businFlagStr, outSideOrderNo, tradeType,
						tradeConfirmShare, tradeConfirmSum, tradeTargetShare, tradeTargetSum);
	}
}