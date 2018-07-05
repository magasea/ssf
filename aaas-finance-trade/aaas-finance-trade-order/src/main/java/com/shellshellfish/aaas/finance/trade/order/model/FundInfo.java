package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundInfo")
public class FundInfo {
  @Id
  private String id;

  @Field("fundname")
  private String fundname; //基金名称

  @Field("fundcode")
  private String fundcode; //基金代号

  @Field("minshare")
  private String minshare; //最小持有份额
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFundname() {
    return fundname;
  }

  public void setFundname(String fundname) {
    this.fundname = fundname;
  }

  public String getFundcode() {
    return fundcode;
  }

  public void setFundcode(String fundcode) {
    this.fundcode = fundcode;
  }

  public String getMinshare() {
    return minshare;
  }

  public void setMinshare(String minshare) {
    this.minshare = minshare;
  }

  public FundInfo() {
  }
}
