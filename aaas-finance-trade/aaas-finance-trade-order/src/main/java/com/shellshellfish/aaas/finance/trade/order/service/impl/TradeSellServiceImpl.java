package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.datamanager.FundInfo;
import com.shellshellfish.aaas.datamanager.FundsInfoServiceGrpc;
import com.shellshellfish.aaas.datamanager.FundsInfoServiceGrpc.FundsInfoServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderTypeEnum;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */
@Service
public class TradeSellServiceImpl implements TradeSellService {
  Logger logger = LoggerFactory.getLogger(TradeSellServiceImpl.class);

  @Autowired
  ManagedChannel managedDMChannel;

  FundsInfoServiceFutureStub fundsInfoFutureStub;

  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @PostConstruct
  void init(){
    fundsInfoFutureStub = FundsInfoServiceGrpc.newFutureStub(managedDMChannel);
  }

  @Override
  public TrdOrder sellProduct(ProdSellPageDTO prodSellPageDTO)
      throws ExecutionException, InterruptedException {
    //first : get price of funds , this
    com.shellshellfish.aaas.datamanager.FundCodes.Builder requestBuilder = com.shellshellfish
        .aaas.datamanager.FundCodes.newBuilder();
    for(ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      requestBuilder.addFundCode(prodDtlSellPageDTO.getFundCode());
    }

    List<FundInfo> fundInfoList =fundsInfoFutureStub.getFundsPrice
        (requestBuilder.build()).get().getFundInfoList();
    fundInfoList.get(0).getNavunit();
    Map<String, Integer> fundNavunits = new HashMap<>();
    for(FundInfo fundInfo: fundInfoList){
      fundNavunits.put(fundInfo.getFundCode(), fundInfo.getNavunit());
    }
    List<ProdDtlSellDTO> prodDtlSellDTOList = new ArrayList<>();
    for(ProdDtlSellPageDTO prodDtlSellDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      ProdDtlSellDTO prodDtlSellDTOTgt = new ProdDtlSellDTO();
      BeanUtils.copyProperties(prodDtlSellDTO, prodDtlSellDTOTgt);
      //Todo:此次需要加单元测试验证正确性
      int quantity = prodDtlSellDTO.getTargetSellAmount().divide(BigDecimal.valueOf(fundNavunits.get
          (prodDtlSellDTO.getFundCode())).divide(BigDecimal.valueOf(100L))).intValue();
      prodDtlSellDTOTgt.setFundQuantity(quantity);
      prodDtlSellDTOList.add(prodDtlSellDTOTgt);
    }
    ProdSellDTO prodSellDTO = new ProdSellDTO();
    BeanUtils.copyProperties(prodSellPageDTO, prodSellDTO);
    prodSellDTO.setProdDtlSellDTOList(prodDtlSellDTOList);
    TrdOrder result = generateOrderInfo4Sell(prodSellDTO);
    if(result == null){
      logger.error("failed to generate order info for sell information");
    }
    broadcastMessageProducer.sendSellMessages(prodSellDTO);
    return result;
  }
  private TrdOrder generateOrderInfo4Sell(ProdSellDTO prodSellDTO){
    TrdOrder trdOrder = new TrdOrder();
    try {
      List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository
          .findByUserId(prodSellDTO.getUserId());
      int trdBrokerId = trdBrokerUsers.get(0).getTradeBrokerId().intValue();
      List<TrdOrder> trdOrders = trdOrderRepository.findByUserProdId(prodSellDTO.getUserProdId());
      if (CollectionUtils.isEmpty(trdOrders)) {
        logger.error("failed to find corresponding order for sell by userProdId:");
        return null;
      }
      String orderId = TradeUtil.generateOrderId(Integer.valueOf(trdOrders.get(0).getBankCardNum()
          .substring(0, 6)), trdBrokerId);
      trdOrder.setUserId(prodSellDTO.getUserId());
      trdOrder.setOrderId(orderId);
      trdOrder.setOrderType(TrdOrderTypeEnum.SELL.ordinal());
      trdOrder.setUserProdId(prodSellDTO.getUserProdId());
      trdOrder.setCreateBy(prodSellDTO.getUserId());
      trdOrder.setCreateDate(TradeUtil.getUTCTime());
      trdOrder.setUpdateBy(prodSellDTO.getUserId());
      trdOrder.setPayAmount(prodSellDTO.getSellNum());
      trdOrder.setOrderStatus(TrdOrderStatusEnum.WAITSELL.ordinal());
      trdOrderRepository.save(trdOrder);
      for( ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
        //生成赎回子订单信息
        TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
        trdOrderDetail.setUserId(prodSellDTO.getUserId());
        trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setUserProdId(prodSellDTO.getUserProdId());
        trdOrderDetail.setFundNum(prodDtlSellDTO.getFundQuantity());
        trdOrderDetail.setCreateBy(prodSellDTO.getUserId());
        trdOrderDetail.setTradeType(TrdOrderOpTypeEnum.SELL.getOperation());
        trdOrderDetail.setUpdateBy(prodSellDTO.getUserId());
        trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setBuysellDate(TradeUtil.getUTCTime());
        trdOrderDetail.setFundCode(prodDtlSellDTO.getFundCode());
        trdOrderDetail.setFundNum(prodDtlSellDTO.getFundQuantity());
        trdOrderDetail.setFundMoneyQuantity(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
            .getTargetSellAmount()));
        trdOrderDetail.setOrderId(orderId);
        trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.WAITSELL.getStatus());
        trdOrderDetailRepository.save(trdOrderDetail);
        prodDtlSellDTO.setOrderDetailId(trdOrderDetail.getId());
      }
      return trdOrder;
    }catch (Exception ex){
      ex.printStackTrace();
      logger.error(ex.getMessage());
      return null;
    }
  }
}
