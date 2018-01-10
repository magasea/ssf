package com.shellshellfish.aaas.finance.trade.pay.model;

import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import java.math.BigDecimal;

/**
 * Created by chenwei on 2018- 一月 - 09
 */

public class ZZBuyFund {

  Long userId;
  int trdBrokerId;
  String tradeAcco;
  BigDecimal applySum;
  String outsideOrderNo;
  String fundCode;
  TrdOrderDetail trdOrderDetail;

  public int getTrdBrokerId() {
    return trdBrokerId;
  }

  public void setTrdBrokerId(int trdBrokerId) {
    this.trdBrokerId = trdBrokerId;
  }

  public TrdOrderDetail getTrdOrderDetail() {
    return trdOrderDetail;
  }

  public void setTrdOrderDetail(TrdOrderDetail trdOrderDetail) {
    this.trdOrderDetail = trdOrderDetail;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getTradeAcco() {
    return tradeAcco;
  }

  public void setTradeAcco(String tradeAcco) {
    this.tradeAcco = tradeAcco;
  }

  public BigDecimal getApplySum() {
    return applySum;
  }

  public void setApplySum(BigDecimal applySum) {
    this.applySum = applySum;
  }

  public String getOutsideOrderNo() {
    return outsideOrderNo;
  }

  public void setOutsideOrderNo(String outsideOrderNo) {
    this.outsideOrderNo = outsideOrderNo;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }
}
