package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "discountInfo")
public class DiscountInfo{
  @Id
  private String id;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

}
