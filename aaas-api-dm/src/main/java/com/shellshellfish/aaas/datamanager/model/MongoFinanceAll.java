package com.shellshellfish.aaas.datamanager.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ui_finance_all")
public class MongoFinanceAll implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Field(value = "serial")
  private Integer serial;
  
  @Field(value = "total")
  private Integer total;
  
  @Field(value = "totalPage")
  private Integer totalPage;

  @Field(value = "date")
  private String date;

  // @Field(value = "head")
  // private Head head;
  //
  // @Field(value = "jonResult")
  // private Object result;

  @Field(value = "result")
  private Object result;

  @Column(name = "last_modified_by")
  private String lastModifiedBy;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  // public Head getHead() {
  // return head;
  // }
  //
  // public void setHead(Head head) {
  // this.head = head;
  // }
  //
  // public Object getResult() {
  // return result;
  // }
  //
  // public void setResult(Object result) {
  // this.result = result;
  // }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }


  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Integer getSerial() {
    return serial;
  }

  public void setSerial(Integer serial) {
    this.serial = serial;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(Integer totalPage) {
    this.totalPage = totalPage;
  }
  
}
