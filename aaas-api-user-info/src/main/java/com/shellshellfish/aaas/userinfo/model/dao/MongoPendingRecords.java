package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 六月 - 04
 */
@Document(collection = "ui_pending_records")
public class MongoPendingRecords implements Serializable {
  private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field( value = "trade_type")
    private int tradeType = Integer.MIN_VALUE;

    @Field( value = "created_by")
    private Long createdBy;

    @Field( value = "created_date")
    private Long createdDate;

    @Field( value = "last_modified_by")
    private Long lastModifiedBy;

    @Field( value = "last_modified_date")
    private Long lastModifiedDate;


    @Field( value = "user_prod_id")
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long userProdId;


    @Field( value = "apply_date")
    private Long applyDate;

  @Field( value = "apply_date_str")
  private String applyDateStr;

  //注意精度达到百万分之一 比如1.000001 -> 1000001
  @Field( value = "apply_date_navadj")
  private Long applyDateNavadj;

    @Field( value = "process_status")
    private int processStatus;

  @Field( value = "trade_status")
  private int tradeStatus;

    @Field( value = "user_id")
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long userId;

    @Field( value = "fund_code")
    private String fundCode;

    @Field( value = "trade_confirm_share")
    private Long tradeConfirmShare;

    @Field( value = "trade_confirm_sum")
    private Long tradeConfirmSum;

    @Field( value = "trade_target_share")
    private Long tradeTargetShare;

    @Field( value = "trade_target_sum")
    private Long tradeTargetSum;

    @Field(value = "oemId")
    private  Integer oemId;

    @Field( value = "apply_serial")
    private String applySerial;

    @Field(value = "order_id")
    @Indexed()
    private String orderId;

    @Field(value = "outside_order_id")
    private String outsideOrderId;

    @Field(value="prod_id")
    private Long prodId;

    @Field(value="group_id")
    private Long groupId;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getTradeType() {
    return tradeType;
  }

  public void setTradeType(int tradeType) {
    this.tradeType = tradeType;
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

  public Long getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(Long lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  public Long getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(Long applyDate) {
    this.applyDate = applyDate;
  }

  public Long getApplyDateNavadj() {
    return applyDateNavadj;
  }

  public void setApplyDateNavadj(Long applyDateNavadj) {
    this.applyDateNavadj = applyDateNavadj;
  }

  public int getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(int processStatus) {
    this.processStatus = processStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
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

  public Integer getOemId() {
    return oemId;
  }

  public void setOemId(Integer oemId) {
    this.oemId = oemId;
  }

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getApplyDateStr() {
    return applyDateStr;
  }

  public void setApplyDateStr(String applyDateStr) {
    this.applyDateStr = applyDateStr;
  }

  public int getTradeStatus() {
    return tradeStatus;
  }

  public void setTradeStatus(int tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public String getOutsideOrderId() {
    return outsideOrderId;
  }

  public void setOutsideOrderId(String outsideOrderId) {
    this.outsideOrderId = outsideOrderId;
  }

  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }
}
