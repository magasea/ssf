package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;

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


  /**
   *
   * @param mongoPendingRecords
   */
  public void processMiddleStatePendingRecord(MongoPendingRecords mongoPendingRecords);

  /**
   * 如果在目前交易系统里面有中证确认信息MongoUiTrdZZInfo ，而没有对应的MongoPendingRecords， 那么用中证确认信息去把对应的
   * mongoPendRecords 生成出来
   * @param mongoUiTrdZZInfo
   */
  public void recoverPendingRecordFromZZInfo(MongoUiTrdZZInfo mongoUiTrdZZInfo);

  /**
   * 如果在目前交易系统里面有中证确认信息MongoUiTrdZZInfo ，而没有对应的MongoPendingRecords， 那么用中证确认信息去把对应的
   * mongoPendRecords 生成出来
   */
  public void patchChkPendingRecordFromZZInfo();

  /**
   * 如果某些情況下applyDate的货币基金的navadj没有取得导致pendingRecord的状态一直处于unhandled，那么用定时任务去获取，周期每12个小时一次
   */
  public void checkUnhandledRecordWithNavadj();

  /**
   * 把交易失败的订单信息补入到trdLog里面
   */
  public void patchFailedOrderInfoToTrdLog();


}
