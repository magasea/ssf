package com.shellshellfish.aaas.finance.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2018- 三月 - 15
 */



@Document(collection = "fn_upc_detail")
public class UserProdChgDetail {
  @Id
  String id;

  Long userProdId;
  Long prodId;

  Long modifySeq;

  String code;
  String fundName;
  String fundType;

  Long percentBefore;
  Long percentAfter;
  Long modifyTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public Long getModifySeq() {
    return modifySeq;
  }

  public void setModifySeq(Long modifySeq) {
    this.modifySeq = modifySeq;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public String getFundType() {
    return fundType;
  }

  public void setFundType(String fundType) {
    this.fundType = fundType;
  }

  public Long getPercentBefore() {
    return percentBefore;
  }

  public void setPercentBefore(Long percentBefore) {
    this.percentBefore = percentBefore;
  }

  public Long getPercentAfter() {
    return percentAfter;
  }

  public void setPercentAfter(Long percentAfter) {
    this.percentAfter = percentAfter;
  }

  public Long getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Long modifyTime) {
    this.modifyTime = modifyTime;
  }
}
