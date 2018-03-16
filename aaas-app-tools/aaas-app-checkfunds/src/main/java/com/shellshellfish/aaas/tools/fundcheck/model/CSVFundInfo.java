package com.shellshellfish.aaas.tools.fundcheck.model;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
public class CSVFundInfo {




  private String code; //基金代码




  private String date; //最新净值日期

  private String unitNav; //单位净值

  private String accumuLatedNav; //累计单位净值


  private String adjustedNav;  //复权单位净值

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }



  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getUnitNav() {
    return unitNav;
  }

  public void setUnitNav(String unitNav) {
    this.unitNav = unitNav;
  }

  public String getAccumuLatedNav() {
    return accumuLatedNav;
  }

  public void setAccumuLatedNav(String accumuLatedNav) {
    this.accumuLatedNav = accumuLatedNav;
  }

  public String getAdjustedNav() {
    return adjustedNav;
  }

  public void setAdjustedNav(String adjustedNav) {
    this.adjustedNav = adjustedNav;
  }

  public CSVFundInfo(String date, String code, String unitNav,
      String accumuLatedNav, String adjustedNav) {
    this.code = code;
    String[] dateItems = null;
    if(date.contains("/")){
      dateItems = date.split("\\/");
    }else if(date.contains("-")){
      dateItems = date.split("\\-");
    }


    this.date = String.format("%04d-%02d-%02d",Integer.parseInt(dateItems[0]),Integer.parseInt
        (dateItems[1]),Integer.parseInt(dateItems[2]));
    this.unitNav = unitNav;
    this.accumuLatedNav = accumuLatedNav;
    this.adjustedNav = adjustedNav;
  }
}
