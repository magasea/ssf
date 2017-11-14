package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class UiTradesLog {

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Long tradeDate) {
    this.tradeDate = tradeDate;
  }

  public int getTradeStatus() {
    return tradeStatus;
  }

  public void setTradeStatus(int tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public String getProdId() {
    return prodId;
  }

  public void setProdId(String prodId) {
    this.prodId = prodId;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Id
  public String id;
  public Long userId;
  public Long tradeDate;
  public int tradeStatus;
  public String prodId;
  public int operation;
  public BigDecimal amount;
}
