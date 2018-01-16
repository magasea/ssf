package com.shellshellfish.aaas.datacollection.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 一月 - 05
 */
@Getter
@Setter
@Document(collection = "fundbaseclose_origin")
public class FundBaseClose {
  @Id
  @Field( value = "_id")
  String id;

  //yyyy-MM-dd
//  @Indexed(name ="update", direction = IndexDirection.DESCENDING)
  @Field( value =  "update")
  Long update;

  @Field( value = "Close" )
  Double close;

  @Field( value = "code" )
  String code;

  @Indexed(name ="querydate", direction = IndexDirection.DESCENDING)
  @Field( value = "querydate")
  Long querydate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getUpdate() {
    return update;
  }

  public void setUpdate(Long update) {
    this.update = update;
  }

  public Double getClose() {
    return close;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Long getQuerydate() {
    return querydate;
  }

  public void setQuerydate(Long querydate) {
    this.querydate = querydate;
  }
}
