package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollection;
import com.shellshellfish.aaas.userinfo.grpc.FinanceProdInfosQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserIdQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
  TrdTradeBankDicRepository trdTradeBankDicRepository;

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;


  UserInfoServiceFutureStub userInfoServiceFutureStub;


  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;


  @Autowired
  PayService payService;

  @Autowired
  ManagedChannel managedUIChannel;



  @PostConstruct
  public void init(){
    userInfoServiceFutureStub = UserInfoServiceGrpc.newFutureStub(managedUIChannel);
  }

  public void shutdown() throws InterruptedException {
    managedUIChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  @Override
  @Transactional( propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
  public TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdBuyInfo)
      throws Exception {
    ProductBaseInfo productBaseInfo = new ProductBaseInfo();
    BeanUtils.copyProperties(financeProdBuyInfo, productBaseInfo);
    List<ProductMakeUpInfo> productMakeUpInfos =  financeProdInfoService.getFinanceProdMakeUpInfo
        (productBaseInfo);
    if(productMakeUpInfos.size() <=0 ){
      logger.info("failed to get prod make up informations!");
      throw new Exception("failed to get prod make up informations!");
    }
    //在用户理财产品系统里面生成用户的理财产品, 这是日后《我的理财产品》模块的依据
    Long userProdId =  genUserProduct(financeProdBuyInfo, productMakeUpInfos);
    financeProdBuyInfo.setUserProdId(userProdId);
    return genOrderFromBuyInfoAndProdMakeUpInfo(financeProdBuyInfo, productMakeUpInfos);

  }

  private Long genUserProduct(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos) throws ExecutionException, InterruptedException {
    FinanceProdInfosQuery.Builder requestBuilder = FinanceProdInfosQuery.newBuilder();
    requestBuilder.setUserId(financeProdBuyInfo.getUserId());

    FinanceProdInfoCollection.Builder subReqBuilder = FinanceProdInfoCollection.newBuilder();
    FinanceProdInfo.Builder finProdInfoBuilder = FinanceProdInfo.newBuilder();
    for( ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      BeanUtils.copyProperties(productMakeUpInfo, finProdInfoBuilder);
      requestBuilder.addProdList(finProdInfoBuilder);
      finProdInfoBuilder.clear();
    }
    Long userProdId = userInfoServiceFutureStub.genUserProdsFromOrder(requestBuilder
        .build()).get().getUserProdId();
    if(userProdId == -1L){

    }
    logger.info("genUserProduct get :" + userProdId);
    return userProdId;
  }



  TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos) throws ExecutionException, InterruptedException {
    //generate order
//    TrdTradeBroker trdTradeBroker = trdBrokderRepository.findOne(1L);
    PayDto payDto = new PayDto();
    List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository.findByUserId(financeProdBuyInfo.getUserId());

    int trdBrokerId = trdBrokerUsers.get(0).getTradeBrokerId().intValue();
    String bankCardNum = financeProdBuyInfo.getBankAcc();
    String trdAcco = trdBrokerUsers.get(0).getTradeAcco();
    if(StringUtils.isEmpty(trdAcco)){
        //Todo: get userBankCardInfo to make tradAcco
      logger.info("trdBrokerUsers.get(0).getTradeAcco() is empty, 坑货出现，需要生成交易账号再交易");
      UserIdOrUUIDQuery.Builder builder = UserIdOrUUIDQuery.newBuilder();
      builder.setUuid(financeProdBuyInfo.getUuid());

      com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
          userInfoServiceFutureStub.getUserBankInfo(builder.build()).get();

      BindBankCard bindBankCard = new BindBankCard();
      TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByUserIdAndAndBankCardNum
          (userBankInfo.getUserId(),financeProdBuyInfo.getBankAcc());
      bindBankCard.setBankCardNum(financeProdBuyInfo.getBankAcc());
      TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankNameAndTraderBrokerId
          (BankUtil.getNameOfBank(financeProdBuyInfo.getBankAcc()), TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());

      bindBankCard.setBankCode(trdTradeBankDic.getBankCode());
      bindBankCard.setCellphone(userBankInfo.getCellphone());
      bindBankCard.setBankCardNum(financeProdBuyInfo.getBankAcc());
      bindBankCard.setTradeBrokerId(trdBrokerUser.getTradeBrokerId());
      bindBankCard.setUserId(trdBrokerUser.getUserId());
      bindBankCard.setUserName(userBankInfo.getUserName());
      bindBankCard.setUserPid(userBankInfo.getUserPid());
      trdAcco = payService.bindCard(bindBankCard);
      trdBrokerUserRepository.updateTradeAcco(trdAcco, TradeUtil.getUTCTime(), bindBankCard
          .getUserId(),  bindBankCard.getUserId());

    }
    String orderId = TradeUtil.generateOrderId(Integer.valueOf(financeProdBuyInfo.getBankAcc()
            .substring(0,6)),trdBrokerId);
    payDto.setTrdAccount(trdAcco);
    payDto.setUserUuid(financeProdBuyInfo.getUuid());
    payDto.setUserProdId(financeProdBuyInfo.getUserProdId());
    List<com.shellshellfish.aaas.common.message.order.TrdOrderDetail> trdOrderDetails =  new
        ArrayList<com.shellshellfish.aaas.common.message.order.TrdOrderDetail>();

    TrdOrder trdOrder = new TrdOrder();
    trdOrder.setBankCardNum(financeProdBuyInfo.getBankAcc());
    trdOrder.setOrderDate(TradeUtil.getUTCTime());
    trdOrder.setCreateDate(TradeUtil.getUTCTime());
    trdOrder.setOrderType(financeProdBuyInfo.getOrderType());
    trdOrder.setProdId(financeProdBuyInfo.getProdId());
    trdOrder.setUserProdId(financeProdBuyInfo.getUserProdId());
    trdOrder.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.ordinal());
    trdOrder.setOrderId(orderId);
    trdOrder.setUserId(financeProdBuyInfo.getUserId());
    trdOrder.setCreateBy(financeProdBuyInfo.getUserId());
    trdOrder.setPayAmount(TradeUtil.getLongNumWithMul100(financeProdBuyInfo.getMoney()));
    trdOrder.setProdCode(financeProdBuyInfo.getProdCode());
    trdOrderRepository.save(trdOrder);

    //generate sub order for each funds
    for(ProductMakeUpInfo productMakeUpInfo: productMakeUpInfos){
      TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
      trdOrderDetail.setOrderId(trdOrder.getOrderId());
      //规定基金占比用百分比并且精确万分之一
      BigDecimal fundRatio = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide
          (BigDecimal.valueOf(10000));
      trdOrderDetail.setFundMoneyQuantity(fundRatio.multiply(financeProdBuyInfo.getMoney())
          .multiply(BigDecimal.valueOf(100)).toBigInteger()
          .longValue());
      trdOrderDetail.setBoughtDate(TradeUtil.getUTCTime());
      trdOrderDetail.setCreateBy(financeProdBuyInfo.getUserId());
      trdOrderDetail.setCreateDate(TradeUtil.getUTCTime());
      trdOrderDetail.setUpdateBy(financeProdBuyInfo.getUserId());
      trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
      trdOrderDetail.setUserId(financeProdBuyInfo.getUserId());
      trdOrderDetail.setFundCode(productMakeUpInfo.getFundCode());
      trdOrderDetail.setUserProdId(trdOrder.getUserProdId());
      trdOrderDetail.setTradeType(trdOrder.getOrderType());
      trdOrderDetail = trdOrderDetailRepository.save(trdOrderDetail);
      com.shellshellfish.aaas.common.message.order.TrdOrderDetail trdOrderPay =  new com
          .shellshellfish.aaas.common.message.order.TrdOrderDetail();
      BeanUtils.copyProperties(trdOrderDetail, trdOrderPay);
      trdOrderPay.setOrderDetailId(trdOrderDetail.getId());
      trdOrderPay.setUserId(financeProdBuyInfo.getUserId());
//      GenericMessage<TrdOrderDetail> genericMessage = new GenericMessage<TrdOrderDetail>();
      trdOrderDetails.add(trdOrderPay);

    }
    payDto.setOrderDetailList(trdOrderDetails);
    payDto.setTrdBrokerId(trdBrokerId);
    broadcastMessageProducer.sendPayMessages(payDto);
    return trdOrder;
  }

  @Override
  public Long getUserId(String userUuid) throws ExecutionException, InterruptedException {
    UserIdQuery.Builder builder = UserIdQuery.newBuilder();
    builder.setUuid(userUuid);
    com.shellshellfish.aaas.userinfo.grpc.UserId userId = userInfoServiceFutureStub.getUserId(builder
        .build()).get();
    return userId.getUserId();
  }

}
