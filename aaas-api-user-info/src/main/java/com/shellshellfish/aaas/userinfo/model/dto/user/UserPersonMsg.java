package com.shellshellfish.aaas.userinfo.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPersonMsg {
  String content;
  String msgSource;
  String msgTitle;
  String id;
  Boolean readed;
  Long date;
  Long userId;
}
