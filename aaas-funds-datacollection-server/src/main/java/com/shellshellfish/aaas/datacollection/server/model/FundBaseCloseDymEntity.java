package com.shellshellfish.aaas.datacollection.server.model;

import java.util.Map;
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
@Document(collection = "fundbaseclose")
public class FundBaseCloseDymEntity {
  @Id
  @Field( value = "_id")
  String id;

  private Map<String, Object> details;

  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(Map<String, Object> details) {
    this.details = details;
  }
}
