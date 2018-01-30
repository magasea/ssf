package com.shellshellfish.aaas.finance.trade.order.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.UserPID;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceImplBase;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderServiceImpl  extends OrderRpcServiceGrpc.OrderRpcServiceImplBase implements OrderService{

  Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Autowired
  UserInfoService userInfoService;

  @Override
  public List<TrdOrderDetail> getOrderByUserId(Long userId) {
    List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
    List<TrdOrder> trdOrders = trdOrderRepository.findTrdOrdersByUserId(userId);
    for(TrdOrder trdOrder: trdOrders){
      List<TrdOrderDetail> trdOrderDetailList = trdOrderDetailRepository.findAllByOrderId(trdOrder
          .getOrderId());
      trdOrderDetails.addAll(trdOrderDetailList);
    }
    return trdOrderDetails;
  }
  
  
	@Override
	public TrdOrder getOrderByOrderId(String orderId) {
		TrdOrder trdOrders = trdOrderRepository.findByOrderId(orderId);
		return trdOrders;
	}


	@Override
	public List<TrdOrderDetail> findOrderDetailByOrderId(String orderId) {
		//List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
		List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findAllByOrderId(orderId);
		return trdOrderDetails;
	}
	@Override
	public TrdOrder findOrderByUserProdIdAndUserId(Long prodId,Long userId) {
		//List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
		List<TrdOrder> trdOrders = trdOrderRepository.findByUserProdIdAndUserId(prodId,userId);
		TrdOrder trdOrder = trdOrders.get(0);
		return trdOrder;
	}

  /**
   * <pre>
   *获取交易用户PID
   * </pre>
   */
  @Override
  public void getPidFromTrdAccoBrokerId(com.shellshellfish.aaas.finance.trade.order.GetPIDReq request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order.UserPID> responseObserver) {
    String trdAcco = request.getTrdAcco();
    int brokerId = request.getTrdBrokerId();
    long userId = request.getUserId();
    if(StringUtils.isEmpty(trdAcco)||brokerId <= 0 || userId <= 0){
      logger.error("trdAcco:"+ trdAcco + " brokerId:"+brokerId + " userId:"+userId +" is not valid");
    }
    TrdBrokerUser trdBrokerUser = trdBrokerUserRepository.findByTradeAccoAndTradeBrokerId(trdAcco, brokerId);
    //获取银行卡cardNm后去查询userInfo里面的 userPid
    UserBankInfo userInfo = null;
    UserPID.Builder upidBuilder = UserPID.newBuilder();
    try {
      userInfo = userInfoService.getUserBankInfo(userId);
      for(CardInfo cardInfo: userInfo.getCardNumbersList()){
        if(trdBrokerUser.getBankCardNum().equals(cardInfo.getCardNumbers())){
          upidBuilder.setUserPid(cardInfo.getUserPid());
        }
      }
      responseObserver.onNext(upidBuilder.build());
      responseObserver.onCompleted();
    } catch (ExecutionException |InterruptedException e ) {
      e.printStackTrace();
      logger.error(e.getMessage());
      upidBuilder.setUserPid("-1");
      responseObserver.onNext(upidBuilder.build());
      responseObserver.onCompleted();
    }


  }

}
