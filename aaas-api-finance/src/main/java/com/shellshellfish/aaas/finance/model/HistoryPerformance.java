package com.shellshellfish.aaas.finance.model;

import java.math.BigDecimal;

public class HistoryPerformance {
    private Double accumulatedIncome;
    private Double annulizedIncome;
    private Double maxPullback;
    private Double ratio1;
    private Double sharpoRatio;

    public Double getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(Double accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    public Double getAnnulizedIncome() {
        return annulizedIncome;
    }

    public void setAnnulizedIncome(Double annulizedIncome) {
        this.annulizedIncome = annulizedIncome;
    }

    public Double getMaxPullback() {
        return maxPullback;
    }

    public void setMaxPullback(Double maxPullback) {
        this.maxPullback = maxPullback;
    }

    public Double getRatio1() {
        return ratio1;
    }

    public void setRatio1(Double ratio1) {
        this.ratio1 = ratio1;
    }

    public Double getSharpoRatio() {
        return sharpoRatio;
    }

    public void setSharpoRatio(Double sharpoRatio) {
        this.sharpoRatio = sharpoRatio;
    }
}
