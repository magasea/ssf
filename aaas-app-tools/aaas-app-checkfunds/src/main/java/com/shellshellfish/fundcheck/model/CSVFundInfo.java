package com.shellshellfish.fundcheck.model;

import org.springframework.data.mongodb.core.mapping.Field;
import com.opencsv.bean.CsvBindByName;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
public class CSVFundInfo {



  @CsvBindByName(column = "代码", required = true)
  private String code; //基金代码

  @CsvBindByName(column = "基金名称", required = true)
  private String fundName;

  @CsvBindByName(column = "日期", required = true)
  private String navlatestdate; //最新净值日期
  @CsvBindByName(column = "单位净值", required = true)
  private String navunit; //单位净值
  @CsvBindByName(column = "累计单位净值", required = true)
  private String navaccum; //累计单位净值

  @CsvBindByName(column = "复权单位净值", required = true)
  private String navadj;  //复权单位净值

  public String getNavadj() {
    return navadj;
  }

  public void setNavadj(String navadj) {
    this.navadj = navadj;
  }

  public String getNavlatestdate() {
    return navlatestdate;
  }

  public void setNavlatestdate(String navlatestdate) {
    this.navlatestdate = navlatestdate;
  }

  public String getNavunit() {
    return navunit;
  }

  public void setNavunit(String navunit) {
    this.navunit = navunit;
  }

  public String getNavaccum() {
    return navaccum;
  }

  public void setNavaccum(String navaccum) {
    this.navaccum = navaccum;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }
}
