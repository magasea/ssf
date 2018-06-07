package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
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

/**
 * Created by chenwei on 2018- 六月 - 06
 */
@Service
public class CheckPendingRecordsServiceImpl implements CheckPendingRecordsService {

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  OrderRpcService orderRpcService;

  Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkPendingRecords() {
    Query query = new Query();
    query.addCriteria(Criteria.where("order_id").is(null).orOperator(Criteria.where("outside_order_id").is(null)));
    List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
    Long currentTime = TradeUtil.getUTCTime();
    if(CollectionUtils.isEmpty(mongoPendingRecords)){
      return;
    }else{
      for(MongoPendingRecords mongoPendingRecord: mongoPendingRecords){
        if ( currentTime - mongoPendingRecord.getCreatedDate() < 60*1000){
          logger.info("we ignore this pendingRecord because time is too short");
        }else{
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
    Collections.sort(trdOrderDetails, new Comparator<TrdOrderDetail>() {
      @Override
      public int compare(TrdOrderDetail o1, TrdOrderDetail o2) {
        return Math.toIntExact(o2.getCreateDate() - o1.getCreateDate() );
      }
    });
    if(trdOrderDetails.size() >= 1 && mongoPendingRecords.getTradeType() == trdOrderDetails.get
        (0).getTradeType() ){
      if(mongoPendingRecords.getTradeType() == TrdOrderOpTypeEnum.BUY.getOperation() &&
          (mongoPendingRecords.getTradeTargetSum() == null || mongoPendingRecords
              .getTradeTargetSum() == trdOrderDetails.get(0).getFundSum())){
        mongoPendingRecords.setOrderId(trdOrderDetails.get(0).getOrderId());
        mongoPendingRecords.setOutsideOrderId(trdOrderDetails.get(0).getOrderId()+ trdOrderDetails.get(0)
            .getId());
      }else if(mongoPendingRecords.getTradeType() == TrdOrderOpTypeEnum.REDEEM.getOperation() &&
          (mongoPendingRecords.getTradeTargetShare() == null || mongoPendingRecords
              .getTradeTargetShare() == trdOrderDetails.get(0).getFundNum())){
        mongoPendingRecords.setOrderId(trdOrderDetails.get(0).getOrderId());
        mongoPendingRecords.setOutsideOrderId(trdOrderDetails.get(0).getOrderId()+ trdOrderDetails.get(0));

      }
      mongoPendingRecords.setLastModifiedDate(TradeUtil.getUTCTime());
      mongoTemplate.save(mongoPendingRecords, "ui_pending_records");
    }
  }

}
