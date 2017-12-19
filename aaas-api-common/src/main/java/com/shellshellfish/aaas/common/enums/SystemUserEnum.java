package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2017- 十二月 - 18
 */
public enum SystemUserEnum {
  SYSTEM_USER_ENUM(0L);

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  private Long userId;
  SystemUserEnum(Long userId){
    this.userId = userId;
  }
}
