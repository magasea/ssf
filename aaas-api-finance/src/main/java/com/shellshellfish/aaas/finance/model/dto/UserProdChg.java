package com.shellshellfish.aaas.finance.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@Document(collection = "fn_upc")
public class UserProdChg {
  @Id
  String id;

  Integer groupId;
  Integer prodId;

  Long modifySeq;

  Integer userProdId;

  Long modifyTime;
  String modifyComment;
  Long modifyType;
  String modifyTypeComment;
  Long createTime;

}
