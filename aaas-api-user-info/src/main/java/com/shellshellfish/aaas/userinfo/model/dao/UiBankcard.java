package com.shellshellfish.aaas.userinfo.model.dao;

import java.sql.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 17
 */

@Entity
@Table(name = "ui_bankcard", schema = "ssfuser", catalog = "")
public class UiBankcard {

  private long id;
  private long userId;
  private String userName;
  private String userPid;
  private String cellphone;
  private String bankName;
  private String cardNumber;
  private Date expireDate;
  private int status;
  private Long createdBy;
  private Long createdDate;
  private Long updateBy;
  private Long updateDate;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "user_id")
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "user_name")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Basic
  @Column(name = "user_pid")
  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  @Basic
  @Column(name = "cellphone")
  public String getCellphone() {
    return cellphone;
  }

  public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
  }

  @Basic
  @Column(name = "bank_name")
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  @Basic
  @Column(name = "card_number")
  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  @Basic
  @Column(name = "expire_date")
  public Date getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(Date expireDate) {
    this.expireDate = expireDate;
  }

  @Basic
  @Column(name = "status")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Basic
  @Column(name = "created_by")
  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  @Basic
  @Column(name = "created_date")
  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "update_by")
  public Long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Long updateBy) {
    this.updateBy = updateBy;
  }

  @Basic
  @Column(name = "update_date")
  public Long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Long updateDate) {
    this.updateDate = updateDate;
  }

  @Override
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
    if (status != that.status) {
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

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (userId ^ (userId >>> 32));
    result = 31 * result + (userName != null ? userName.hashCode() : 0);
    result = 31 * result + (userPid != null ? userPid.hashCode() : 0);
    result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
    result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
    result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
    result = 31 * result + (expireDate != null ? expireDate.hashCode() : 0);
    result = 31 * result + status;
    result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    return result;
  }
}
