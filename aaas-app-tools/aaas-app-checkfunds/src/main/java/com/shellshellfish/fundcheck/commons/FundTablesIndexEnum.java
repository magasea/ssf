package com.shellshellfish.fundcheck.commons;

/**
 * Created by chenwei on 2018- 三月 - 13
 */

public enum FundTablesIndexEnum {
  DAILYFUNDS(0,"dailyfunds"),FUNDRESOURCES(1,"fundresources"),FUNDRATE(2,"fundrate"),
  FUND_YIELDRATE(3,"fund_yieldrate"),COINFUND_YIELDRATE(4,"coinfund_yieldrate"),
  FUNDBASECLOSE_ORIGIN(5,"fundbaseclose_origin"),FUNDBASECLOSE(6,"fundbaseclose");
 int index;
 String tableName;
 FundTablesIndexEnum(int index, String tableName){
   this.index = index;
   this.tableName = tableName;
 }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
