package com.shellshellfish.aaas.finance.trade.pay.model;

import java.math.BigDecimal;

public class BankCardLimitation {
    private BigDecimal perTrans;
    private BigDecimal perDay;

    public BigDecimal getPerTrans() {
        return perTrans;
    }

    public void setPerTrans(BigDecimal perTrans) {
        this.perTrans = perTrans;
    }

    public BigDecimal getPerDay() {
        return perDay;
    }

    public void setPerDay(BigDecimal perDay) {
        this.perDay = perDay;
    }

    public BankCardLimitation(BigDecimal perTrans, BigDecimal perDay) {
        this.perTrans = perTrans;
        this.perDay = perDay;
    }
}
