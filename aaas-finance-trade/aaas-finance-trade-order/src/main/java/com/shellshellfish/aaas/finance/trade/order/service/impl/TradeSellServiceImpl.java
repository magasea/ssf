package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceFutureStub;
import com.shellshellfish.aaas.datacollect.FundCodes;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPercentDTO;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.SellProductDetail;
import com.shellshellfish.aaas.userinfo.grpc.SellProductDetailResult;
import com.shellshellfish.aaas.userinfo.grpc.SellProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

  @Autowired
  PayService payGrpcService;

  @PostConstruct
  void init(){
    dataCollectionServiceFutureStub = DataCollectionServiceGrpc.newFutureStub(managedDCChannel);
  }




  @Override
  @Transactional
  public TrdOrder sellProduct(ProdSellPageDTO prodSellPageDTO)
      throws Exception {
    if(CollectionUtils.isEmpty(prodSellPageDTO.getProdDtlSellPageDTOList())){
	  logger.error(
		  "failed to generate sell information because input information is not complete,prodSellPageDTO.getProdDtlSellPageDTOList():{} ",
		  prodSellPageDTO.getProdDtlSellPageDTOList());
      throw new IllegalArgumentException("赎回输入信息不完整，无法赎回, prodSellPageDTO"
          + ".getProdDtlSellPageDTOList():" + prodSellPageDTO.getProdDtlSellPageDTOList());
    }else{
      //filter 0 amount request in getProdDtlSellPageDTOList
      Iterator<ProdDtlSellPageDTO> prodDtlSellPageDTOIterator = prodSellPageDTO
          .getProdDtlSellPageDTOList().iterator();
      while(prodDtlSellPageDTOIterator.hasNext()){
        ProdDtlSellPageDTO prodDtlSellPageDTO = prodDtlSellPageDTOIterator.next();
        if(TradeUtil.getLongNumWithMul100(prodDtlSellPageDTO.getTargetSellAmount()) == 0L){
          logger.error("needn't to handle the fundCode:{} , because sellAmount is :{}",
              prodDtlSellPageDTO.getFundCode(), prodDtlSellPageDTO.getTargetSellAmount());
          prodDtlSellPageDTOIterator.remove();
        }
      }
    }
    //first : get price of funds , this
    FundCodes.Builder requestBuilder = FundCodes.newBuilder();
    for(ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      requestBuilder.addFundCode(prodDtlSellPageDTO.getFundCode());
    }
    BigDecimal totalSell = BigDecimal.valueOf(0);
    List<String> fundCodes = new ArrayList<>();
    if(null == prodSellPageDTO.getSellTargetMoney()){
      for(ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
        totalSell = totalSell.add(prodDtlSellPageDTO.getTargetSellAmount());
        fundCodes.add(prodDtlSellPageDTO.getFundCode());
      }
      prodSellPageDTO.setSellTargetMoney(totalSell);
    }


    com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
        userInfoService.getUserBankInfo(prodSellPageDTO.getUserId());
    String userPid = null;
    List<TrdOrder> trdOrders = trdOrderRepository.findByUserProdId(prodSellPageDTO.getUserProdId());
    String usedBankCard = trdOrders.get(0).getBankCardNum();
    for(CardInfo cardInfo: userBankInfo.getCardNumbersList()){
      if(cardInfo.getCardNumbers().equals(usedBankCard)){
        userPid = cardInfo.getUserPid();
        break;
      }
    }
    List<FundNetInfo> fundNetInfos = payGrpcService.getFundNetInfo(userPid,fundCodes,1);

//    List<FundInfo> fundInfoList =dataCollectionServiceFutureStub.getFundsPrice
//        (requestBuilder.build()).get().getFundInfoList();
//    fundInfoList.get(0).getNavunit();
    Map<String, Integer> fundNavunits = new HashMap<>();
//    for(FundInfo fundInfo: fundInfoList){
//      fundNavunits.put(fundInfo.getFundCode(), fundInfo.getNavunit());
//    }
    for(FundNetInfo fundNetInfo: fundNetInfos){
      Long netValL = null;
      if(MonetaryFundEnum.containsCode(fundNetInfo.getFundCode())){
        logger.info("contains monetary fund");
        netValL = TradeUtil.getLongNumWithMul100(fundNetInfo.getUnitNet());
      }else{
        //四舍五入的基金净值
        netValL = TradeUtil.getLongNumWithMul100(TradeUtil.getBigDecimalNumWithRoundUp2Digit
            (fundNetInfo.getUnitNet()));
      }
      fundNavunits.put(fundNetInfo.getFundCode(), netValL.intValue());
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
        prodDtlSellDTOTgt.setFundQuantity(TradeUtil.getLongNumWithMul100(quantity).intValue());
      }else{
        prodDtlSellDTOTgt.setFundQuantity(0);
      }
      prodDtlSellDTOList.add(prodDtlSellDTOTgt);
    }
    ProdSellDTO prodSellDTO = new ProdSellDTO();
    BeanUtils.copyProperties(prodSellPageDTO, prodSellDTO);
    prodSellDTO.setSellTargetMoney(TradeUtil.getLongNumWithMul100(prodSellPageDTO.getSellTargetMoney()));
    prodSellDTO.setProdDtlSellDTOList(prodDtlSellDTOList);

    if(StringUtils.isEmpty(prodSellDTO.getUserBankNum())){
      prodSellDTO.setUserBankNum(usedBankCard);
    }

    TrdOrder result = generateOrderInfo4Sell(prodSellDTO, fundNavunits);
    if(result == null){
      logger.error("failed to generate order info for sell information");
    }

    prodSellDTO.setUserPid(userPid);
    broadcastMessageProducer.sendSellMessages(prodSellDTO);
    return result;
  }

  @Override
  public TrdOrder sellProductPercent(ProdSellPercentDTO prodSellPercentDTO) {
    return null;
  }


  private TrdOrder generateOrderInfo4Sell(ProdSellDTO prodSellDTO, Map<String, Integer>
      fundNavunits) throws Exception {
    TrdOrder trdOrder = new TrdOrder();
    boolean isSellable = true;
    List<Exception> errors = new ArrayList<>();
    try {
      TrdBrokerUser trdBrokerUser = trdBrokerUserRepository
          .findByUserIdAndBankCardNum(prodSellDTO.getUserId(), prodSellDTO.getUserBankNum());


//
//      String bankcardNum = trdBrokerUsers.get(0).getBankCardNum();

      List<TrdOrder> trdOrders = trdOrderRepository.findByUserProdId(prodSellDTO.getUserProdId());
      if (CollectionUtils.isEmpty(trdOrders)) {
        logger.error("failed to find corresponding order for sell by userProdId:");
        return null;
      }
      //在生成订单前先检查一下是否够减
      SellProducts.Builder spBuilder = SellProducts.newBuilder();
      spBuilder.setUserId(prodSellDTO.getUserId());
      spBuilder.setUserProductId(prodSellDTO.getUserProdId());
      SellProductDetail.Builder spdBuilder = SellProductDetail.newBuilder();
      for(ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
        spdBuilder.clear();
        spdBuilder.setFundQuantityTrade(prodDtlSellDTO.getFundQuantity());
        spdBuilder.setFundCode(prodDtlSellDTO.getFundCode());
        spdBuilder.setResult(1);
        spBuilder.addSellProductDetails(spdBuilder);
      }

      prodSellDTO.setTrdAcco(trdBrokerUser.getTradeAcco());
      SellProductsResult results = userInfoService.checkSellProducts(spBuilder.build());
      if(results.getErrInfo() != null && results.getErrInfo().getErrCode() < 0){
        ErrInfo errInfo = results.getErrInfo();
		logger.error("赎回失败:{}", errInfo.getErrMsg());
        throw new Exception(String.format("赎回失败:%s", errInfo.getErrMsg()));
      }
//      for( SellProductDetailResult sellProductDetail: results.getSellProductDetailResultsList()){
//        if(sellProductDetail.getResult() < 0){
//          isSellable = false;
//          logger.error("cannot sell the userProdId:" + prodSellDTO.getUserProdId() + " because "
//              + "the fundCode:" + sellProductDetail.getFundCode() + " quantity is not enough");
//          throw new IllegalArgumentException("cannot sell the userProdId:" + prodSellDTO.getUserProdId() + " because "
//              + "the fundCode:" + sellProductDetail.getFundCode() + " quantity is not enough");
//        }
//      }

      // if the results list contains -1, means need adjust sell target money
      Map<String, SellProductDetailResult> sellProdDetailNeedHandle = new HashMap<>();
      for( SellProductDetailResult sellProductDetailResult: results.getSellProductDetailResultsList()){
        if(sellProductDetailResult.getResult() < 0){
          sellProdDetailNeedHandle.put(sellProductDetailResult.getFundCode(),
              sellProductDetailResult);
        }
      }
      BigDecimal delta = BigDecimal.ZERO;
      boolean adjusted = false;
      for(ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
        if(sellProdDetailNeedHandle.containsKey(prodDtlSellDTO.getFundCode())){
          logger.error("need adjust the target sell money and fund sell money and quantity for "
              + "fundCode:{} and the current netValue:{}", prodDtlSellDTO.getFundCode(), fundNavunits.get(prodDtlSellDTO.getFundCode()));
          BigDecimal targetSellAmount = TradeUtil.getBigDecimalNumWithDiv10000(
              sellProdDetailNeedHandle.get(prodDtlSellDTO.getFundCode()
          ).getFundQuantityTradeRemain()*fundNavunits.get(prodDtlSellDTO.getFundCode()));
          BigDecimal diff = prodDtlSellDTO.getTargetSellAmount().subtract(targetSellAmount);
          delta=delta.add(diff);
          logger.error("the diff for fundCode:{} is:{}", prodDtlSellDTO.getFundCode(), diff);
          prodDtlSellDTO.setTargetSellAmount(targetSellAmount);
          prodDtlSellDTO.setFundQuantity(Long.valueOf(sellProdDetailNeedHandle.get(prodDtlSellDTO
              .getFundCode()).getFundQuantityTradeRemain()).intValue());
          adjusted = true;

        }
      }
      if(adjusted){
        Long adjustedTarget = prodSellDTO.getSellTargetMoney() - TradeUtil
            .getLongNumWithMul100(delta);
        logger.error("now adjust the getSellTargetMoney:{} to {}", prodSellDTO.getSellTargetMoney
            (), adjustedTarget);
        prodSellDTO.setSellTargetMoney(adjustedTarget);
      }
      String bankCardNum = null;
      if(StringUtils.isEmpty(results.getUserBankNum())){
        logger.error("the user_prod_id:" + results.getUserProductId() + " haven't save "
            + "bankCardNum");
        bankCardNum = prodSellDTO.getUserBankNum();
      }else{
        bankCardNum = results.getUserBankNum();
      }
      String orderId = TradeUtil.generateOrderIdByBankCardNum(bankCardNum
          , TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId().intValue());
      trdOrder.setUserId(prodSellDTO.getUserId());
      trdOrder.setOrderId(orderId);
      trdOrder.setOrderType(TrdOrderOpTypeEnum.REDEEM.getOperation());
      trdOrder.setUserProdId(prodSellDTO.getUserProdId());
      trdOrder.setCreateBy(prodSellDTO.getUserId());
      trdOrder.setCreateDate(TradeUtil.getUTCTime());
      trdOrder.setUpdateBy(prodSellDTO.getUserId());
//      trdOrder.setPayAmount(prodSellDTO.getSellTargetMoney());
      trdOrder.setOrderStatus(TrdOrderStatusEnum.WAITSELL.ordinal());
      trdOrder.setBankCardNum(results.getUserBankNum());
      trdOrder.setPayAmount(prodSellDTO.getSellTargetMoney());
      prodSellDTO.setUserProdId(trdOrders.get(0).getUserProdId());
      prodSellDTO.setProdId(trdOrders.get(0).getProdId());
      prodSellDTO.setTrdAcco(trdBrokerUser.getTradeAcco());
      prodSellDTO.setTrdBrokerId(trdBrokerUser.getTradeBrokerId());
      prodSellDTO.setOrderId(trdOrder.getOrderId());
      trdOrder = trdOrderRepository.save(trdOrder);
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      if(CollectionUtils.isEmpty(prodSellDTO.getProdDtlSellDTOList())){
        logger.error("prodSellDTO.getProdDtlSellDTOList() is empty:{}", prodSellDTO.getProdDtlSellDTOList());
        throw new IllegalArgumentException("赎回信息输入不完整 prodSellDTO.getProdDtlSellDTOList()：" +
            prodSellDTO.getProdDtlSellDTOList());
      }
      for( ProdDtlSellDTO prodDtlSellDTO: prodSellDTO.getProdDtlSellDTOList()){
        //生成赎回子订单信息
        trdOrderDetail.setUserId(prodSellDTO.getUserId());
        trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setUserProdId(prodSellDTO.getUserProdId());
//        trdOrderDetail.setFundNum(prodDtlSellDTO.getFundQuantity());
        trdOrderDetail.setCreateBy(prodSellDTO.getUserId());
        trdOrderDetail.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        trdOrderDetail.setUpdateBy(prodSellDTO.getUserId());
        trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
        trdOrderDetail.setBuysellDate(TradeUtil.getUTCTime());
        trdOrderDetail.setFundCode(prodDtlSellDTO.getFundCode());
        trdOrderDetail.setFundNum(prodDtlSellDTO.getFundQuantity());
        trdOrderDetail.setFundSum(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
            .getTargetSellAmount()));
        trdOrderDetail.setFundMoneyQuantity(TradeUtil.getLongNumWithMul100(prodDtlSellDTO
            .getTargetSellAmount()));
        trdOrderDetail.setOrderId(orderId);
        trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.WAITSELL.getStatus());
        trdOrderDetail = trdOrderDetailRepository.save(trdOrderDetail);
        prodDtlSellDTO.setOrderDetailId(trdOrderDetail.getId());
        trdOrderDetail = new TrdOrderDetail();
      }
      return trdOrder;
    }catch (Exception ex){
      logger.error("exception:",ex);

      errors.add(ex);
      StringBuilder sb = new StringBuilder();
      for(Exception err: errors){
        sb.append(err.getMessage()+"\n");
      }
      logger.error(sb.toString(), ex);
      throw new Exception(sb.toString());
    }

  }
}
