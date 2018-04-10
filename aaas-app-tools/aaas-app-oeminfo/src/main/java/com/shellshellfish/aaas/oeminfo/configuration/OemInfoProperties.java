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
    String version;
    String home_page1;
    String home_page2;
    String home_page3;
    String home_page4;
    String combination1;
    String combination2;
    String combination3;
    String combination4;
    String combination5;
    String me_headphoto;
    String about_logo;

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

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getHome_page1() {
      return home_page1;
    }

    public void setHome_page1(String home_page1) {
      this.home_page1 = home_page1;
    }

    public String getHome_page2() {
      return home_page2;
    }

    public void setHome_page2(String home_page2) {
      this.home_page2 = home_page2;
    }

    public String getHome_page3() {
      return home_page3;
    }

    public void setHome_page3(String home_page3) {
      this.home_page3 = home_page3;
    }

    public String getHome_page4() {
      return home_page4;
    }

    public void setHome_page4(String home_page4) {
      this.home_page4 = home_page4;
    }

    public String getCombination1() {
      return combination1;
    }

    public void setCombination1(String combination1) {
      this.combination1 = combination1;
    }

    public String getCombination2() {
      return combination2;
    }

    public void setCombination2(String combination2) {
      this.combination2 = combination2;
    }

    public String getCombination3() {
      return combination3;
    }

    public void setCombination3(String combination3) {
      this.combination3 = combination3;
    }

    public String getCombination4() {
      return combination4;
    }

    public void setCombination4(String combination4) {
      this.combination4 = combination4;
    }

    public String getCombination5() {
      return combination5;
    }

    public void setCombination5(String combination5) {
      this.combination5 = combination5;
    }

    public String getMe_headphoto() {
      return me_headphoto;
    }

    public void setMe_headphoto(String me_headphoto) {
      this.me_headphoto = me_headphoto;
    }

    public String getAbout_logo() {
      return about_logo;
    }

    public void setAbout_logo(String about_logo) {
      this.about_logo = about_logo;
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
