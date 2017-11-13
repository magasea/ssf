package com.shellshellfish.aaas.userinfo.model.dto.user;


import java.math.BigInteger;

public class UserPortfolio {

    private long id;

    private BigInteger investProdId;

    private BigInteger userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigInteger getInvestProdId() {
        return investProdId;
    }

    public void setInvestProdId(BigInteger investProdId) {
        this.investProdId = investProdId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
