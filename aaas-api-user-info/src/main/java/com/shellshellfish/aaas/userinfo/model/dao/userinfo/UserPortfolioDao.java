package com.shellshellfish.aaas.userinfo.model.dao.userinfo;


import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERINFO_PORTFOLIO")
public class UserPortfolioDao {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

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
