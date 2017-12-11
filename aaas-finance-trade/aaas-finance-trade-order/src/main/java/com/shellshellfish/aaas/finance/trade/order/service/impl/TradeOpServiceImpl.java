package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderStatusEnum;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBroker;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import com.shellshellfish.aaas.finance.trade.order.util.TradeUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeOpServiceImpl implements TradeOpService {

  Logger logger = LoggerFactory.getLogger(TradeOpServiceImpl.class);

  @Autowired
  FinanceProdInfoService financeProdInfoService;

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdBrokderRepository trdBrokderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;

  @Override
  public TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdBuyInfo)
      throws ExecutionException, InterruptedException {
    ProductBaseInfo productBaseInfo = new ProductBaseInfo();
    BeanUtils.copyProperties(financeProdBuyInfo, productBaseInfo);
    List<ProductMakeUpInfo> productMakeUpInfos =  financeProdInfoService.getFinanceProdMakeUpInfo
        (productBaseInfo);

    return genOrderFromBuyInfoAndProdMakeUpInfo(financeProdBuyInfo, productMakeUpInfos);

  }

  @Transactional
  TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos){
    //generate order
    TrdTradeBroker trdTradeBroker = trdBrokderRepository.findOne(1L);
    String orderId = TradeUtil.generateOrderId(Integer.getInteger(financeProdBuyInfo.getBankAcc()
            .substring(0,6)),trdTradeBroker.getTradeBrokerId());
    TrdOrder trdOrder = new TrdOrder();
    trdOrder.setBankCardNum(financeProdBuyInfo.getBankAcc());
    trdOrder.setOrderDate(TradeUtil.getUTCTime());
    trdOrder.setCreateDate(TradeUtil.getUTCTime());
    trdOrder.setOrderType(financeProdBuyInfo.getOrderType());
    trdOrder.setProdId(financeProdBuyInfo.getProdId());
    trdOrder.setOrderStatus(TrdOrderStatusEnum.WAITCONFIRM.ordinal());
    trdOrderRepository.save(trdOrder);

    //generate sub order for each funds
    for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      trdOrderDetail.setOrderId(trdOrder.getOrderId());
      //规定基金占比用百分比并且精确万分之一
      BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
          (BigDecimal.valueOf(10000));
      trdOrderDetail.setFundQuantity(fundRatio.multiply(BigDecimal.valueOf(financeProdBuyInfo
          .getMoney())).toBigInteger().longValue());
      trdOrderDetail.setBoughtDate(TradeUtil.getUTCTime());
      trdOrderDetail.setCreateBy(0L);
      trdOrderDetail.setFundCode(productMakeUpInfo.getFundCode());
      trdOrderDetail.setProdId(trdOrder.getProdId());
      trdOrderDetail.setTradeType(trdOrder.getOrderType());
      trdOrderDetailRepository.save(trdOrderDetail);
      TrdOrderPay trdOrderPay = new TrdOrderPay();
      BeanUtils.copyProperties(trdOrderDetail, trdOrderPay);
      broadcastMessageProducer.sendMessages(trdOrderPay);
    }
    return trdOrder;
  }
}
