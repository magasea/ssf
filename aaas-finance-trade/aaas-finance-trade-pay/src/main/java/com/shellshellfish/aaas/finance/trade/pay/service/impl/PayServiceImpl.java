package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdPayFlowStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.DateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery;
import com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceImplBase;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl extends PayRpcServiceImplBase implements PayService {

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
        fundResult = fundTradeApiService.buyFund(payDto.getUserUuid(), trdAcco, payAmount,
            String.valueOf(trdOrderDetail.getId()),trdOrderDetail.getFundCode());
      }catch (Exception ex){
        ex.printStackTrace();
        logger.error(ex.getMessage());
        errs.add(ex);
      }
      //ToDo: 如果有真实数据， 则删除下面if代码
      if(null == fundResult){

        fundResult = new BuyFundResult();
        fundResult.setApplySerial("12312341");
        fundResult.setCapitalMode("");
        fundResult.setOutsideOrderNo(""+trdOrderDetail.getId());
        fundResult.setKkstat(TrdPayFlowStatusEnum.NOTHANDLED.getComment());

      }
      if(null != fundResult){
        trdPayFlow.setApplySerial(fundResult.getApplySerial());
        trdPayFlow.setPayStatus(TradeUtil.getPayFlowStatus(fundResult.getKkstat()));
        trdPayFlow.setCreateDate(TradeUtil.getUTCTime());
        trdPayFlow.setFundCode(trdOrderDetail.getFundCode());
        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
        trdPayFlow.setCreateBy(trdOrderDetail.getUserId());
        trdPayFlow.setUpdateBy(trdOrderDetail.getUserId());
        trdPayFlow.setTradeAcco(trdAcco);
        trdPayFlow.setProdId(trdOrderDetail.getProdId());
        trdPayFlow.setTradeBrokeId(payDto.getTrdBrokerId());;
        TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
            .shellshellfish.aaas.common.message.order.TrdPayFlow();
        BeanUtils.copyProperties(trdPayFlowResult, trdPayFlowMsg);
        notifyPay(trdPayFlowMsg);
      }
    }
    if(errs.size() > 0){
      throw new Exception("meet errors in pay api services");
    }
    return payDto;
  }

  @Override
  public com.shellshellfish.aaas.common.message.order.TrdPayFlow notifyPay(com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlow) {
    logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());

    broadcastMessageProducers.sendMessage(trdPayFlow);
    return trdPayFlow;
  }

  @Override
  public String bindCard(BindBankCard bindBankCard) {
    try {
      OpenAccountResult openAccountResult = fundTradeApiService.openAccount("" +bindBankCard
              .getUserId(), bindBankCard.getUserName(),bindBankCard.getCellphone(), bindBankCard
              .getUserPid(), bindBankCard.getBankCardNum(),
          bindBankCard.getBankCode());
      return openAccountResult.getTradeAcco();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void bindBankCard(com.shellshellfish.aaas.finance.trade.pay.BindBankCardQuery bindBankCardQuery,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.pay.BindBankCardResult> responseObserver){
    BindBankCard bindBankCard = new BindBankCard();
    BeanUtils.copyProperties(bindBankCardQuery, bindBankCard);
    String trdAcco = bindCard(bindBankCard);
    BindBankCardResult.Builder builder = BindBankCardResult.newBuilder();
    builder.setTradeacco(trdAcco);
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
