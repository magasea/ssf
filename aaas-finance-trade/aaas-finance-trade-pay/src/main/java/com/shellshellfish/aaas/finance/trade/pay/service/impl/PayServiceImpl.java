package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.DateUtil;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PayServiceImpl implements PayService{

  Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

  @Autowired
  BroadcastMessageProducers broadcastMessageProducers;

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Autowired
  TrdPayFlowRepository trdPayFlowRepository;


  @Override
  public PayDto payOrder(PayDto payDto) throws Exception {
    String trdAcco = payDto.getTrdAccount();
    List<TrdOrderDetail> orderDetailList = payDto.getOrderDetailList();
    List<Exception > errs = new ArrayList<>();
    for(TrdOrderDetail trdOrderDetail: orderDetailList){
      logger.info("payOrder fundCode:"+trdOrderDetail.getFundCode());
      //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
      BigDecimal payAmount = TradeUtil.getBigDecimalNumWithDiv100(trdOrderDetail.getFundMoneyQuantity());
      //TODO: replace userId with userUuid
      TrdPayFlow trdPayFlow = new TrdPayFlow();
      trdPayFlow.setCreateDate(DateUtil.getCurrentDateInLong());
      trdPayFlow.setCreateBy(0L);
      trdPayFlow.setPayAmount(trdOrderDetail.getFundMoneyQuantity());
      trdPayFlow.setPayStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
      BuyFundResult fundResult = null;
      try {
        fundResult = fundTradeApiService.buyFund(payDto.getUserUuid()
            , trdAcco, payAmount, String.valueOf(trdOrderDetail.getId()),
            trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }
      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setPayStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        notifyPay(trdPayFlowResult);
      }
    }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return payDto;
  }

  @Override
  public TrdPayFlow notifyPay(TrdPayFlow trdPayFlow) {
    logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());
    com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
        .shellshellfish.aaas.common.message.order.TrdPayFlow();
    BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
    broadcastMessageProducers.sendMessage(trdPayFlowMsg);
    return trdPayFlow;
  }
}
