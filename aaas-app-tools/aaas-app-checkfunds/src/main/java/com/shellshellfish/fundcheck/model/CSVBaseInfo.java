package com.shellshellfish.fundcheck.model;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
public class CSVBaseInfo {


  private String date;//from yyyy-mm-dd to yyyy/MM/dd

  private String code; //基准代码





  private String close; //单位净值

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }




  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public CSVBaseInfo(String date, String code,  String close) {
    String[] dateItems = null;
    if(date.contains("/")){
      dateItems = date.split("\\/");
    }else if(date.contains("-")){
      dateItems = date.split("\\-");
    }
    this.date = String.format("%04d-%02d-%02d",Integer.parseInt(dateItems[0]),Integer.parseInt
        (dateItems[1]),Integer.parseInt(dateItems[2]));
    this.code = code;

    this.close = close;
  }
}
