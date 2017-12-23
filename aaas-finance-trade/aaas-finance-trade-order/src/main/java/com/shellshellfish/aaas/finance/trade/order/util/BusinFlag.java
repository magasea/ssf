package com.shellshellfish.aaas.finance.trade.order.util;

public enum BusinFlag{
    BUY_FUND("022", "申购"), SELL_FUND("024", "赎回");

    private String code;
    private String desc;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    BusinFlag(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }
}
