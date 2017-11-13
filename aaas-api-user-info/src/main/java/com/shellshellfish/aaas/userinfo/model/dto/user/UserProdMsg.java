package com.shellshellfish.aaas.userinfo.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProdMsg {
  String prodId;
  String title;
  String content;
  Long date;
}
