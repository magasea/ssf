package com.shellshellfish.aaas.finance.trade.model;

public class OneFundAccount {
    private String custNo;
    private String fundAcco;
    private String tradeAcco;
    private String applySerial;

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getFundAcco() {
        return fundAcco;
    }

    public void setFundAcco(String fundAcco) {
        this.fundAcco = fundAcco;
    }

    public String getTradeAcco() {
        return tradeAcco;
    }

    public void setTradeAcco(String tradeAcco) {
        this.tradeAcco = tradeAcco;
    }

    public String getApplySerial() {
        return applySerial;
    }

    public void setApplySerial(String applySerial) {
        this.applySerial = applySerial;
    }
}
