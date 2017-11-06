package com.shellshellfish.aaas.common.model.dao.userinfo;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="USERINFO_PORTFOLIO")
public class UserPortfolioDao {
    Long userId;

    public Long getInvestProdId() {
        return investProdId;
    }

    public void setInvestProdId(Long investProdId) {
        this.investProdId = investProdId;
    }

    Long investProdId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
