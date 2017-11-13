package com.shellshellfish.aaas.userinfo.model.dto.user;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UserInfoAssectsBrief {



    BigInteger userId;

    BigDecimal totalAssets;
    BigDecimal dailyProfit;
    BigDecimal totalProfit;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getDailyProfit() {
        return dailyProfit;
    }

    public void setDailyProfit(BigDecimal dailyProfit) {
        this.dailyProfit = dailyProfit;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }
}
