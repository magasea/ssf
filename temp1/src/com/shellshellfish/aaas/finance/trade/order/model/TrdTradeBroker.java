package com.shellshellfish.aaas.finance.trade.order.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 22
 */

@Entity
@Table(name = "trd_trade_broker", schema = "ssftrdorder", catalog = "")
public class TrdTradeBroker {

  private long id;
  private Long createBy;
  private Long createDate;
  private int priority;
  private String tradeBrokerCode;
  private String tradeBrokerName;
  private Long updateBy;
  private Long updateDate;
  private Integer tradeBrokerId;

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
  @Column(name = "priority")
  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Basic
  @Column(name = "trade_broker_code")
  public String getTradeBrokerCode() {
    return tradeBrokerCode;
  }

  public void setTradeBrokerCode(String tradeBrokerCode) {
    this.tradeBrokerCode = tradeBrokerCode;
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
  @Column(name = "trade_broker_id")
  public Integer getTradeBrokerId() {
    return tradeBrokerId;
  }

  public void setTradeBrokerId(Integer tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TrdTradeBroker that = (TrdTradeBroker) o;

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
    if (tradeBrokerCode != null ? !tradeBrokerCode.equals(that.tradeBrokerCode)
        : that.tradeBrokerCode != null) {
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
    if (tradeBrokerId != null ? !tradeBrokerId.equals(that.tradeBrokerId)
        : that.tradeBrokerId != null) {
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
    result = 31 * result + (tradeBrokerCode != null ? tradeBrokerCode.hashCode() : 0);
    result = 31 * result + (tradeBrokerName != null ? tradeBrokerName.hashCode() : 0);
    result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    result = 31 * result + (tradeBrokerId != null ? tradeBrokerId.hashCode() : 0);
    return result;
  }
}
