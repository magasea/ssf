package com.shellshellfish.aaas.finance.trade.order.model;

import java.math.BigDecimal;

public class PoundageResult {
    private BigDecimal poundage;
    private BigDecimal discountSaving;

    public PoundageResult() {

    }

    public PoundageResult(BigDecimal poundage, BigDecimal discountSaving) {
        this.poundage = poundage;
        this.discountSaving = discountSaving;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public BigDecimal getDiscountSaving() {
        return discountSaving;
    }

    public void setDiscountSaving(BigDecimal discountSaving) {
        this.discountSaving = discountSaving;
    }
}
