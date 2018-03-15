package com.shellshellfish.aaas.finance.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2018- 三月 - 15
 */



@Document(collection = "fn_upc_detail")
public class UserProdChgDetail {
  @Id
  String id;

  Long userProdId;
  Long prodId;

  Long modifySeq;

  String code;
  String fundName;
  String fundType;

  Long percentBefore;
  Long percentAfter;
  Long modifyTime;





}
