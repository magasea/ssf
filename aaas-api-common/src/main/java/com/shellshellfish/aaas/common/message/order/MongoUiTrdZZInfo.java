package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;

/**
 * The persistent class for the ui_trd_log database table.
 */

public class MongoUiTrdZZInfo implements Serializable {
    private static final long serialVersionUID = 1L;


    private String id;


    private Long createdBy;


    private Long createdDate;


    private String lastModifiedBy;


    private Long lastModifiedDate;


    private int operations;


    private Long userProdId;


    private String fundCode;


    private String fundName;


    private String applyDate;


    private String applySerial;


    private String melonMethod;


    private int tradeStatus;


    private Long userId;


    private String confirmFlag;


    private String confirmStat;


    private String oriApplyDate;


    private String confirmDate;


    private String bankAcco;


    private String bankName;


    private String businFlagStr;


    private String occurBankAcco;


    private String bankSerial;


    private String outSideOrderNo;


    private int tradeType;


    private Long tradeConfirmShare;


    private Long tradeConfirmSum;


    private Long tradeTargetShare;


    private Long tradeTargetSum;

    private Long fee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getOperations() {
        return operations;
    }

    public void setOperations(int operations) {
        this.operations = operations;
    }

    public Long getUserProdId() {
        return userProdId;
    }

    public void setUserProdId(Long userProdId) {
        this.userProdId = userProdId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplySerial() {
        return applySerial;
    }

    public void setApplySerial(String applySerial) {
        this.applySerial = applySerial;
    }

    public String getMelonMethod() {
        return melonMethod;
    }

    public void setMelonMethod(String melonMethod) {
        this.melonMethod = melonMethod;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmStat() {
        return confirmStat;
    }

    public void setConfirmStat(String confirmStat) {
        this.confirmStat = confirmStat;
    }

    public String getOriApplyDate() {
        return oriApplyDate;
    }

    public void setOriApplyDate(String oriApplyDate) {
        this.oriApplyDate = oriApplyDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getBankAcco() {
        return bankAcco;
    }

    public void setBankAcco(String bankAcco) {
        this.bankAcco = bankAcco;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBusinFlagStr() {
        return businFlagStr;
    }

    public void setBusinFlagStr(String businFlagStr) {
        this.businFlagStr = businFlagStr;
    }

    public String getOccurBankAcco() {
        return occurBankAcco;
    }

    public void setOccurBankAcco(String occurBankAcco) {
        this.occurBankAcco = occurBankAcco;
    }

    public String getBankSerial() {
        return bankSerial;
    }

    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }

    public String getOutSideOrderNo() {
        return outSideOrderNo;
    }

    public void setOutSideOrderNo(String outSideOrderNo) {
        this.outSideOrderNo = outSideOrderNo;
    }

    public Long getTradeConfirmShare() {
        return tradeConfirmShare;
    }

    public void setTradeConfirmShare(Long tradeConfirmShare) {
        this.tradeConfirmShare = tradeConfirmShare;
    }

    public Long getTradeConfirmSum() {
        return tradeConfirmSum;
    }

    public void setTradeConfirmSum(Long tradeConfirmSum) {
        this.tradeConfirmSum = tradeConfirmSum;
    }

    public Long getTradeTargetShare() {
        return tradeTargetShare;
    }

    public void setTradeTargetShare(Long tradeTargetShare) {
        this.tradeTargetShare = tradeTargetShare;
    }

    public Long getTradeTargetSum() {
        return tradeTargetSum;
    }

    public void setTradeTargetSum(Long tradeTargetSum) {
        this.tradeTargetSum = tradeTargetSum;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }


    @Override
    public String toString() {
        return "MongoUiTrdZZInfo{" +
                "id='" + id + '\'' +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", operations=" + operations +
                ", userProdId=" + userProdId +
                ", fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", applySerial='" + applySerial + '\'' +
                ", melonMethod='" + melonMethod + '\'' +
                ", tradeStatus=" + tradeStatus +
                ", userId=" + userId +
                ", confirmFlag='" + confirmFlag + '\'' +
                ", confirmStat='" + confirmStat + '\'' +
                ", oriApplyDate='" + oriApplyDate + '\'' +
                ", confirmDate='" + confirmDate + '\'' +
                ", bankAcco='" + bankAcco + '\'' +
                ", bankName='" + bankName + '\'' +
                ", businFlagStr='" + businFlagStr + '\'' +
                ", occurBankAcco='" + occurBankAcco + '\'' +
                ", bankSerial='" + bankSerial + '\'' +
                ", outSideOrderNo='" + outSideOrderNo + '\'' +
                ", tradeType=" + tradeType +
                ", tradeConfirmShare=" + tradeConfirmShare +
                ", tradeConfirmSum=" + tradeConfirmSum +
                ", tradeTargetShare=" + tradeTargetShare +
                ", tradeTargetSum=" + tradeTargetSum +
                ", fee=" + fee +
                '}';
    }
}