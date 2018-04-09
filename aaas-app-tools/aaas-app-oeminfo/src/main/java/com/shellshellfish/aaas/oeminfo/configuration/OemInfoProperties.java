package com.shellshellfish.aaas.oeminfo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Created by chenwei on 2018- 四月 - 04
 */
@Component
@ConfigurationProperties(prefix = "oem-info")
public class OemInfoProperties {
  List<OemInfo> oemInfos;


  public static class OemInfo{
    String oemId;
    String oemName;
    String prodName;
    String bankPhone;

    public String getOemId() {
      return oemId;
    }

    public void setOemId(String oemId) {
      this.oemId = oemId;
    }

    public String getOemName() {
      return oemName;
    }

    public void setOemName(String oemName) {
      this.oemName = oemName;
    }

    public String getProdName() {
      return prodName;
    }

    public void setProdName(String prodName) {
      this.prodName = prodName;
    }

    public String getBankPhone() {
      return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
      this.bankPhone = bankPhone;
    }
  }

  public List<OemInfo> getOemInfos() {
    return oemInfos;
  }

  public void setOemInfos(
      List<OemInfo> oemInfos) {
    this.oemInfos = oemInfos;
  }
}
