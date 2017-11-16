package com.shellshellfish.aaas.userinfo.model.dto.vo.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAssetsDataFilterVo {
  String beginDate;

  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  String endDate;
}
