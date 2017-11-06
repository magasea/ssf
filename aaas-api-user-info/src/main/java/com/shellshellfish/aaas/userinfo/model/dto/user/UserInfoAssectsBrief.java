package com.shellshellfish.aaas.userinfo.model.dto.user;

public class UserInfoAssectsBrief {



    Long userId;

    String totalAssects;
    Float dailyProfit;
    Float totalProfit;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTotalAssects() {
        return totalAssects;
    }

    public void setTotalAssects(String totalAssects) {
        this.totalAssects = totalAssects;
    }

    public Float getDailyProfit() {
        return dailyProfit;
    }

    public void setDailyProfit(Float dailyProfit) {
        this.dailyProfit = dailyProfit;
    }

    public Float getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Float totalProfit) {
        this.totalProfit = totalProfit;
    }
}
