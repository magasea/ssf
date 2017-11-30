package com.shellshellfish.aaas.datacollection.server.model;

import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "dailyfunds")
public class DailyFunds {
  @Id
  String id;
  @Column(name = "navunit")
  String navUnit;

  String update;
  @Column(name = "fundscale")
  String fundScale;
  @Column(name = "navaccum")
  String navAccum;
  @Column(name = "yieldof7days")
  String yieldOf7Days;
  @Column(name = "querydate")
  String queryDate;
  @Column(name = "navadj")
  String navAdj;
  @Column(name = "navlatestdate")
  String navLatestDate;
  String code;
  @Column(name = "bmIndexChgPct")
  String bmIndexChgPct;
  @Column(name = "10kunityield")
  String millionRevenue;


  @Override
  public String toString() {
    return "DailyFunds{" +
        "id='" + id + '\'' +
        ", navUnit='" + navUnit + '\'' +
        ", update='" + update + '\'' +
        ", fundScale='" + fundScale + '\'' +
        ", navAccum='" + navAccum + '\'' +
        ", yieldOf7Days='" + yieldOf7Days + '\'' +
        ", queryDate='" + queryDate + '\'' +
        ", navAdj='" + navAdj + '\'' +
        ", navLatestDate='" + navLatestDate + '\'' +
        ", code='" + code + '\'' +
        ", bmIndexChgPct='" + bmIndexChgPct + '\'' +
        ", millionRevenue='" + millionRevenue + '\'' +
        '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNavUnit() {
    return navUnit;
  }

  public void setNavUnit(String navUnit) {
    this.navUnit = navUnit;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update = update;
  }

  public String getFundScale() {
    return fundScale;
  }

  public void setFundScale(String fundScale) {
    this.fundScale = fundScale;
  }

  public String getNavAccum() {
    return navAccum;
  }

  public void setNavAccum(String navAccum) {
    this.navAccum = navAccum;
  }

  public String getYieldOf7Days() {
    return yieldOf7Days;
  }

  public void setYieldOf7Days(String yieldOf7Days) {
    this.yieldOf7Days = yieldOf7Days;
  }

  public String getQueryDate() {
    return queryDate;
  }

  public void setQueryDate(String queryDate) {
    this.queryDate = queryDate;
  }

  public String getNavAdj() {
    return navAdj;
  }

  public void setNavAdj(String navAdj) {
    this.navAdj = navAdj;
  }

  public String getNavLatestDate() {
    return navLatestDate;
  }

  public void setNavLatestDate(String navLatestDate) {
    this.navLatestDate = navLatestDate;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getBminDexChgPct() {
    return bmIndexChgPct;
  }

  public void setBminDexChgPct(String bmIndexChgPct) {
    this.bmIndexChgPct = bmIndexChgPct;
  }

  public String getMillionRevenue() {
    return millionRevenue;
  }

  public void setMillionRevenue(String millionRevenue) {
    this.millionRevenue = millionRevenue;
  }
}
