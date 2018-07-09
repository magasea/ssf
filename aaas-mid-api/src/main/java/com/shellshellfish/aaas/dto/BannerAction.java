package com.shellshellfish.aaas.dto;

public class BannerAction {
  String pic_url;
  String action_type;
  String action;
  String title;

  public BannerAction() {
  }

  public BannerAction(String pic_url, String action_type, String action, String title) {
    this.pic_url = pic_url;
    this.action_type = action_type;
    this.action = action;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPic_url() {
    return pic_url;
  }

  public void setPic_url(String pic_url) {
    this.pic_url = pic_url;
  }

  public String getAction_type() {
    return action_type;
  }

  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
