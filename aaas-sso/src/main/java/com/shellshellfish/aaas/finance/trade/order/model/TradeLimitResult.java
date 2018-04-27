package com.shellshellfish.aaas.finance.trade.order.model;

public class TradeLimitResult {
    private String fundCode;
    private String shareType;
    private String custType;
    private String maxValue;
    private String minValue;
    private String sndMinValue;
    private String businFlag;
    private String trust;
    private String capitalMode;
    private String flag;
    private String custKind;
    private String otherFundCode;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getSndMinValue() {
        return sndMinValue;
    }

    public void setSndMinValue(String sndMinValue) {
        this.sndMinValue = sndMinValue;
    }

    public String getBusinFlag() {
        return businFlag;
    }

    public void setBusinFlag(String businFlag) {
        this.businFlag = businFlag;
    }

    public String getTrust() {
        return trust;
    }

    public void setTrust(String trust) {
        this.trust = trust;
    }

    public String getCapitalMode() {
        return capitalMode;
    }

    public void setCapitalMode(String capitalMode) {
        this.capitalMode = capitalMode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCustKind() {
        return custKind;
    }

    public void setCustKind(String custKind) {
        this.custKind = custKind;
    }

    public String getOtherFundCode() {
        return otherFundCode;
    }

    public void setOtherFundCode(String otherFundCode) {
        this.otherFundCode = otherFundCode;
    }
}
