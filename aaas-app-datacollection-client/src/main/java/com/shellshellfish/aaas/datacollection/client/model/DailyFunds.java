package com.shellshellfish.aaas.datacollection.client.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DailyFunds {

  String id;

  Long navLatestDate;

  Float fundsScale;

  String code;

  Double navAdj;

  String queryDate;

  Float bmIndexChgPct;

  Float  yieldOf7Days;

  String update;

  Double millionRevenue;

  Double navUnit;

  Double navAccum;

  String fname;

  String firstInvestType;

  String secondInvestType;

  Double close;

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

