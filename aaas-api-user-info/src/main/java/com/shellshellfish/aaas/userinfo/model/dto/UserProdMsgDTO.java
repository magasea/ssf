package com.shellshellfish.aaas.userinfo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProdMsgDTO {
  String prodId;
  String title;
  String content;
  Long date;
}
