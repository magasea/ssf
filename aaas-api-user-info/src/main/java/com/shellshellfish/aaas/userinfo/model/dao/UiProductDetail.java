package com.shellshellfish.aaas.userinfo.model.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

@Entity
@Table(name = "ui_product_detail", schema = "ssfuser", catalog = "")
public class UiProductDetail {

  private long id;
  private long userProdId;
  private String fundCode;
  private String fundName;
  private int fundShare;
  private int fundQuantity;
  private long updateBy;
  private long updateDate;
  private long createBy;
  private long createDate;
  private Integer status;

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
  @Column(name = "user_prod_id")
  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
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
  @Column(name = "fund_share")
  public int getFundShare() {
    return fundShare;
  }

  public void setFundShare(int fundShare) {
    this.fundShare = fundShare;
  }

  @Basic
  @Column(name = "fund_quantity")
  public int getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(int fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  @Basic
  @Column(name = "update_by")
  public long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  @Basic
  @Column(name = "update_date")
  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  @Basic
  @Column(name = "create_by")
  public long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  @Basic
  @Column(name = "create_date")
  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }
  
  @Basic
  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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
    if (userProdId != that.userProdId) {
      return false;
    }
    if (fundShare != that.fundShare) {
      return false;
    }
    if (fundQuantity != that.fundQuantity) {
      return false;
    }
    if (updateBy != that.updateBy) {
      return false;
    }
    if (updateDate != that.updateDate) {
      return false;
    }
    if (createBy != that.createBy) {
      return false;
    }
    if (createDate != that.createDate) {
      return false;
    }
    if (fundCode != null ? !fundCode.equals(that.fundCode) : that.fundCode != null) {
      return false;
    }
    if (fundName != null ? !fundName.equals(that.fundName) : that.fundName != null) {
      return false;
    }
    if (status != that.status) {
    	return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (fundName != null ? fundName.hashCode() : 0);
    result = 31 * result + fundShare;
    result = 31 * result + fundQuantity;
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + status;
    return result;
  }
}
