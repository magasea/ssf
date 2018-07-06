package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "limitInfo")
public class LimitInfo{
  @Id
  private String id;
  @Field("fundCode")
  private String fundCode; //基金代号
  @Field("businFlag")
  private String businFlag; //操作类型
  @Field("minValue")
  private String minValue; //最小交易额
  @Field("maxValue")
  private String maxValue; //最大交易额

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public String getFundCode() {
    return fundCode;
  }
  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getBusinFlag() {
    return businFlag;
  }

  public void setBusinFlag(String businFlag) {
    this.businFlag = businFlag;
  }

  public String getMinValue() {
    return minValue;
  }

  public void setMinValue(String minValue) {
    this.minValue = minValue;
  }

  public String getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(String maxValue) {
    this.maxValue = maxValue;
  }

  public LimitInfo() {
  }
}
