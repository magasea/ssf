package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The persistent class for the ui_trd_log database table.
 *
 */
@Document(collection = "ui_trdzzinfo")
public class MongoUiTrdZZInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field( value = "created_by")
	private String createdBy;

	@Field( value = "created_date")
	private Long createdDate;

	@Field( value = "last_modified_by")
	private String lastModifiedBy;

	@Field( value = "last_modified_date")
	private Long lastModifiedDate;

	@Field(value = "operations")
	private int operations;

	@Field( value = "user_prod_id")
	@Indexed(direction = IndexDirection.DESCENDING)
	private Long userProdId;

	@Field(value = "fund_code")
	private String fundCode;

	@Field(value = "fund_name")
	private String fundName;

	@Field( value = "apply_date")
	private String applyDate;

	@Field( value = "apply_serial")
	@Indexed(direction = IndexDirection.DESCENDING)
	private String applySerial;

	@Field( value = "melon_method")
	private String melonMethod;

	@Field( value = "trade_status")
	private int tradeStatus;

	@Field( value = "user_id")
	@Indexed(direction = IndexDirection.DESCENDING)
	private Long userId;

	@Field(value = "confirm_flag")
	private String confirmFlag;

	@Field(value = "confirm_stat")
	private String confirmStat;

	@Field(value = "ori_apply_date")
	private String oriApplyDate;

	@Field(value = "confirm_date")
	private String confirmDate;

	@Field(value = "bank_acco")
	private String bankAcco;

	@Field(value ="bank_name")
	private String bankName;

	@Field(value = "busin_flag_str")
	private String businFlagStr;

	@Field(value = "occur_bank_acco")
	private String occurBankAcco;

	@Field(value = "bank_serial")
	private String bankSerial;

	@Field(value = "outsideOrderNo")
	@Indexed(direction = IndexDirection.DESCENDING)
	private String outSideOrderNo;

	@Field( value = "trade_type")
	private int tradeType;

	@Field( value = "trade_confirm_share")
	private Long tradeConfirmShare;

	@Field( value = "trade_confirm_sum")
	private Long tradeConfirmSum;

	@Field( value = "trade_target_share")
	private Long tradeTargetShare;

	@Field( value = "trade_target_sum")
	private Long tradeTargetSum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getOperations() {
		return operations;
	}

	public void setOperations(int operations) {
		this.operations = operations;
	}

	public Long getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(Long userProdId) {
		this.userProdId = userProdId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplySerial() {
		return applySerial;
	}

	public void setApplySerial(String applySerial) {
		this.applySerial = applySerial;
	}

	public String getMelonMethod() {
		return melonMethod;
	}

	public void setMelonMethod(String melonMethod) {
		this.melonMethod = melonMethod;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	public String getConfirmStat() {
		return confirmStat;
	}

	public void setConfirmStat(String confirmStat) {
		this.confirmStat = confirmStat;
	}

	public String getOriApplyDate() {
		return oriApplyDate;
	}

	public void setOriApplyDate(String oriApplyDate) {
		this.oriApplyDate = oriApplyDate;
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

	public String getOccurBankAcco() {
		return occurBankAcco;
	}

	public void setOccurBankAcco(String occurBankAcco) {
		this.occurBankAcco = occurBankAcco;
	}

	public String getBankSerial() {
		return bankSerial;
	}

	public void setBankSerial(String bankSerial) {
		this.bankSerial = bankSerial;
	}

	public String getOutSideOrderNo() {
		return outSideOrderNo;
	}

	public void setOutSideOrderNo(String outSideOrderNo) {
		this.outSideOrderNo = outSideOrderNo;
	}

	public Long getTradeConfirmShare() {
		return tradeConfirmShare;
	}

	public void setTradeConfirmShare(Long tradeConfirmShare) {
		this.tradeConfirmShare = tradeConfirmShare;
	}

	public Long getTradeConfirmSum() {
		return tradeConfirmSum;
	}

	public void setTradeConfirmSum(Long tradeConfirmSum) {
		this.tradeConfirmSum = tradeConfirmSum;
	}

	public Long getTradeTargetShare() {
		return tradeTargetShare;
	}

	public void setTradeTargetShare(Long tradeTargetShare) {
		this.tradeTargetShare = tradeTargetShare;
	}

	public Long getTradeTargetSum() {
		return tradeTargetSum;
	}

	public void setTradeTargetSum(Long tradeTargetSum) {
		this.tradeTargetSum = tradeTargetSum;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}
}