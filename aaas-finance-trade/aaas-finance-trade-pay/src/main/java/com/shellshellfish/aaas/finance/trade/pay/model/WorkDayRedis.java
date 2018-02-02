package com.shellshellfish.aaas.finance.trade.pay.model;

import java.io.Serializable;

/**
 * Created by chenwei on 2018- 二月 - 01
 */

public class WorkDayRedis implements Serializable{
  String workDay;
  Long create_date;
  Long update_date;

  public String getWorkDay() {
    return workDay;
  }

  public void setWorkDay(String workDay) {
    this.workDay = workDay;
  }

  public Long getCreate_date() {
    return create_date;
  }

  public void setCreate_date(Long create_date) {
    this.create_date = create_date;
  }

  public Long getUpdate_date() {
    return update_date;
  }

  public void setUpdate_date(Long update_date) {
    this.update_date = update_date;
  }
}
