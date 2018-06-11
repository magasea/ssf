package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "limitInfo")
public class LimitInfo{
  @Id
  private String id;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

}
