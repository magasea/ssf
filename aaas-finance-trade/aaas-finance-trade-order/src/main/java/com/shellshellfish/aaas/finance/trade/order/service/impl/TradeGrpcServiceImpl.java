package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.finance.trade.grpc.SellProdInfo;
import com.shellshellfish.aaas.finance.trade.grpc.SellProdResult;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */
@Service
public class TradeGrpcServiceImpl implements TradeSellService {

  Logger logger = LoggerFactory.getLogger(TradeGrpcServiceImpl.class);

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
  public Boolean sellProduct(ProdSellPageDTO prodSellPageDTO) {
    List<ProdDtlSellDTO> prodDtlSellDTOList = new ArrayList<ProdDtlSellDTO>();
    ProdSellDTO prodSellDTO =  new ProdSellDTO();
    BeanUtils.copyProperties(prodSellPageDTO, prodSellDTO);
    for( ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      ProdDtlSellDTO prodDtlSellDTO = new ProdDtlSellDTO();
      BeanUtils.copyProperties(prodDtlSellPageDTO, prodDtlSellDTO);
      prodDtlSellDTOList.add(prodDtlSellDTO);
    }
    prodSellDTO.setProdDtlSellDTOList(prodDtlSellDTOList);
    broadcastMessageProducer.sendPayMessages(prodSellDTO);
    return null;
  }
}
