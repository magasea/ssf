package com.shellshellfish.aaas.finance.trade.pay.service.impl;


import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.ZZBizOpEnum;
import com.shellshellfish.aaas.common.enums.ZZKKStatusEnum;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.ZZStatsToOrdStatsUtils;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.ZZBuyFund;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by chenwei on 2018- 一月 - 09
 */
@Component
@Scope("prototype")
public class ZZTaskBuyFunds implements Callable<TrdOrderDetail> {

  private static final Logger logger = LoggerFactory.getLogger(ZZTaskBuyFunds.class);

  private ZZBuyFund request;


  public ZZTaskBuyFunds(ZZBuyFund param) {
    this.request = param;
  }

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Autowired
  TrdPayFlowRepository trdPayFlowRepository;

  @Autowired
  BroadcastMessageProducers broadcastMessageProducers;

  @Override
  public TrdOrderDetail call() throws Exception {
    logger.debug("Thread started [" + request + "]");
    return doWork();
  }

  private TrdOrderDetail doWork() {
    BuyFundResult buyFundResult = null;
    TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
    trdOrderDetail.setUserId(request.getUserId());
    trdOrderDetail.setPayAmount(TradeUtil.getLongNumWithMul100(request.getApplySum()));
    trdOrderDetail.setId(Long.valueOf(request.getOutsideOrderNo()));
    try {
        buyFundResult = fundTradeApiService.buyFund(""+TradeUtil.getZZOpenId(request.getUserPid()
        ), request.getTradeAcco(), request.getApplySum(), request.getOutsideOrderNo(), request.getFundCode());
        //把交易流水入库
      int kkStat = Integer.parseInt(buyFundResult.getKkstat());
      String kkStatName = ZZKKStatusEnum.getByStatus(kkStat).getComment();
      logger.info("orderId:"+ request.getTrdOrderDetail().getId() +" kkStat:"+ kkStat + " "
          + "kkStatName:" + kkStatName);
      TrdOrderStatusEnum trdOrderStatusEnum = ZZStatsToOrdStatsUtils.getOrdStatByZZKKStatus(ZZKKStatusEnum
          .getByStatus((kkStat)), TrdOrderOpTypeEnum.BUY);
      trdOrderDetail.setOrderStatus(TrdOrderStatusEnum.FAILED.getStatus());
      com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow trdPayFlow = new com
          .shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow();
      BeanUtils.copyProperties(request.getTrdOrderDetail(), trdPayFlow);
      trdPayFlow.setUpdateBy(request.getUserId());
      trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
      trdPayFlow.setTrdType(ZZBizOpEnum.BUY.getOptVal());
      trdPayFlow.setTrdDate(TradeUtil.getUTCTime());
      trdPayFlow.setUserId(request.getUserId());
      trdPayFlow.setApplySerial(buyFundResult.getApplySerial());
      trdPayFlow.setCreateBy(request.getUserId());
      trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
      trdPayFlow.setUserProdId(request.getTrdOrderDetail().getUserProdId());
      trdPayFlow.setTradeAcco(request.getTradeAcco());
      trdPayFlow.setFundCode(request.getFundCode());
      trdPayFlow.setOrderDetailId(request.getTrdOrderDetail().getId());
      trdPayFlow.setBankCardNum(request.getTrdOrderDetail().getBankCardNum());
      trdPayFlow.setOutsideOrderno(buyFundResult.getOutsideOrderNo());
      trdPayFlow.setTrdbkerStatusName(kkStatName);
      trdPayFlow.setTrdbkerStatusCode(kkStat);
      trdPayFlow.setTradeBrokeId(request.getTrdBrokerId());
      //注意外面接口用BigDecimal表示金额，入库都用long精确到分
      trdPayFlow.setTrdMoneyAmount(TradeUtil.getLongNumWithMul100(request.getApplySum()));
      trdPayFlow.setTrdStatus(trdOrderStatusEnum.getStatus());
      trdPayFlowRepository.save(trdPayFlow);
      com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
          .shellshellfish.aaas.common.message.order.TrdPayFlow();
      BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
      notifyPayMsg( trdPayFlowMsg);
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.FAILED.getStatus());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.FAILED.getStatus());
    }
    return trdOrderDetail;
  }

  private com.shellshellfish.aaas.common.message.order.TrdPayFlow notifyPayMsg(com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlow) {
      logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());
      broadcastMessageProducers.sendMessage(trdPayFlow);
      return trdPayFlow;
  }

  private String createUUID() {
    UUID id = UUID.randomUUID();
    return id.toString();
  }

}