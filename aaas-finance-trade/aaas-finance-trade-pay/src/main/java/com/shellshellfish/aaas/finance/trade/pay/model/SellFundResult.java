package com.shellshellfish.aaas.finance.trade.pay.model;

public class SellFundResult {
    private String applySerial;
    private String acceptDate;
    private String requestDate;
    private String outsideOrderNo;

    public String getApplySerial() {
        return applySerial;
    }

    public void setApplySerial(String applySerial) {
        this.applySerial = applySerial;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }



    public String getOutsideOrderNo() {
        return outsideOrderNo;
    }

    public void setOutsideOrderNo(String outsideOrderNo) {
        this.outsideOrderNo = outsideOrderNo;
    }
}
