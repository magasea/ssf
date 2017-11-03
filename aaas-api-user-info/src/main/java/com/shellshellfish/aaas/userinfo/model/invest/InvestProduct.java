package com.shellshellfish.aaas.userinfo.model.invest;

public class InvestProduct {
    Long id;
    String name;
    Float ratio;
    Long amount;

    public InvestProduct(Long id, String name, Float ratio, Long amount) {
        this.id = id;
        this.name = name;
        this.ratio = ratio;
        this.amount = amount;
    }

    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
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
