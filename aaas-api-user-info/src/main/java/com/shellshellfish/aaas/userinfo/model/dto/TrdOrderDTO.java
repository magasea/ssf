package com.shellshellfish.aaas.userinfo.model.dto;

public class TrdOrderDTO {

  private long id;
  private String orderId;
  private String bankCardNum;
  private String prodCode;
  private int orderStatus;
  private long orderDate;
  private int orderType;
  private long payAmount;
  private Long payFee;
  private long userId;
  private long prodId;
  private long userProdId;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;
  private Long buyDiscount;
  private Long buyFee;
  private Long boughtDate;
  private Integer tradeType;
  private String fundCode;
  private Long fundNum;
  private Long fundNumConfirmed;
  private Long fundQuantity;
  private Integer orderDetailStatus;
  private String tradeApplySerial;
  private String errMsg;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getBankCardNum() {
    return bankCardNum;
  }

  public void setBankCardNum(String bankCardNum) {
    this.bankCardNum = bankCardNum;
  }

  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  public int getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
  }

  public long getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(long orderDate) {
    this.orderDate = orderDate;
  }

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  public long getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(long payAmount) {
    this.payAmount = payAmount;
  }

  public Long getPayFee() {
    return payFee;
  }

  public void setPayFee(Long payFee) {
    this.payFee = payFee;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getProdId() {
    return prodId;
  }

  public void setProdId(long prodId) {
    this.prodId = prodId;
  }

  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
  }

  public long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  public long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  public Long getBuyDiscount() {
    return buyDiscount;
  }

  public void setBuyDiscount(Long buyDiscount) {
    this.buyDiscount = buyDiscount;
  }

  public Long getBuyFee() {
    return buyFee;
  }

  public void setBuyFee(Long buyFee) {
    this.buyFee = buyFee;
  }

  public Long getBoughtDate() {
    return boughtDate;
  }

  public void setBoughtDate(Long boughtDate) {
    this.boughtDate = boughtDate;
  }

  public Integer getTradeType() {
    return tradeType;
  }

  public void setTradeType(Integer tradeType) {
    this.tradeType = tradeType;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public Long getFundNum() {
    return fundNum;
  }

  public void setFundNum(Long fundNum) {
    this.fundNum = fundNum;
  }

  public Long getFundNumConfirmed() {
    return fundNumConfirmed;
  }

  public void setFundNumConfirmed(Long fundNumConfirmed) {
    this.fundNumConfirmed = fundNumConfirmed;
  }

  public Long getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(Long fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  public Integer getOrderDetailStatus() {
    return orderDetailStatus;
  }

  public void setOrderDetailStatus(Integer orderDetailStatus) {
    this.orderDetailStatus = orderDetailStatus;
  }

  public String getTradeApplySerial() {
    return tradeApplySerial;
  }

  public void setTradeApplySerial(String tradeApplySerial) {
    this.tradeApplySerial = tradeApplySerial;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
    result = 31 * result + (bankCardNum != null ? bankCardNum.hashCode() : 0);
    result = 31 * result + (prodCode != null ? prodCode.hashCode() : 0);
    result = 31 * result + orderStatus;
    result = 31 * result + (int) (orderDate ^ (orderDate >>> 32));
    result = 31 * result + orderType;
    result = 31 * result + (int) (payAmount ^ (payAmount >>> 32));
    result = 31 * result + (payFee != null ? payFee.hashCode() : 0);
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (int) (prodId ^ (prodId >>> 32));
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (buyDiscount != null ? buyDiscount.hashCode() : 0);
    result = 31 * result + (buyFee != null ? buyFee.hashCode() : 0);
    result = 31 * result + (boughtDate != null ? boughtDate.hashCode() : 0);
    result = 31 * result + (tradeType != null ? tradeType.hashCode() : 0);
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (fundNum != null ? fundNum.hashCode() : 0);
    result = 31 * result + (fundNumConfirmed != null ? fundNumConfirmed.hashCode() : 0);
    result = 31 * result + (fundQuantity != null ? fundQuantity.hashCode() : 0);
    result = 31 * result + (orderDetailStatus != null ? orderDetailStatus.hashCode() : 0);
    result = 31 * result + (tradeApplySerial != null ? tradeApplySerial.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TrdOrderDTO{" +
            "id=" + id +
            ", orderId='" + orderId + '\'' +
            ", bankCardNum='" + bankCardNum + '\'' +
            ", prodCode='" + prodCode + '\'' +
            ", orderStatus=" + orderStatus +
            ", orderDate=" + orderDate +
            ", orderType=" + orderType +
            ", payAmount=" + payAmount +
            ", payFee=" + payFee +
            ", userId=" + userId +
            ", prodId=" + prodId +
            ", userProdId=" + userProdId +
            ", createBy=" + createBy +
            ", createDate=" + createDate +
            ", updateBy=" + updateBy +
            ", updateDate=" + updateDate +
            ", buyDiscount=" + buyDiscount +
            ", buyFee=" + buyFee +
            ", boughtDate=" + boughtDate +
            ", tradeType=" + tradeType +
            ", fundCode='" + fundCode + '\'' +
            ", fundNum=" + fundNum +
            ", fundNumConfirmed=" + fundNumConfirmed +
            ", fundQuantity=" + fundQuantity +
            ", orderDetailStatus=" + orderDetailStatus +
            ", tradeApplySerial='" + tradeApplySerial + '\'' +
            ", errMsg='" + errMsg + '\'' +
            '}';
  }
}
