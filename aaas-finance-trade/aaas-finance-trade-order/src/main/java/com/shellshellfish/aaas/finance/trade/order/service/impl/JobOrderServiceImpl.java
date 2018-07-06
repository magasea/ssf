package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.JobOrderService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2018- 六月 - 13
 */
@Service
public class JobOrderServiceImpl implements JobOrderService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired
  PayService payService;

  @Autowired
  UserInfoService userInfoService;

  /**
   * 检查订单创立后状态一直没有改变的数据，用orderId+orderDetailId fundCode 以及applySerial查询 如果查到不一致: 1. trd_pay_flow
   * 有记录， 那么直接把该记录拿来更新 trd_order_detail 2. trd_pay_flow 没有记录，那么直接用trd_order_detail
   * 去试图生成交易trd_pay_flow如果中证已经有交易 记录，那么调用查询接口获取最新的状态，用最新的trd_pay_flow状态来更新trd_order_detail 3.
   * trd_pay_flow 没有记录，调中证接口查询也没有对应的交易记录，那么看order_detail的create时间， 如果当前时间和create时间在同一个交易日，
   * 而且时间已经超过1小时，
   *
   * 否则直接标记trd_order_detail状态为失败
   */
  @Override
  public void patchOrderWithPay() {
    int pageSize = 100;

    Pageable pageable = new PageRequest(0, pageSize);
    Page<TrdOrderDetail> trdOrderList = trdOrderDetailRepository.findPendingOrderinfo(pageable);
    int totalPages = trdOrderList.getTotalPages();
    processPageOrderDetails(trdOrderList);
    for (int pageStart = 1; pageStart < totalPages; pageStart++) {
      pageable = new PageRequest(pageStart, pageSize);
      trdOrderList = trdOrderDetailRepository.findPendingOrderinfo(pageable);
      processPageOrderDetails(trdOrderList);
    }


  }
  private void processPageOrderDetails(Page<TrdOrderDetail> trdOrderDetails){
    for(TrdOrderDetail item: trdOrderDetails){
      TrdPayFlow trdPayFlow = payService.patchOrderToPay(item);
      if(trdPayFlow == null){
        continue;
      }
      if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.REDEEMFAILED.getStatus() || trdPayFlow
          .getTrdStatus() == TrdOrderStatusEnum.FAILED.getStatus()){
        logger.error("the trdPayFlow is not exist in payFlow table and not in zz system, so we "
            + "mark it as failed");
        item.setOrderDetailStatus(trdPayFlow.getTrdStatus());
        trdOrderDetailRepository.save(item);
      }
    }
  }


  /**
   * 检查订单创立后状态已经为确认而pendingRecords里面没有记录下来的数据，用orderId+orderDetailId fundCode 以及applySerial查询
   * 如果查到不一致: 1. trd_pay_flow 有记录， 那么直接把该记录拿来更新 trd_order_detail 2. trd_pay_flow 没有记录，那么直接用trd_order_detail
   * 去试图生成交易trd_pay_flow如果中证已经有交易 记录，那么调用查询接口获取最新的状态，用最新的trd_pay_flow状态来更新trd_order_detail 3.
   * trd_pay_flow 没有记录，调中证接口查询也没有对应的交易记录，那么看order_detail的create时间， 如果当前时间和create时间在同一个交易日，
   * 而且时间已经超过1小时，
   *
   * 否则直接标记trd_order_detail状态为失败
   */
  @Override
  public void patchPendingRecordWithOrder() {
    int pageSize = 100;
    Pageable pageable = new PageRequest(0, pageSize);
    Page<TrdOrderDetail> trdOrderList = trdOrderDetailRepository.findConfirmedOrderinfo(pageable);
    int totalPages = trdOrderList.getTotalPages();
    processPageOutsideOrderIds(trdOrderList);
    for (int pageStart = 1; pageStart < totalPages; pageStart++) {
      pageable = new PageRequest(pageStart, pageSize);
      trdOrderList = trdOrderDetailRepository.findConfirmedOrderinfo(pageable);
      processPageOutsideOrderIds(trdOrderList);
    }

  }

  private void processPageOutsideOrderIds(Page<TrdOrderDetail> trdOrderDetails){
    List<String> outsideOrderIds = new ArrayList<>();
    trdOrderDetails.forEach(
        item->outsideOrderIds.add(item.getOrderId()+item.getId())
    );
    List<String> needHandleIds =  userInfoService.getNeedHandleOutsideOrderIds(outsideOrderIds);
    if(needHandleIds == null || CollectionUtils.isEmpty(needHandleIds)){
      return;
    }
    Set<String> outsideIdsWaitCheck = new HashSet<>();
    needHandleIds.forEach(
        item -> outsideIdsWaitCheck.add(item)
    );

    for(TrdOrderDetail item: trdOrderDetails){
      if(!outsideIdsWaitCheck.contains(item.getOrderId()+item.getId())){
        continue;
      }
      if(item.getOrderDetailStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()){
        item.setOrderDetailStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      }else if(item.getOrderDetailStatus() == TrdOrderStatusEnum.SELLCONFIRMED.getStatus()){
        item.setOrderDetailStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
      }
      TrdPayFlow trdPayFlow = payService.patchOrderToPay(item);
      if(trdPayFlow == null){
        continue;
      }
      if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.REDEEMFAILED.getStatus() || trdPayFlow
          .getTrdStatus() == TrdOrderStatusEnum.FAILED.getStatus()){
        logger.error("the trdPayFlow is not exist in payFlow table and not in zz system, so we "
            + "mark it as failed");
        item.setOrderDetailStatus(trdPayFlow.getTrdStatus());
        trdOrderDetailRepository.save(item);
      }
    }
  }
}
