package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.ZZBuyFund;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 一月 - 09
 */
@Service
public class MultiThreadTaskHandler {
  Logger logger = LoggerFactory.getLogger(MultiThreadTaskHandler.class);

  @Value("${multithread.pool.size}")
  private int poolSize;

  public List<TrdOrderDetail> processBuyOrders(List<ZZBuyFund> zzBuyFunds){
    ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
    List<ZZTaskBuyFunds> zzTaskBuyFunds = new ArrayList<>(zzBuyFunds.size());
    for(ZZBuyFund zzBuyFund: zzBuyFunds){
      zzTaskBuyFunds.add(new ZZTaskBuyFunds(zzBuyFund));
    }
    List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
    try {
      List<Future<TrdOrderDetail>> results = executorService.invokeAll(zzTaskBuyFunds);
      for (Future<TrdOrderDetail> result : results) {
        TrdOrderDetail trdOrderDetail = result.get(10000, TimeUnit.MILLISECONDS);
        trdOrderDetails.add(trdOrderDetail);
        logger.debug("Thread reply results [" + result + "]");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (ExecutionException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (TimeoutException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return trdOrderDetails;
  }


}
