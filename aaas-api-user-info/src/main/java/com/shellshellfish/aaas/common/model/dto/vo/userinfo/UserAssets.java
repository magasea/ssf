package com.shellshellfish.aaas.common.model.dto.vo.userinfo;

public class UserAssets {
//  "totalAssetsValue":"2478.90",
//      "dailyProfit":"0.90",
//      "accumulatedProfit":"78.90"
 String totalAssetsValue;
  String dailyProfit;
  String accumulatedProfit;

  public  String getTotalAssetsValue() {
    return totalAssetsValue;
  }
  public void   setTotalAssetsValue(String totalAssetsValue) {
    this.totalAssetsValue = totalAssetsValue;
  }
  public String getDailyProfit() {
    return dailyProfit;
  }
  public void   setDailyProfit(String dailyProfit) {
    this.dailyProfit = dailyProfit;
  }
  public String getAccumulatedProfit() {
    return accumulatedProfit;
  }
  public void   setAccumulatedProfit(String accumulatedProfit) {
    this.accumulatedProfit = accumulatedProfit;
  }
}
