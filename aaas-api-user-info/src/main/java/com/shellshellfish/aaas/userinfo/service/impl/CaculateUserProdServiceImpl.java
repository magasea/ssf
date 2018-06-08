package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.PendingRecordStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.userinfo.model.dao.MongoCaculateBase;
import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CaculateUserProdService;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by developer4 on 2018- 六月 - 08
 */
@Service
public class CaculateUserProdServiceImpl implements CaculateUserProdService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  UiProductRepo uiProductRepo;

  @Autowired
  UiProductDetailRepo uiProductDetailRepo;

  /**
   * 查询PendingRecords表里面所有的已经处理完的记录， 交易状态为购买确认和赎回确认的列入
   * 到表MongoCaculateBase表里面
   * @param userProdId
   * @param fundCode
   * @return
   */
  @Override
  public boolean updateCaculateBase(Long userProdId, String fundCode) {
    Query query = new Query();
    query.addCriteria(Criteria.where("user_prod_id").is(userProdId).and("fund_code").is(fundCode)
        .and("process_status").is(PendingRecordStatusEnum.HANDLED.getStatus()).and
            ("trade_status").ne(TrdOrderStatusEnum.FAILED.getStatus()));
    List<MongoPendingRecords> mongoPendingRecords =
    mongoTemplate.find(query, MongoPendingRecords.class,"ui_pending_records");
    for(MongoPendingRecords item: mongoPendingRecords){
      if(item.getTradeStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus() || item.getTradeStatus() == TrdOrderStatusEnum.SELLCONFIRMED.getStatus()){
        Query querySub = new Query();
        querySub.addCriteria(Criteria.where("user_prod_id").is(userProdId).and("fund_code").is
            (fundCode).and("outside_order_id").is(item.getOutsideOrderId()));
        List<MongoCaculateBase> mongoCaculateBases =
            mongoTemplate.find(querySub, MongoCaculateBase.class,"ui_calc_base");
        if(CollectionUtils.isEmpty(mongoCaculateBases)){
          MongoCaculateBase mongoCaculateBase = new MongoCaculateBase();
          MyBeanUtils.mapEntityIntoDTO(item, mongoCaculateBase);
          mongoCaculateBase.setOutsideOrderId(item.getOutsideOrderId());
          mongoCaculateBase.setTradeConfirmShare(item.getTradeConfirmShare());
          mongoCaculateBase.setTradeConfirmSum(item.getTradeConfirmSum());
          mongoTemplate.save(mongoCaculateBase);
        }else{
          logger.error("user_prod_id:{} fund_code:{} outside_order_id:{} have more than 1 records"
                  + " in ui_calc_base",userProdId, fundCode, item.getOutsideOrderId());
          if(mongoCaculateBases.size() > 1){
            mongoTemplate.remove(querySub, "ui_calc_base");
          }
          MyBeanUtils.mapEntityIntoDTO(item, mongoCaculateBases.get(0));
          mongoCaculateBases.get(0).setTradeConfirmShare(item.getTradeConfirmShare());
          mongoCaculateBases.get(0).setTradeConfirmSum(item.getTradeConfirmSum());
          //注意凡是pendingProcessStatus状态为Handled的话必须Navadj已经取到
          mongoCaculateBases.get(0).setApplyDateNavadj(item.getApplyDateNavadj());
          mongoTemplate.save(mongoCaculateBases.get(0));
        }
      }
    }
    return false;
  }



  @Override
  public boolean caculateQuantityByUserProdIdAndFundCode(Long userProdId, String fundCode) {
    return false;
  }

  @Override
  public void updateCaculateBase() {
    List<Long> prodIds = uiProductRepo.getAllId();
    for(Long userProdId: prodIds){
      List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(userProdId);
      for(UiProductDetail uiProductDetail: uiProductDetails){
        updateCaculateBase(userProdId, uiProductDetail.getFundCode());
      }
    }
  }
}
