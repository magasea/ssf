package com.shellshellfish.aaas.oeminfo.configuration;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
    String oemVersion;
    String homePageImgOne;
    String homePageImgTwo;
    String homePageImgThree;
    String homePageImgFour;
    String combinationOne;
    String combinationTwo;
    String combinationThree;
    String combinationFour;
    String combinationFive;
    String meHeadImg;
    String aboutLogo;

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

    public String getOemVersion() {
      return oemVersion;
    }

    public void setOemVersion(String oemVersion) {
      this.oemVersion = oemVersion;
    }

    public String getHomePageImgOne() {
      return homePageImgOne;
    }

    public void setHomePageImgOne(String homePageImgOne) {
      this.homePageImgOne = homePageImgOne;
    }

    public String getHomePageImgTwo() {
      return homePageImgTwo;
    }

    public void setHomePageImgTwo(String homePageImgTwo) {
      this.homePageImgTwo = homePageImgTwo;
    }

    public String getHomePageImgThree() {
      return homePageImgThree;
    }

    public void setHomePageImgThree(String homePageImgThree) {
      this.homePageImgThree = homePageImgThree;
    }

    public String getHomePageImgFour() {
      return homePageImgFour;
    }

    public void setHomePageImgFour(String homePageImgFour) {
      this.homePageImgFour = homePageImgFour;
    }

    public String getCombinationOne() {
      return combinationOne;
    }

    public void setCombinationOne(String combinationOne) {
      this.combinationOne = combinationOne;
    }

    public String getCombinationTwo() {
      return combinationTwo;
    }

    public void setCombinationTwo(String combinationTwo) {
      this.combinationTwo = combinationTwo;
    }

    public String getCombinationThree() {
      return combinationThree;
    }

    public void setCombinationThree(String combinationThree) {
      this.combinationThree = combinationThree;
    }

    public String getCombinationFour() {
      return combinationFour;
    }

    public void setCombinationFour(String combinationFour) {
      this.combinationFour = combinationFour;
    }

    public String getCombinationFive() {
      return combinationFive;
    }

    public void setCombinationFive(String combinationFive) {
      this.combinationFive = combinationFive;
    }

    public String getMeHeadImg() {
      return meHeadImg;
    }

    public void setMeHeadImg(String meHeadImg) {
      this.meHeadImg = meHeadImg;
    }

    public String getAboutLogo() {
      return aboutLogo;
    }

    public void setAboutLogo(String aboutLogo) {
      this.aboutLogo = aboutLogo;
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
