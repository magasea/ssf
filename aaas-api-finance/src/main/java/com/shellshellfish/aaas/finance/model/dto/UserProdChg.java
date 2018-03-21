package com.shellshellfish.aaas.finance.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@Document(collection = "fn_upc")
public class UserProdChg {
  @Id
  String id;

  Integer groupId;
  Long prodId;

  Integer modifySeq;

  Integer userProdId;

  Long modifyTime;
  String modifyComment;
  Integer modifyType;
  String modifyTypeComment;
  Long createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public Long getProdId() {
    return prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public Integer getModifySeq() {
    return modifySeq;
  }

  public void setModifySeq(Integer modifySeq) {
    this.modifySeq = modifySeq;
  }

  public Integer getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Integer userProdId) {
    this.userProdId = userProdId;
  }

  public Long getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Long modifyTime) {
    this.modifyTime = modifyTime;
  }

  public String getModifyComment() {
    return modifyComment;
  }

  public void setModifyComment(String modifyComment) {
    this.modifyComment = modifyComment;
  }

  public Integer getModifyType() {
    return modifyType;
  }

  public void setModifyType(Integer modifyType) {
    this.modifyType = modifyType;
  }

  public String getModifyTypeComment() {
    return modifyTypeComment;
  }

  public void setModifyTypeComment(String modifyTypeComment) {
    this.modifyTypeComment = modifyTypeComment;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }
}
