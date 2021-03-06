package com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 21
 */

@Entity
@Table(name = "trd_trade_broker", schema = "ssftrdpay", catalog = "")
public class TrdTradeBroker {

  private long id;
  private String tradeBrokerName;
  private int tradeBrokerId;
  private int priority;
  private long createBy;
  private long createDate;
  private long updateBy;
  private long updateDate;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
  @Column(name = "trade_broker_id")
  public int getTradeBrokerId() {
    return tradeBrokerId;
  }

  public void setTradeBrokerId(int tradeBrokerId) {
    this.tradeBrokerId = tradeBrokerId;
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
    if (tradeBrokerId != that.tradeBrokerId) {
      return false;
    }
    if (priority != that.priority) {
      return false;
    }
    if (createBy != that.createBy) {
      return false;
    }
    if (createDate != that.createDate) {
      return false;
    }
    if (updateBy != that.updateBy) {
      return false;
    }
    if (updateDate != that.updateDate) {
      return false;
    }
    if (tradeBrokerName != null ? !tradeBrokerName.equals(that.tradeBrokerName)
        : that.tradeBrokerName != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (tradeBrokerName != null ? tradeBrokerName.hashCode() : 0);
    result = 31 * result + tradeBrokerId;
    result = 31 * result + priority;
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    return result;
  }
}
