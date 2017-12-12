package com.shellshellfish.aaas.model;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trd_trade_broker", schema = "ssftrdorder", catalog = "")
public class TrdTradeBrokerEntity {

  private long id;
  private BigDecimal createBy;
  private BigDecimal createDate;
  private int priority;
  private Integer tradeBrokerId;
  private String tradeBrokerName;
  private BigDecimal updateBy;
  private BigDecimal updateDate;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "create_by")
  public BigDecimal getCreateBy() {
    return createBy;
  }

  public void setCreateBy(BigDecimal createBy) {
    this.createBy = createBy;
  }

  @Basic
  @Column(name = "create_date")
  public BigDecimal getCreateDate() {
    return createDate;
  }

  public void setCreateDate(BigDecimal createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "priority")
  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Basic
  @Column(name = "trade_broker_id")
  public Integer getTradeBrokerId() {
    return tradeBrokerId;
  }

  public void setTradeBrokerId(Integer tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
  }

  @Basic
  @Column(name = "trade_broker_name")
  public String getTradeBrokerName() {
    return tradeBrokerName;
  }

  public void setTradeBrokerName(String tradeBrokerName) {
    this.tradeBrokerName = tradeBrokerName;
  }

  @Basic
  @Column(name = "update_by")
  public BigDecimal getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(BigDecimal updateBy) {
    this.updateBy = updateBy;
  }

  @Basic
  @Column(name = "update_date")
  public BigDecimal getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(BigDecimal updateDate) {
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

    TrdTradeBrokerEntity that = (TrdTradeBrokerEntity) o;

    if (id != that.id) {
      return false;
    }
    if (priority != that.priority) {
      return false;
    }
    if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) {
      return false;
    }
    if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) {
      return false;
    }
    if (tradeBrokerId != null ? !tradeBrokerId.equals(that.tradeBrokerId)
        : that.tradeBrokerId != null) {
      return false;
    }
    if (tradeBrokerName != null ? !tradeBrokerName.equals(that.tradeBrokerName)
        : that.tradeBrokerName != null) {
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
    result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + priority;
    result = 31 * result + (tradeBrokerId != null ? tradeBrokerId.hashCode() : 0);
    result = 31 * result + (tradeBrokerName != null ? tradeBrokerName.hashCode() : 0);
    result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    return result;
  }
}
