package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.JobOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by developer4 on 2018- 六月 - 13
 */
@Service
public class JobOrderServiceImpl implements JobOrderService {

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired


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

    }
  }

}
