package com.shellshellfish.aaas.userinfo.model.user;


import com.shellshellfish.aaas.userinfo.model.invest.InvestProduct;
import java.util.List;

public class UserPortfolio {
    Long userId;
    List<InvestProduct> invests;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<InvestProduct> getInvests() {
        return invests;
    }

    public void setInvests(List<InvestProduct> invests) {
        this.invests = invests;
    }
}
