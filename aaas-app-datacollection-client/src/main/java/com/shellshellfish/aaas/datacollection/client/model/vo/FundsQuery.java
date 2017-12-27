package com.shellshellfish.aaas.datacollection.client.model.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundsQuery {
  String navLatestDateStart;

  public String getNavLatestDateStart() {
    return navLatestDateStart;
  }

  public void setNavLatestDateStart(String navLatestDateStart) {
    this.navLatestDateStart = navLatestDateStart;
  }

  public String getNavLatestDateEnd() {
    return navLatestDateEnd;
  }

  public void setNavLatestDateEnd(String navLatestDateEnd) {
    this.navLatestDateEnd = navLatestDateEnd;
  }

  String navLatestDateEnd;
  List<String> codes;



  public List<String> getCodes() {
    return codes;
  }

  public void setCodes(List<String> codes) {
    this.codes = codes;
  }
}
