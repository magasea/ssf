package com.shellshellfish.aaas.userinfo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSysMsgDTO {
  String source;
  String content;
  String id;
  Long date;
}
