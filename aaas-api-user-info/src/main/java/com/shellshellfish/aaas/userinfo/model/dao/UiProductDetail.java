package com.shellshellfish.aaas.userinfo.model.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2018- 一月 - 29
 */

@Entity
@Table(name = "ui_product_detail", schema = "ssfuser", catalog = "")
public class UiProductDetail {

  private long id;
  private Long createBy;
  private Long createDate;
  private String fundCode;
  private String fundName;
  private Integer fundQuantity;
  private Integer fundQuantityTrade;
  private Integer fundShare;
  private Integer status;
  private Long updateBy;
  private Long updateDate;
  private Long userProdId;
  private String lastestSerial;

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
  @Column(name = "create_by")
  public Long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(Long createBy) {
    this.createBy = createBy;
  }

  @Basic
  @Column(name = "create_date")
  public Long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Long createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "fund_code")
  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  @Basic
  @Column(name = "fund_name")
  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  @Basic
  @Column(name = "fund_quantity")
  public Integer getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(Integer fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  @Basic
  @Column(name = "fund_quantity_trade")
  public Integer getFundQuantityTrade() {
    return fundQuantityTrade;
  }

  public void setFundQuantityTrade(Integer fundQuantityTrade) {
    this.fundQuantityTrade = fundQuantityTrade;
  }

  @Basic
  @Column(name = "fund_share")
  public Integer getFundShare() {
    return fundShare;
  }

  public void setFundShare(Integer fundShare) {
    this.fundShare = fundShare;
  }

  @Basic
  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  @Basic
  @Column(name = "user_prod_id")
  public Long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(Long userProdId) {
    this.userProdId = userProdId;
  }

  @Basic
  @Column(name = "lastest_serial")
  public String getLastestSerial() {
    return lastestSerial;
  }

  public void setLastestSerial(String lastestSerial) {
    this.lastestSerial = lastestSerial;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UiProductDetail that = (UiProductDetail) o;

    if (id != that.id) {
      return false;
    }
    if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) {
      return false;
    }
    if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(that.fundCode) : that.fundCode != null) {
      return false;
    }
    if (fundName != null ? !fundName.equals(that.fundName) : that.fundName != null) {
      return false;
    }
    if (fundQuantity != null ? !fundQuantity.equals(that.fundQuantity)
        : that.fundQuantity != null) {
      return false;
    }
    if (fundQuantityTrade != null ? !fundQuantityTrade.equals(that.fundQuantityTrade)
        : that.fundQuantityTrade != null) {
      return false;
    }
    if (fundShare != null ? !fundShare.equals(that.fundShare) : that.fundShare != null) {
      return false;
    }
    if (status != null ? !status.equals(that.status) : that.status != null) {
      return false;
    }
    if (updateBy != null ? !updateBy.equals(that.updateBy) : that.updateBy != null) {
      return false;
    }
    if (updateDate != null ? !updateDate.equals(that.updateDate) : that.updateDate != null) {
      return false;
    }
    if (userProdId != null ? !userProdId.equals(that.userProdId) : that.userProdId != null) {
      return false;
    }
    if (lastestSerial != null ? !lastestSerial.equals(that.lastestSerial)
        : that.lastestSerial != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (fundName != null ? fundName.hashCode() : 0);
    result = 31 * result + (fundQuantity != null ? fundQuantity.hashCode() : 0);
    result = 31 * result + (fundQuantityTrade != null ? fundQuantityTrade.hashCode() : 0);
    result = 31 * result + (fundShare != null ? fundShare.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    result = 31 * result + (userProdId != null ? userProdId.hashCode() : 0);
    result = 31 * result + (lastestSerial != null ? lastestSerial.hashCode() : 0);
    return result;
  }
}
