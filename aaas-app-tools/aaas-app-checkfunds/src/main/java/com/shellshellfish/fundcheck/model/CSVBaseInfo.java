package com.shellshellfish.fundcheck.model;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
public class CSVBaseInfo {



  @CsvBindByName(column = "代码", required = true)
  private String code; //基准代码

  @CsvBindByName(column = "基准名称", required = true)
  private String baseName;

  @CsvBindByName(column = "日期", required = true)
  private String navlatestdate; //最新净值日期
  @CsvBindByName(column = "收盘价", required = true)
  private String close; //单位净值

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getBaseName() {
    return baseName;
  }

  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  public String getNavlatestdate() {
    return navlatestdate;
  }

  public void setNavlatestdate(String navlatestdate) {
    this.navlatestdate = navlatestdate;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }
}
