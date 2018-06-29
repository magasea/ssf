package com.shellshellfish.aaas.userinfo.model.dao;

import java.math.BigDecimal;

/**
 * @Author pierre 18-06-26
 */
public class UserDailyIncomeAggregation {


    private String date;

    private BigDecimal totalIncome;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Override
    public String toString() {
        return "UserDailyIncomeAggregation{" +
                ", date='" + date + '\'' +
                ", totalIncome=" + totalIncome +
                '}';
    }
}
