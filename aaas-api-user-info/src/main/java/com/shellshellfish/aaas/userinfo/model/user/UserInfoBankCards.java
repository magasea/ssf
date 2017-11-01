package com.shellshellfish.aaas.userinfo.model.user;


import com.shellshellfish.aaas.userinfo.model.bankcard.BankCard;
import java.util.List;

public class UserInfoBankCards {


    Long userId;
    List<BankCard> bankCardList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<BankCard> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCard> bankCardList) {
        this.bankCardList = bankCardList;
    }

}
