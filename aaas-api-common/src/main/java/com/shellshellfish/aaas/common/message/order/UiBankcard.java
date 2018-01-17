package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by chenwei on 2018- 一月 - 17
 */



public class UiBankcard  implements Serializable{

  private long id;
  private long userId;
  private String userName;
  private String userPid;
  private String cellphone;
  private String bankName;
  private String cardNumber;
  private Date expireDate;
  private int cardStatus;
  private Long createdBy;
  private Long createdDate;
  private Long updateBy;
  private Long updateDate;

  
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  
  
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  
  
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  
  
  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  
  
  public String getCellphone() {
    return cellphone;
  }

  public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
  }

  
  
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  
  
  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  
  
  public Date getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(Date expireDate) {
    this.expireDate = expireDate;
  }

  
  
  public int getCardStatus() {
    return cardStatus;
  }

  public void setCardStatus(int cardStatus) {
    this.cardStatus = cardStatus;
  }

  
  
  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  
  
  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  
  
  public Long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Long updateBy) {
    this.updateBy = updateBy;
  }

  
  
  public Long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Long updateDate) {
    this.updateDate = updateDate;
  }

  
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UiBankcard that = (UiBankcard) o;

    if (id != that.id) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (cardStatus != that.cardStatus) {
      return false;
    }
    if (userName != null ? !userName.equals(that.userName) : that.userName != null) {
      return false;
    }
    if (userPid != null ? !userPid.equals(that.userPid) : that.userPid != null) {
      return false;
    }
    if (cellphone != null ? !cellphone.equals(that.cellphone) : that.cellphone != null) {
      return false;
    }
    if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) {
      return false;
    }
    if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null) {
      return false;
    }
    if (expireDate != null ? !expireDate.equals(that.expireDate) : that.expireDate != null) {
      return false;
    }
    if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) {
      return false;
    }
    if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
      return false;
    }
    if (updateBy != null ? !updateBy.equals(that.updateBy) : that.updateBy != null) {
      return false;
    }
    if (updateDate != null ? !updateDate.equals(that.updateDate) : that.updateDate != null) {
      return false;
    }

    return true;
  }

  
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (userName != null ? userName.hashCode() : 0);
    result = 31 * result + (userPid != null ? userPid.hashCode() : 0);
    result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
    result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
    result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
    result = 31 * result + (expireDate != null ? expireDate.hashCode() : 0);
    result = 31 * result + cardStatus;
    result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    return result;
  }
}
