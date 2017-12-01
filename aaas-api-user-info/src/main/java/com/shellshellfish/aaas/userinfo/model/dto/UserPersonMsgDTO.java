package com.shellshellfish.aaas.userinfo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPersonMsgDTO {

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMsgSource() {
    return msgSource;
  }

  public void setMsgSource(String msgSource) {
    this.msgSource = msgSource;
  }

  public String getMsgTitle() {
    return msgTitle;
  }

  public void setMsgTitle(String msgTitle) {
    this.msgTitle = msgTitle;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getReaded() {
    return readed;
  }

  public void setReaded(Boolean readed) {
    this.readed = readed;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
    this.date = date;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  String content;
  String msgSource;
  String msgTitle;
  String id;
  Boolean readed;
  Long date;
  Long userId;
}
