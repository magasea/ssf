package com.shellshellfish.aaas.finance.trade.pay.model;

public class BuyFundResult {
    private String applySerial;
    private String capitalMode;
    private String requestDate;
    private String outsideOrderNo;
    private String confirmdate;
    private String kkstat;

    public String getApplySerial() {
        return applySerial;
    }

    public void setApplySerial(String applySerial) {
        this.applySerial = applySerial;
    }

    public String getCapitalMode() {
        return capitalMode;
    }

    public void setCapitalMode(String capitalMode) {
        this.capitalMode = capitalMode;
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

    public String getKkstat() {
        return kkstat;
    }

    public void setKkstat(String kkstat) {
        this.kkstat = kkstat;
    }

    public String getConfirmdate() {
        return confirmdate;
    }

    public void setConfirmdate(String confirmdate) {
        this.confirmdate = confirmdate;
    }
}
