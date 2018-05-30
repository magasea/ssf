package com.shellshellfish.aaas.datacollection.server.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 二月 - 05
 */
@Document(collection = "coinfund_yieldrate")
public class CoinFunds {
  @Field( value="_id")
  String id;
  @Field( value="10KUNITYIELD" )
  Double unitYieldOf10K;
  @Field( value="ADJUSTEDNAV" )
  Double navAdj;
  @Field( value="YIELDOF7DAYS" )
  Double yieldOf7Days;
  @Field( value="update" )
  Long update;
  @Field( value="querydate" )
  Long queryDate;
  @Field( value="code" )
  String code;
  @Field( value="querydatestr" )
  String queryDateStr;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Double getUnitYieldOf10K() {
    return unitYieldOf10K;
  }

  public void setUnitYieldOf10K(Double unitYieldOf10K) {
    this.unitYieldOf10K = unitYieldOf10K;
  }

  public Double getNavAdj() {
    return navAdj;
  }

  public void setNavAdj(Double navAdj) {
    this.navAdj = navAdj;
  }

  public Double getYieldOf7Days() {
    return yieldOf7Days;
  }

  public void setYieldOf7Days(Double yieldOf7Days) {
    this.yieldOf7Days = yieldOf7Days;
  }

  public Long getUpdate() {
    return update;
  }

  public void setUpdate(Long update) {
    this.update = update;
  }

  public Long getQueryDate() {
    return queryDate;
  }

  public void setQueryDate(Long queryDate) {
    this.queryDate = queryDate;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getQueryDateStr() {
    return queryDateStr;
  }

  public void setQueryDateStr(String queryDateStr) {
    this.queryDateStr = queryDateStr;
  }
}
