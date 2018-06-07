package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;

/**
 * Created by chenwei on 2018- 六月 - 06
 */

public interface CheckPendingRecordsService {

  /**
   * PendingRecord 记录应该有orderId, outsideOrderId
   * 如果没有的话查一下orderService获取对应的orderDetail去补数据
   */
  public void  checkPendingRecords();

  /**
   *
   * @param mongoPendingRecords
   */
  public void processPendingRecord(MongoPendingRecords mongoPendingRecords);
}
