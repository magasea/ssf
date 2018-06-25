package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.PendingRecordStatusEnum;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import com.shellshellfish.aaas.userinfo.service.DataCollectionService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 六月 - 06
 */
@Service
public class CheckPendingRecordsServiceImpl implements CheckPendingRecordsService {

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  OrderRpcService orderRpcService;

  @Autowired
  DataCollectionService dataCollectionService;

  Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkPendingRecords() {
    Query query = new Query();
    query.addCriteria(Criteria.where("order_id").is("").orOperator(Criteria.where("order_id").is
        (null)).orOperator(Criteria.where("outside_order_id").is(""), Criteria.where
        ("outside_order_id").is(null)));
    List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
    Long currentTime = TradeUtil.getUTCTime();
    if(CollectionUtils.isEmpty(mongoPendingRecords)){
      return;
    }else{
      //只允许有一个pendingRecord fundCode
      for(MongoPendingRecords mongoPendingRecord: mongoPendingRecords){
        if ( currentTime - mongoPendingRecord.getCreatedDate() < 60*1000){
          logger.info("we ignore this pendingRecord because time is too short");
        }else if(mongoPendingRecord.getUserProdId() == null && mongoPendingRecord.getUserProdId() <=
            0){
          mongoTemplate.remove(mongoPendingRecord);
        } else {
          processPendingRecord(mongoPendingRecord);
        }
      }
    }

  }



  @Transactional
  @Override
  public void processPendingRecord(MongoPendingRecords mongoPendingRecords){
    List<TrdOrderDetail> trdOrderDetails = orderRpcService
        .getOrderDetailByUserProdIdAndFundCodeAndTrdType(mongoPendingRecords
        .getUserProdId(), mongoPendingRecords.getFundCode(), mongoPendingRecords.getTradeType());
//    mongoPendingRecords.getUserProdId();
    //because if there is a pendingRecord with the UserProdId and the FundCode without orderId, then
    //further operation on the same userProdId or the same prodId will be blocked
    //so the created time desc orderDetail's top one should be updated into the pendingRecord
    if(CollectionUtils.isEmpty(trdOrderDetails) && mongoPendingRecords.getUserProdId() > 0){
      mongoTemplate.remove(mongoPendingRecords);
      return ;
    }
    Collections.sort(trdOrderDetails, new Comparator<TrdOrderDetail>() {
      @Override
      public int compare(TrdOrderDetail o1, TrdOrderDetail o2) {
        return Math.toIntExact(o2.getCreateDate() - o1.getCreateDate() );
      }
    });
    if(Math.abs(trdOrderDetails.get(0).getCreateDate() - mongoPendingRecords.getCreatedDate()) >
        5*60*1000L || trdOrderDetails.get(0).getCreateDate() < mongoPendingRecords.getCreatedDate()){
      logger.error("this pendingRecord should be deleted because time lag between order, or the "
          + "sequence is reverse , so it is not related");
      mongoTemplate.remove(mongoPendingRecords);
      return ;
    }
    boolean shouldSave = false;
    if(trdOrderDetails.size() >= 1 && mongoPendingRecords.getTradeType() == trdOrderDetails.get
        (0).getTradeType() ){
      if(mongoPendingRecords.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation() &&
          (mongoPendingRecords.getTradeTargetSum() == null || mongoPendingRecords
              .getTradeTargetSum() == trdOrderDetails.get(0).getFundSum())){
        mongoPendingRecords.setOrderId(trdOrderDetails.get(0).getOrderId());
        mongoPendingRecords.setOutsideOrderId(trdOrderDetails.get(0).getOrderId()+ trdOrderDetails.get(0)
            .getId());
        shouldSave = true;
      }else if(mongoPendingRecords.getTradeType() == TrdOrderOpTypeEnum.REDEEM.getOperation() &&
          (mongoPendingRecords.getTradeTargetShare() == null || mongoPendingRecords
              .getTradeTargetShare() == trdOrderDetails.get(0).getFundNum())){
        mongoPendingRecords.setOrderId(trdOrderDetails.get(0).getOrderId());
        mongoPendingRecords.setOutsideOrderId(trdOrderDetails.get(0).getOrderId()+
            trdOrderDetails.get(0).getId());
        shouldSave = true;
      }
      if(shouldSave) {
        mongoPendingRecords.setLastModifiedDate(TradeUtil.getUTCTime());
        mongoTemplate.save(mongoPendingRecords, "ui_pending_records");
      }else{
        mongoTemplate.remove(mongoPendingRecords);
      }
    }
  }

  @Override
  public void recoverPendingRecordFromZZInfo(MongoUiTrdZZInfo mongoUiTrdZZInfo) {
    MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
    MyBeanUtils.mapEntityIntoDTO(mongoUiTrdZZInfo, mongoPendingRecordsPatch);
    mongoPendingRecordsPatch.setTradeTargetSum(mongoUiTrdZZInfo.getTradeTargetSum());
    mongoPendingRecordsPatch.setTradeTargetShare(mongoUiTrdZZInfo.getTradeTargetShare());
    if(mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation()){
      mongoPendingRecordsPatch.setTradeStatus(TrdOrderStatusEnum.CONFIRMED.getStatus());
    }else if(mongoUiTrdZZInfo.getTradeType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
      mongoPendingRecordsPatch.setTradeStatus(TrdOrderStatusEnum.SELLCONFIRMED.getStatus());
    }else{
      logger.error("Cannot handle such kind of ");
    }
    mongoPendingRecordsPatch.setLastModifiedDate(TradeUtil.getUTCTime());
    mongoPendingRecordsPatch.setOutsideOrderId(mongoUiTrdZZInfo.getOutSideOrderNo());
    mongoPendingRecordsPatch.setUserProdId(mongoUiTrdZZInfo.getUserProdId());
    mongoPendingRecordsPatch.setCreatedDate(mongoUiTrdZZInfo.getLastModifiedDate());
    mongoPendingRecordsPatch.setCreatedBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());

    mongoPendingRecordsPatch.setTradeConfirmShare(mongoUiTrdZZInfo.getTradeConfirmShare());
    mongoPendingRecordsPatch.setTradeConfirmSum(mongoUiTrdZZInfo.getTradeConfirmSum());
    mongoPendingRecordsPatch.setApplyDateStr(mongoUiTrdZZInfo.getApplyDate());
    checkMonetaryFundNavNavadj(mongoUiTrdZZInfo.getFundCode(), mongoUiTrdZZInfo.getApplyDate(),
        mongoPendingRecordsPatch);

    mongoPendingRecordsPatch.setApplyDateStr(mongoUiTrdZZInfo.getApplyDate());
    mongoTemplate.save(mongoPendingRecordsPatch,"ui_pending_records");




  }

  private void checkMonetaryFundNavNavadj(String fundCode, String applyDate, MongoPendingRecords
      mongoPendingRecordsPatch){
    if(MonetaryFundEnum.containsCode(fundCode)){
      Long navadj = getMoneyCodeNavAdjByDate(fundCode, applyDate);
      if(navadj < 0){
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
      }else{
        mongoPendingRecordsPatch.setApplyDateNavadj(navadj);
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
      }
    }else{
      mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
    }
  }
  @Override
  public void patchChkPendingRecordFromZZInfo() {
    //如果在目前交易系统里面有中证确认信息MongoUiTrdZZInfo ，而没有对应的MongoPendingRecords， 那么用中证确认信息去把对应的
    //mongoPendRecords 生成出来
    Query queryZZInfo = new Query();
    List<MongoUiTrdZZInfo> mongoUiTrdZZInfos =  mongoTemplate.findAll(MongoUiTrdZZInfo.class,
        "ui_trdzzinfo");
    for(MongoUiTrdZZInfo mongoUiTrdZZInfoItem: mongoUiTrdZZInfos){
      Query queryByZZInfo = new Query();
      queryByZZInfo.addCriteria(Criteria.where("user_prod_id").is(mongoUiTrdZZInfoItem
          .getUserProdId()).and("outsideOrderNo").is(mongoUiTrdZZInfoItem.getOutSideOrderNo()));
      List<MongoPendingRecords> mongoPendingRecordsListByZZInfo =
          mongoTemplate.find(queryByZZInfo, MongoPendingRecords.class, "ui_pending_records");
      if(CollectionUtils.isEmpty(mongoPendingRecordsListByZZInfo)){
        logger.info("need to recoverPendingRecordFromZZInfo for user_prod_id:{} and "
            + "outsideOrdernNo:{}",mongoUiTrdZZInfoItem.getUserProdId(), mongoUiTrdZZInfoItem
            .getOutSideOrderNo() );
        recoverPendingRecordFromZZInfo(mongoUiTrdZZInfoItem);
      }
    }
  }

  @Override
  public void checkUnhandledRecordWithNavadj() {
    //check all pendingRecords which trade status is coonfirmed but the pendingStatus is
    // unhandled and fundCode is of monentary funds
    Query query = new Query();
//    query.addCriteria(Criteria.where("trade_status").is(TrdOrderStatusEnum.CONFIRMED.getStatus())
//        .and("process_status").is(PendingRecordStatusEnum.NOTHANDLED.getStatus()).orOperator
//        (Criteria.where("trade_status").is(TrdOrderStatusEnum.SELLCONFIRMED.getStatus()).and
//            ("process_status").is(PendingRecordStatusEnum.NOTHANDLED.getStatus())));
    query.addCriteria(Criteria.where("process_status").is(PendingRecordStatusEnum.NOTHANDLED
        .getStatus()).orOperator(Criteria.where("trade_status").is(TrdOrderStatusEnum.SELLCONFIRMED
        .getStatus()), Criteria.where("trade_status").is(TrdOrderStatusEnum.CONFIRMED.getStatus()
    )));
    List<MongoPendingRecords> mongoPendingRecordsNeedHandle =
    mongoTemplate.find(query, MongoPendingRecords.class, "ui_pending_records");

    for(MongoPendingRecords mongoPendingRecordsItem: mongoPendingRecordsNeedHandle){
      if(MonetaryFundEnum.containsCode(mongoPendingRecordsItem.getFundCode())){
        checkMonetaryFundNavNavadj(mongoPendingRecordsItem.getFundCode(), mongoPendingRecordsItem
            .getApplyDateStr(), mongoPendingRecordsItem);
        mongoTemplate.save(mongoPendingRecordsItem, "ui_pending_records");
      }else{
        logger.error("There is a pendRecord with fundCode:{} tradeStatus:{} in "
            + "unhandledStatus:{}", mongoPendingRecordsItem.getFundCode(),
            mongoPendingRecordsItem.getTradeStatus(), mongoPendingRecordsItem.getProcessStatus());
      }
    }
  }

  private Long getMoneyCodeNavAdjByDate(String fundCode, String applyDate){

    if(StringUtils.isEmpty(applyDate) || applyDate.equals("-1")){
      logger.error("cannot make navadj without applyDate:{}", applyDate);
      return -1L;
    }

    if(!applyDate.contains("-")){
      StringBuilder sb = new StringBuilder();
      sb.append(applyDate.substring(0,4)).append("-").append(applyDate.substring(4,6)).append
          ("-").append(applyDate.substring(6,8));
      applyDate = sb.toString();
    }

    if(MonetaryFundEnum.containsCode(fundCode)){
      String beginDate = TradeUtil.getDayBefore(applyDate, 1);
      List<String> codes = new ArrayList<>();
      codes.add(fundCode);
      List<DCDailyFunds> dcDailyFunds = dataCollectionService.getFundDataOfDay(codes,
          beginDate, applyDate);
      if(!CollectionUtils.isEmpty(dcDailyFunds)){
        Double navadj = dcDailyFunds.get(0).getNavadj();
        return TradeUtil.getLongNumWithMul1000000(navadj);
      }
    }
    return -1L;
  }



}
