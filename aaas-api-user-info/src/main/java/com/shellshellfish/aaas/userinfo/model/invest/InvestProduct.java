package com.shellshellfish.aaas.userinfo.model.invest;

public class InvestProduct {
    Long investId;
    String name;
    Float ratio;
    Long amount;

    public InvestProduct(Long investId, String name, Float ratio, Long amount) {
        this.investId = investId;
        this.name = name;
        this.ratio = ratio;
        this.amount = amount;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
