package com.shellshellfish.aaas.datacollection.client.model.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundsQuery {
  String navLatestDate;
  List<String> codes;

  public String getNavLatestDate() {
    return navLatestDate;
  }

  public void setNavLatestDate(String navLatestDate) {
    this.navLatestDate = navLatestDate;
  }

  public List<String> getCodes() {
    return codes;
  }

  public void setCodes(List<String> codes) {
    this.codes = codes;
  }
}
