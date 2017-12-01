package com.shellshellfish.aaas.userinfo.model.dto;


import java.util.List;

public class UserInfoBankCardsDTO {


    Long userId;
    List<BankCardDTO> bankCardList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<BankCardDTO> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCardDTO> bankCardList) {
        this.bankCardList = bankCardList;
    }

}
