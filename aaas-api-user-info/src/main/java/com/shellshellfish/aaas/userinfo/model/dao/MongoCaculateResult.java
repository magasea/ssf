package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 六月 - 08
 */
@Document(collection = "ui_calc_result")
public class MongoCaculateResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Field( value = "user_prod_id")
  @Indexed(direction = IndexDirection.DESCENDING)
  private Long userProdId;

  @Field(value ="fund_code")
  String fundCode;

  @Field( value = "curr_quantity")
  private Long currQuantity;

  @Field(value = "curr_hash")
  private String currHash;

  @Field(value = "oem_id")
  private Long oemId;

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

  public Long getCurrQuantity() {
    return currQuantity;
  }

  public void setCurrQuantity(Long currQuantity) {
    this.currQuantity = currQuantity;
  }

  public String getCurrHash() {
    return currHash;
  }

  public void setCurrHash(String currHash) {
    this.currHash = currHash;
  }

  public Long getOemId() {
    return oemId;
  }

  public void setOemId(Long oemId) {
    this.oemId = oemId;
  }
}
