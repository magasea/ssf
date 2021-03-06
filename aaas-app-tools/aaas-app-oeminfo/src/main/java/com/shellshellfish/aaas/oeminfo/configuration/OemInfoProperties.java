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
    String combinationOneAction;
    String combinationTwoAction;
    String combinationThreeAction;
    String combinationOneTitle;
    String combinationTwoTitle;
    String combinationThreeTitle;
    String meHeadImg;
    String aboutLogo;

    public String getCombinationOneTitle() {
      return combinationOneTitle;
    }

    public void setCombinationOneTitle(String combinationOneTitle) {
      this.combinationOneTitle = combinationOneTitle;
    }

    public String getCombinationTwoTitle() {
      return combinationTwoTitle;
    }

    public void setCombinationTwoTitle(String combinationTwoTitle) {
      this.combinationTwoTitle = combinationTwoTitle;
    }

    public String getCombinationThreeTitle() {
      return combinationThreeTitle;
    }

    public void setCombinationThreeTitle(String combinationThreeTitle) {
      this.combinationThreeTitle = combinationThreeTitle;
    }

    public String getCombinationOneAction() {
      return combinationOneAction;
    }

    public void setCombinationOneAction(String combinationOneAction) {
      this.combinationOneAction = combinationOneAction;
    }

    public String getCombinationTwoAction() {
      return combinationTwoAction;
    }

    public void setCombinationTwoAction(String combinationTwoAction) {
      this.combinationTwoAction = combinationTwoAction;
    }

    public String getCombinationThreeAction() {
      return combinationThreeAction;
    }

    public void setCombinationThreeAction(String combinationThreeAction) {
      this.combinationThreeAction = combinationThreeAction;
    }

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
