package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.finance.trade.order.BindCardResult;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailResult;
import com.shellshellfish.aaas.finance.trade.order.OrderQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.UserBankCardNum;
import com.shellshellfish.aaas.finance.trade.order.UserPID;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.redis.UserPidDAO;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class OrderServiceImpl extends OrderRpcServiceGrpc.OrderRpcServiceImplBase implements
		OrderService {

	Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	TrdOrderRepository trdOrderRepository;

	@Autowired
	TrdOrderDetailRepository trdOrderDetailRepository;

	@Autowired
	TrdBrokerUserRepository trdBrokerUserRepository;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	TrdTradeBankDicRepository trdTradeBankDicRepository;

	@Resource
	UserPidDAO userPidDAO;

	@Autowired
	PayService payService;

	@Override
	public List<TrdOrderDetail> getOrderByUserId(Long userId) {
		List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
		List<TrdOrder> trdOrders = trdOrderRepository.findTrdOrdersByUserId(userId);
		for (TrdOrder trdOrder : trdOrders) {
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
	public TrdOrder findOrderByUserProdIdAndUserId(Long prodId, Long userId) {
		//List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
		List<TrdOrder> trdOrderList = trdOrderRepository.findByUserProdIdAndUserId(prodId, userId);
		TrdOrder trdOrder = new TrdOrder();
		if (trdOrderList != null && trdOrderList.size() > 0) {
			trdOrder = trdOrderList.get(0);
		}
		return trdOrder;
	}


	/**
	 * <pre>
	 * 获取交易用户PID
	 * </pre>
	 */
	@Override
	public void getPidFromTrdAccoBrokerId(
			com.shellshellfish.aaas.finance.trade.order.GetPIDReq request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order.UserPID> responseObserver) {
		String trdAcco = request.getTrdAcco();
		int brokerId = request.getTrdBrokerId();
		long userId = request.getUserId();
		UserPID.Builder upidBuilder = UserPID.newBuilder();
		if (StringUtils.isEmpty(trdAcco) || brokerId < 0 || userId <= 0) {
			logger.error(
					"trdAcco:" + trdAcco + " brokerId:" + brokerId + " userId:" + userId + " is not valid");
		}
		if(StringUtils.isEmpty(userPidDAO.getUserPid(trdAcco,brokerId,userId) )){
			logger.error(
					"trdAcco:" + trdAcco + " brokerId:" + brokerId + " userId:" + userId + " cannot find "
							+ "corresponding userPid");
		}else{
			upidBuilder.setUserPid(userPidDAO.getUserPid(trdAcco,brokerId,userId));
			responseObserver.onNext(upidBuilder.build());
			responseObserver.onCompleted();
			return;
		}
		List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository
				.findByTradeAccoAndTradeBrokerId(trdAcco, brokerId);
		//获取银行卡cardNm后去查询userInfo里面的 userPid
		UserBankInfo userInfo = null;


		try {
			if(CollectionUtils.isEmpty(trdBrokerUsers)){
				logger.error("failed to find trdBrokerUsers with trdAcco:{} brokerId:{}", trdAcco, brokerId);
				throw new Exception(String.format("failed to find trdBrokerUsers with trdAcco:%s "
						+ "brokerId:%s", trdAcco, brokerId));
			}
			Set<String> cardNums = new HashSet<>();
			trdBrokerUsers.forEach(item -> cardNums.add(item.getBankCardNum()));
			userInfo = userInfoService.getUserBankInfo(userId);
			for (CardInfo cardInfo : userInfo.getCardNumbersList()) {
				if (cardNums.contains(cardInfo.getCardNumbers())) {
					userPidDAO.addUserPid(trdAcco,brokerId,userId, cardInfo.getUserPid());
					upidBuilder.setUserPid(cardInfo.getUserPid());
				}
			}
			responseObserver.onNext(upidBuilder.build());
			responseObserver.onCompleted();
		} catch ( Exception e) {
			logger.error("exception:",e);
			logger.error(e.getMessage());
			userPidDAO.deleteUserPid(trdAcco, brokerId, userId);
			upidBuilder.setUserPid("-1");
			responseObserver.onNext(upidBuilder.build());
			responseObserver.onCompleted();
		}


	}

	/**
	 *
	 * @param request
	 * @param responseObserver
	 */
	@Override
	public void getOrderInfo(OrderQueryInfo request, StreamObserver<OrderResult> responseObserver) {
		TrdOrder trdOrder = trdOrderRepository
				.findFirstByUserProdIdAndOrderStatus(request.getUserProdId(), request.getOrderStatus());

		if (trdOrder == null) {
			trdOrder = new TrdOrder();
		}
		logger.info("trdOrder{}", trdOrder);
		OrderResult.Builder orderResultBuilder = OrderResult.newBuilder();

		orderResultBuilder.setCreateBy(Optional.ofNullable(trdOrder.getCreateBy()).orElse(0L));
		orderResultBuilder.setCreateDate(Optional.ofNullable(trdOrder.getCreateDate()).orElse(0L));
		orderResultBuilder.setGroupId(Optional.ofNullable(trdOrder.getGroupId()).orElse(0L));
		orderResultBuilder.setOrderDate(Optional.ofNullable(trdOrder.getOrderDate()).orElse(0L));
		orderResultBuilder.setOrderType(Optional.ofNullable(trdOrder.getOrderType()).orElse(0));
		orderResultBuilder.setPayAmount(Optional.ofNullable(trdOrder.getPayAmount()).orElse(0L));
		orderResultBuilder.setOrderStatus(Optional.ofNullable(trdOrder.getOrderStatus()).orElse(0));
		orderResultBuilder.setPreOrderId(Optional.ofNullable(trdOrder.getPreOrderId()).orElse(0L));
		orderResultBuilder.setUserId(Optional.ofNullable(trdOrder.getUserId()).orElse(0L));
		orderResultBuilder.setUpdateDate(Optional.ofNullable(trdOrder.getUpdateDate()).orElse(0L));
		orderResultBuilder.setBankCardNum(Optional.ofNullable(trdOrder.getBankCardNum()).orElse("-1"));
		orderResultBuilder.setOrderId(Optional.ofNullable(trdOrder.getOrderId()).orElse("-1"));
		orderResultBuilder.setPayFee(Optional.ofNullable(trdOrder.getPayFee()).orElse(0L));

		OrderResult orderResult = orderResultBuilder.build();

		logger.info("=====================================================");
		logger.info("payAmount {}", orderResult.getPayAmount());

		logger.info("=====================================================");
		responseObserver.onNext(orderResult);
		responseObserver.onCompleted();
	}

	/**
	 *
	 * @param request
	 * @param responseObserver
	 */
	@Override
	public void getOrderDetail(OrderDetailQueryInfo request,
			StreamObserver<OrderDetailResult> responseObserver) {

		List<TrdOrderDetail> result = trdOrderDetailRepository
				.findAllByUserProdIdAndOrderDetailStatus(request.getUserProdId(),
						request.getOrderDetailStatus());

		if (result == null) {
			result = new ArrayList<>(0);
		}
		OrderDetailResult.Builder builder = OrderDetailResult.newBuilder();

		for (int i = 0; i < result.size(); i++) {
			OrderDetail.Builder orderDetailBuilder = OrderDetail.newBuilder();
			MyBeanUtils.mapEntityIntoDTO(result.get(i), orderDetailBuilder);
			builder.addOrderDetailResult(orderDetailBuilder);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}


	@Override
	public Map<String, Object> getBankInfos(String bankShortName) {
		Map<String, Object> result = new HashMap<String, Object>();
		TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankShortName(bankShortName);
		if(null != trdTradeBankDic){
			result.put("bankName", trdTradeBankDic.getBankName());
			result.put("bankCode", trdTradeBankDic.getBankCode());
		}else{
			result.put("bankName", "");
			result.put("bankCode", "");
		}

		return result;
	}

	/**
	 */
	@Override
	public void getBankCardNumByUserProdId(com.shellshellfish.aaas.grpc.common.UserProdId request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order.UserBankCardNum> responseObserver) {
		Long userProdId = request.getUserProdId();
		List<TrdOrder> trdOrderList =  trdOrderRepository.findByUserProdId(userProdId);
		UserBankCardNum.Builder ubcnBuilder = UserBankCardNum.newBuilder();
		ubcnBuilder.setUserBankCardnum(trdOrderList.get(0).getBankCardNum());
		responseObserver.onNext(ubcnBuilder.build());
		responseObserver.onCompleted();

	}

	@Override
	/**
	 */
	public void openAccount(com.shellshellfish.aaas.finance.trade.order.BindCardInfo bankCardInfo,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order.BindCardResult> responseObserver) {
		BindCardResult.Builder resultBuilder = BindCardResult.newBuilder();

		final String errorMsg = "-1";

		String bankCardNo = bankCardInfo.getCardNo();
		String bankName = BankUtil.getNameOfBank(bankCardNo);
		bankName = BankUtil.getZZBankNameFromOriginBankName(bankName);

		TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankNameAndTraderBrokerId
				(bankName, TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());

		String tradeNo = null;
		if (trdTradeBankDic != null) {
			BindBankCard bindBankCard = new BindBankCard();
			bindBankCard.setBankCardNum(bankCardInfo.getCardNo());
			bindBankCard.setUserId(bankCardInfo.getUserId());
			bindBankCard.setBankCode(trdTradeBankDic.getBankCode());
			bindBankCard.setCellphone(bankCardInfo.getUserPhone());
			bindBankCard.setTradeBrokerId(trdTradeBankDic.getTraderBrokerId());
			bindBankCard.setUserPid(bankCardInfo.getIdCardNo());
			bindBankCard.setUserName(bankCardInfo.getUserName());
			bindBankCard.setRiskLevel(bankCardInfo.getRiskLevel());

			try {
				tradeNo = payService.bindCard(bindBankCard);
			} catch (ExecutionException e) {
				logger.error(e.getMessage());
				tradeNo = errorMsg;
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				tradeNo = errorMsg;
			} catch (Exception e) {
				logger.error("exception:",e);
				logger.error(e.getMessage());
				if(e.getMessage().contains("|")){
					int errCode = Integer.parseInt(e.getMessage().split("|")[0]);
					String errMsg = e.getMessage().split("|")[1];
					ErrInfo.Builder eiBuilder = ErrInfo.newBuilder();
					eiBuilder.setErrCode(errCode);
					eiBuilder.setErrMsg(errMsg);
					resultBuilder.setErrInfo(eiBuilder.build());
				}
			}
		} else {
			tradeNo = errorMsg;
		}
		resultBuilder.setTradeacco(tradeNo);
		responseObserver.onNext(resultBuilder.build());
		responseObserver.onCompleted();
	}


}
