package com.shellshellfish.aaas.userinfo.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSysMsg {
  String source;
  String content;
  String id;
  Long date;
}