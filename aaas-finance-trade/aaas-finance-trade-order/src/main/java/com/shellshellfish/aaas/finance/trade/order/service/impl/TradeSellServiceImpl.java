package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceFutureStub;
import com.shellshellfish.aaas.datacollect.FundCodes;
import com.shellshellfish.aaas.datacollect.FundInfo;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderTypeEnum;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  ManagedChannel managedDCChannel;

  DataCollectionServiceFutureStub dataCollectionServiceFutureStub;

  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired
  UserInfoService userInfoService;

  @PostConstruct
  void init(){
    dataCollectionServiceFutureStub = DataCollectionServiceGrpc.newFutureStub(managedDCChannel);
  }




  @Override
  public TrdOrder sellProduct(ProdSellPageDTO prodSellPageDTO)
      throws ExecutionException, InterruptedException {
    //first : get price of funds , this
    FundCodes.Builder requestBuilder = FundCodes.newBuilder();
    for(ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      requestBuilder.addFundCode(prodDtlSellPageDTO.getFundCode());
    }


    com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
        userInfoService.getUserBankInfo(prodSellPageDTO.getUserId());


    List<FundInfo> fundInfoList =dataCollectionServiceFutureStub.getFundsPrice
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
//      BigDecimal quantity = prodDtlSellDTO.getTargetSellAmount().multiply(BigDecimal.valueOf(100)).divide
//          (BigDecimal.valueOf(fundNavunits.get(prodDtlSellDTO.getFundCode())));
      BigDecimal sellTargetMoneyInCents = prodDtlSellDTO.getTargetSellAmount().multiply
          (BigDecimal.valueOf(100));
      if(fundNavunits.containsKey(prodDtlSellDTO.getFundCode())){
        Integer fundPriceInCents = fundNavunits.get(prodDtlSellDTO.getFundCode());
        BigDecimal quantity = sellTargetMoneyInCents.divide(BigDecimal.valueOf(fundPriceInCents),2, RoundingMode.HALF_UP);
        prodDtlSellDTOTgt.setFundQuantity(quantity.intValue());
      }else{
        prodDtlSellDTOTgt.setFundQuantity(0);
      }
      prodDtlSellDTOList.add(prodDtlSellDTOTgt);
    }
    ProdSellDTO prodSellDTO = new ProdSellDTO();
    BeanUtils.copyProperties(prodSellPageDTO, prodSellDTO);

    prodSellDTO.setProdDtlSellDTOList(prodDtlSellDTOList);
    TrdOrder result = generateOrderInfo4Sell(prodSellDTO);
    if(result == null){
      logger.error("failed to generate order info for sell information");
    }
    prodSellDTO.setUserPid(userBankInfo.getUserPid());
    broadcastMessageProducer.sendSellMessages(prodSellDTO);
    return result;
  }
  private TrdOrder generateOrderInfo4Sell(ProdSellDTO prodSellDTO){
    TrdOrder trdOrder = new TrdOrder();
    try {
      List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository
          .findByUserId(prodSellDTO.getUserId());
      int trdBrokerId = trdBrokerUsers.get(0).getTradeBrokerId().intValue();
      String bankcardNum = trdBrokerUsers.get(0).getBankCardNum();
      prodSellDTO.setTrdAcco(trdBrokerUsers.get(0).getTradeAcco());

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
//      trdOrder.setPayAmount(prodSellDTO.getSellTargetMoney());
      trdOrder.setOrderStatus(TrdOrderStatusEnum.WAITSELL.ordinal());
      trdOrder.setBankCardNum(bankcardNum);
      prodSellDTO.setUserProdId(trdOrders.get(0).getUserProdId());
      prodSellDTO.setProdId(trdOrders.get(0).getProdId());
      prodSellDTO.setTrdAcco(trdBrokerUsers.get(0).getTradeAcco());
      prodSellDTO.setTrdBrokerId(trdBrokerUsers.get(0).getTradeBrokerId());
      trdOrderRepository.save(trdOrder);
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      for( ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
        //生成赎回子订单信息
        trdOrderDetail.setUserId(prodSellDTO.getUserId());
        trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setUserProdId(prodSellDTO.getUserProdId());
        trdOrderDetail.setFundNum(prodDtlSellDTO.getFundQuantity());
        trdOrderDetail.setCreateBy(prodSellDTO.getUserId());
        trdOrderDetail.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
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
        trdOrderDetail = new TrdOrderDetail();
      }
      return trdOrder;
    }catch (Exception ex){
      ex.printStackTrace();
      logger.error(ex.getMessage());
      return null;
    }
  }
}
