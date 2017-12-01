package com.shellshellfish.aaas.datacollection.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "dailyfunds")
public class DailyFunds {
  @Id
  @Field(value = "_id")
  String id;
  @Indexed(name ="navlatestdate", direction = IndexDirection.DESCENDING)
  @Field( value = "navlatestdate")
  Long navLatestDate;
  @Field( value = "fundscale")
  Float fundsScale;
  @Indexed(name ="code", direction = IndexDirection.DESCENDING)
  @Field( value = "code")
  String code;
  @Field( value = "navadj")
  Double navAdj;
  @Field( value = "querydate")
  String queryDate;
  @Field( value = "bmindexchgpct")
  Float bmIndexChgPct;
  @Field( value = "yieldof7days")
  Float  yieldOf7Days;
  @Field( value = "update")
  String update;
  @Field( value = "10kunityield")
  Double millionRevenue;
  @Field( value = "navunit")
  Double navUnit;
  @Field( value = "navaccum")
  Double navAccum;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getNavLatestDate() {
    return navLatestDate;
  }

  public void setNavLatestDate(Long navLatestDate) {
    this.navLatestDate = navLatestDate;
  }

  public Float getFundsScale() {
    return fundsScale;
  }

  public void setFundsScale(Float fundsScale) {
    this.fundsScale = fundsScale;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Double getNavAdj() {
    return navAdj;
  }

  public void setNavAdj(Double navAdj) {
    this.navAdj = navAdj;
  }

  public String getQueryDate() {
    return queryDate;
  }

  public void setQueryDate(String queryDate) {
    this.queryDate = queryDate;
  }

  public Float getBmIndexChgPct() {
    return bmIndexChgPct;
  }

  public void setBmIndexChgPct(Float bmIndexChgPct) {
    this.bmIndexChgPct = bmIndexChgPct;
  }

  public Float getYieldOf7Days() {
    return yieldOf7Days;
  }

  public void setYieldOf7Days(Float yieldOf7Days) {
    this.yieldOf7Days = yieldOf7Days;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update = update;
  }

  public Double getMillionRevenue() {
    return millionRevenue;
  }

  public void setMillionRevenue(Double millionRevenue) {
    this.millionRevenue = millionRevenue;
  }

  public Double getNavUnit() {
    return navUnit;
  }

  public void setNavUnit(Double navUnit) {
    this.navUnit = navUnit;
  }

  public Double getNavAccum() {
    return navAccum;
  }

  public void setNavAccum(Double navAccum) {
    this.navAccum = navAccum;
  }


}
