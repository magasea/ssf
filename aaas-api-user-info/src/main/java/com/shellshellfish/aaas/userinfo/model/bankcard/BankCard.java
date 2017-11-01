package com.shellshellfish.aaas.userinfo.model.bankcard;

import java.util.Date;

public class BankCard {


    String bankCardNumber;
    String bankCardUserName;
    Date expireDate;
    String bankName;

    public BankCard(String bankCardNumber, String bankCardUserName, Date expireDate, String bankName) {
        this.bankCardNumber = bankCardNumber;
        this.bankCardUserName = bankCardUserName;
        this.expireDate = expireDate;
        this.bankName = bankName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardUserName() {
        return bankCardUserName;
    }

    public void setBankCardUserName(String bankCardUserName) {
        this.bankCardUserName = bankCardUserName;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

}
