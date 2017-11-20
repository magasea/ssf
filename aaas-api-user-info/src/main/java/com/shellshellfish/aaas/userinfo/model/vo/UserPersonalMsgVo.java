package com.shellshellfish.aaas.userinfo.model.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPersonalMsgVo {

//  public String getUuid() {
//    return uuid;
//  }
//
//  public void setUuid(String uuid) {
//    this.uuid = uuid;
//  }
//
//  public List<String> getMessagesToUpdate() {
//    return messagesToUpdate;
//  }
//
//  public void setMessagesToUpdate(List<String> messagesToUpdate) {
//    this.messagesToUpdate = messagesToUpdate;
//  }

  public Boolean getReadedStatus() {
    return readedStatus;
  }

  public void setReadedStatus(Boolean readedStatus) {
    this.readedStatus = readedStatus;
  }

  //String uuid;
//  List<String> messagesToUpdate;
  Boolean readedStatus;
}
