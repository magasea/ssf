package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import javax.persistence.Id;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 六月 - 08
 */
@Document(collection = "ui_calc_base")
public class MongoCaculateBase implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Field( value = "trade_type")
  private int tradeType = Integer.MIN_VALUE;

  @Field( value = "user_prod_id")
  @Indexed(direction = IndexDirection.DESCENDING)
  private Long userProdId;

  @Field( value = "apply_date_str")
  private String applyDateStr;

  //注意精度达到百万分之一 比如1.000001 -> 1000001
  @Field( value = "apply_date_navadj")
  private Long applyDateNavadj;

  //注意这里货币基金用confirmed_share/navadj
  //注意这里普通基金用confirmed_share
  @Field( value = "calculated_share")
  private Long calculatedShare = Long.MIN_VALUE;


  @Field( value = "fund_code")
  @Indexed(direction = IndexDirection.ASCENDING)
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


  @Field(value = "outside_order_id")
  @Indexed(unique = true)
  private String outsideOrderId;


  @Field( value = "created_by")
  private Long createdBy;

  @Field( value = "created_date")
  private Long createdDate;

  @Field( value = "last_modified_by")
  private Long lastModifiedBy;

  @Field( value = "last_modified_date")
  private Long lastModifiedDate;

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

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  public String getApplyDateStr() {
    return applyDateStr;
  }

  public void setApplyDateStr(String applyDateStr) {
    this.applyDateStr = applyDateStr;
  }

  public Long getApplyDateNavadj() {
    return applyDateNavadj;
  }

  public void setApplyDateNavadj(Long applyDateNavadj) {
    this.applyDateNavadj = applyDateNavadj;
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

  public String getOutsideOrderId() {
    return outsideOrderId;
  }

  public void setOutsideOrderId(String outsideOrderId) {
    this.outsideOrderId = outsideOrderId;
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

  public Long getCalculatedShare() {
    return calculatedShare;
  }

  public void setCalculatedShare(Long calculatedShare) {
    this.calculatedShare = calculatedShare;
  }
}
