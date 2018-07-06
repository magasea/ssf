package com.shellshellfish.aaas.finance.trade.order.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZBankInfo;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;

import com.shellshellfish.aaas.finance.trade.order.BindCardResult;

import com.shellshellfish.aaas.finance.trade.order.FundCodeQuerys;
import com.shellshellfish.aaas.finance.trade.order.FundLimitDetail;
import com.shellshellfish.aaas.finance.trade.order.FundLimitResults;
import com.shellshellfish.aaas.finance.trade.order.FundLimitResults.Builder;
import com.shellshellfish.aaas.finance.trade.order.FundQuerys;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailPageResult;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailResult;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailStatusRequest;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailStatusResponse;
import com.shellshellfish.aaas.finance.trade.order.OrderQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.order.UserBankCardNum;
import com.shellshellfish.aaas.finance.trade.order.UserPID;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FundTradeLimitInfo;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.redis.UserPidDAO;
import com.shellshellfish.aaas.finance.trade.order.service.FundInfoApiService;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.ZZApiService;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareInfoResult;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    FundInfoApiService fundInfoApiService;

    @Resource
    UserPidDAO userPidDAO;

    @Autowired
    PayService payService;

    @Autowired
    ZZApiService zzApiService;

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

    //	@Override
//	public TrdOrder findOrderByUserProdIdAndUserId(Long prodId, Long userId) {
//		//List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
//		List<TrdOrder> trdOrderList = trdOrderRepository.findByUserProdIdAndUserId(prodId, userId);
//		TrdOrder trdOrder = new TrdOrder();
//		if (trdOrderList != null && trdOrderList.size() > 0) {
//			trdOrder = trdOrderList.get(0);
//		}
//		return trdOrder;
//	}
    @Override
    public TrdOrder findOrderByUserProdIdAndUserIdAndorderType(Long prodId, Long userId,
                                                               int orderType) {
        //List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
        List<TrdOrder> trdOrderList = trdOrderRepository.findByUserProdIdAndUserId(prodId, userId);
        TrdOrder trdOrder = new TrdOrder();
        if (trdOrderList != null && trdOrderList.size() > 0) {
            for (int i = 0; i < trdOrderList.size(); i++) {
                trdOrder = trdOrderList.get(i);
                if (trdOrder.getOrderType() == orderType) {
                    break;
                }
            }
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
        if (StringUtils.isEmpty(userPidDAO.getUserPid(trdAcco, brokerId, userId))) {
            logger.error(
                    "trdAcco:" + trdAcco + " brokerId:" + brokerId + " userId:" + userId + " cannot find "
                            + "corresponding userPid");
        } else {
            upidBuilder.setUserPid(userPidDAO.getUserPid(trdAcco, brokerId, userId));
            responseObserver.onNext(upidBuilder.build());
            responseObserver.onCompleted();
            return;
        }
        List<TrdBrokerUser> trdBrokerUsers = trdBrokerUserRepository
                .findByTradeAccoAndTradeBrokerId(trdAcco, brokerId);
        //获取银行卡cardNm后去查询userInfo里面的 userPid
        UserBankInfo userInfo = null;

        try {
            if (CollectionUtils.isEmpty(trdBrokerUsers)) {
                logger
                        .error("failed to find trdBrokerUsers with trdAcco:{} brokerId:{}", trdAcco, brokerId);
                throw new Exception(String.format("failed to find trdBrokerUsers with trdAcco:%s "
                        + "brokerId:%s", trdAcco, brokerId));
            }
            Set<String> cardNums = new HashSet<>();
            trdBrokerUsers.forEach(item -> cardNums.add(item.getBankCardNum()));
            List<UserBankInfo> userBankInfos = new ArrayList<>();
            if(userId <= 0){
                logger.error("the payflow havent store valid userid:{}, we use the tradeAcco "
                    + "related userId", userId);

                for(TrdBrokerUser trdBrokerUser: trdBrokerUsers){
                   UserBankInfo userBankInfo =  userInfoService.getUserBankInfo(trdBrokerUser.getUserId());
                   if(userBankInfo != null){
                       userBankInfos.add(userBankInfo);
                   }
                }
            }else{
                userInfo = userInfoService.getUserBankInfo(userId);
                userBankInfos.add(userInfo);
            }
            for(UserBankInfo userBankInfo: userBankInfos){
                for (CardInfo cardInfo : userBankInfo.getCardNumbersList()) {
                    if (cardNums.contains(cardInfo.getCardNumbers())) {
                        userPidDAO.addUserPid(trdAcco, brokerId, userId, cardInfo.getUserPid());
                        upidBuilder.setUserPid(cardInfo.getUserPid());
                    }
                }
            }
            responseObserver.onNext(upidBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error("exception:", e);
            logger.error(e.getMessage());
            onError(responseObserver, e);
            userPidDAO.deleteUserPid(trdAcco, brokerId, userId);

        }


    }

    /**
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
     * @param request
     * @param responseObserver
     */
    @Override
    public void getOrderDetail(OrderDetailQueryInfo request,
                               StreamObserver<OrderDetailResult> responseObserver) {
        try {
            List<TrdOrderDetail> result = null;
            if (request.getOrderDetailStatus() == Integer.MAX_VALUE) {
                result = trdOrderDetailRepository
                        .findAllByUserProdId(request.getUserProdId());
                if (result != null && result.size() > 0) {
                    Collections.sort(result, (o1, o2) -> {
                        Long map1value = o1.getBuysellDate();
                        Long map2value = o2.getBuysellDate();
                        return map2value.compareTo(map1value);
                    });
                    String orderId = result.get(0).getOrderId();
                    List<TrdOrderDetail> filterCollect = result.stream()
                            .filter(filter -> orderId.equals(filter.getOrderId())).collect(Collectors.toList());
                    List<TrdOrderDetail> collect = filterCollect.stream()
                        .filter(k -> k.getFundNum() > 0).collect(Collectors.toList());
                    result = collect;
                }
            } else {
                result = trdOrderDetailRepository
                        .findAllByUserProdIdAndOrderDetailStatus(request.getUserProdId(),
                                request.getOrderDetailStatus());
            }
            if (result == null || result.size() == 0) {
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

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getLatestOrderDetail(UserProdId request,
                                     StreamObserver<OrderDetailResult> responseObserver) {

        TrdOrderDetail trdOrderDetail = trdOrderDetailRepository
                .findTopByUserProdIdOrderByCreateDateDesc(request.getUserProdId());

        List<TrdOrderDetail> result = trdOrderDetailRepository
                .findAllByOrderId(trdOrderDetail.getOrderId());

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


    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAllOrderDetail(UserProdId request,
                                  StreamObserver<OrderDetailResult> responseObserver) {

        List<TrdOrderDetail> result = trdOrderDetailRepository
                .findAllByUserProdId(request.getUserProdId());

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
        if (null != trdTradeBankDic) {
            result.put("bankName", trdTradeBankDic.getBankName());
            result.put("bankCode", trdTradeBankDic.getBankCode());
        } else {
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
        List<TrdOrder> trdOrderList = trdOrderRepository.findByUserProdId(userProdId);
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
                logger.error("exception:", e);
                logger.error(e.getMessage());
                if (e.getMessage().contains("|")) {
                    String errCodeStr = e.getMessage().split("\\|")[0];
                    Long errCode = Long.parseLong(errCodeStr);
                    String errMsg = e.getMessage().split("\\|")[1];
                    ErrInfo.Builder eiBuilder = ErrInfo.newBuilder();
                    eiBuilder.setErrCode(errCode);
                    eiBuilder.setErrMsg(errMsg);
                    resultBuilder.setErrInfo(eiBuilder.build());
                    tradeNo = "-1";
                }
            }
        }else {
            responseObserver.onError(Status.UNAVAILABLE.withDescription("此银行卡不支持").asRuntimeException());
        }
        try {
            resultBuilder.setTradeacco(tradeNo);
            responseObserver.onNext(resultBuilder.build());
            responseObserver.onCompleted();
        }catch (Exception ex){
            onError(responseObserver, ex);
        }

    }

    @Override
    public void syncBankInfos() {
        try {
            List<ZZBankInfo> zzBankInfos = zzApiService.getZZSupportedBanks();
            zzBankInfos.forEach(bankInfo -> {
                if (StringUtils.isEmpty(bankInfo.getBankSerial())) {
                    logger.error("bankCode not valid:{}", bankInfo.getBankSerial());
                    return;
                }
                TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankCode(bankInfo
                        .getBankSerial());
                if (trdTradeBankDic != null) {
                    trdTradeBankDic.setCapitalModel(bankInfo.getCapitalModel());
                    trdTradeBankDic.setMoneyLimitDay(bankInfo.getMoneyLimitDay());
                    trdTradeBankDic.setMoneyLimitOne(bankInfo.getMoneyLimitOne());
                    logger.info("update bankinfo for bankCode:{}", trdTradeBankDic.getBankCode());
                    trdTradeBankDicRepository.save(trdTradeBankDic);
                } else {
                    trdTradeBankDic.setTraderBrokerId(TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());
                    trdTradeBankDic.setBankCode(bankInfo.getBankSerial());
                    trdTradeBankDic.setBankName(bankInfo.getBankName());
                    trdTradeBankDic.setCapitalModel(bankInfo.getCapitalModel());
                    trdTradeBankDic.setMoneyLimitDay(bankInfo.getMoneyLimitDay());
                    trdTradeBankDic.setMoneyLimitOne(bankInfo.getMoneyLimitOne());
                    logger.info("insert bankinfo for bankCode:{}", trdTradeBankDic.getBankCode());
                    trdTradeBankDicRepository.save(trdTradeBankDic);
                }
            });

        } catch (Exception e) {
            logger.error("error:", e);
        }
    }

    @Override
    public Map<String, Object> getBanklists() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TrdTradeBankDic> trdTradeBankDicsList = trdTradeBankDicRepository.findAll();
        result.put("result", trdTradeBankDicsList);
        return result;
    }


    /**
     */
    @Override
    public void getOrderDetailByParams(com.shellshellfish.aaas.finance.trade.order
        .GenOrderIdAndFundCode request, io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order.OrderDetailResult> responseObserver) {
        String orderId = request.getOrderId();

        String fundCode = request.getFundCode();
        Long userProdId = request.getUserProdId();
        Integer trdType = request.getTrdType();
        String applySerial = request.getApplySerial();
        if(!StringUtils.isEmpty(orderId) && !StringUtils.isEmpty(fundCode)){
            getOrderDetailByOrderIdAndFundCode(orderId, fundCode, responseObserver);
        }else if(!StringUtils.isEmpty(applySerial)){
            getOrderDetailByApplySerial(applySerial, responseObserver);
        } else{
            getOrderDetailByUserProdIdAndTrdType(userProdId, fundCode, trdType, responseObserver);
        }

    }
    private void getOrderDetailByApplySerial(String applySerial,
        StreamObserver<OrderDetailResult> responseObserver) {
        try{
            TrdOrderDetail trdOrderDetail = getOrderDetailByApplySerial(applySerial);
            OrderDetailResult.Builder odrBuilder = OrderDetailResult.newBuilder();
            OrderDetail.Builder odBuilder = OrderDetail.newBuilder();

            if(trdOrderDetail != null){
                MyBeanUtils.mapEntityIntoDTO(trdOrderDetail, odBuilder);
                odrBuilder.addOrderDetailResult(odBuilder);
            }
            responseObserver.onNext(odrBuilder.build());
            responseObserver.onCompleted();
        }catch (Exception ex){
            onError(responseObserver, ex);
        }
    }


    private void getOrderDetailByUserProdIdAndTrdType(Long userProdId, String fundCode, Integer
        trdType, StreamObserver<OrderDetailResult> responseObserver) {
        try{
            List<TrdOrderDetail> trdOrderDetails = getOrderDetailByUserProdIdAndTrdType(userProdId,
                fundCode, trdType);
            OrderDetailResult.Builder odrBuilder = OrderDetailResult.newBuilder();
            OrderDetail.Builder odBuilder = OrderDetail.newBuilder();

            if(!CollectionUtils.isEmpty(trdOrderDetails)){
                trdOrderDetails.forEach(
                    trdOrderDetail -> {
                        odBuilder.clear();
                        MyBeanUtils.mapEntityIntoDTO(trdOrderDetail, odBuilder);
                        odrBuilder.addOrderDetailResult(odBuilder);
                    }
                );
            }
            responseObserver.onNext(odrBuilder.build());
            responseObserver.onCompleted();
        }catch (Exception ex){
            onError(responseObserver, ex);
        }
    }

    private List<TrdOrderDetail> getOrderDetailByUserProdIdAndTrdType(Long userProdId, String fundCode, Integer trdType)
        throws IllegalAccessException {

        if(StringUtils.isEmpty(fundCode)){
            throw new IllegalAccessException(String.format("fundCode:%s",fundCode));
        }
        List<TrdOrderDetail> orderDetails = trdOrderDetailRepository.findAllByUserProdIdAndFundCodeAndTradeType
            (userProdId, fundCode, trdType);
        return orderDetails;
    }

    private void getOrderDetailByOrderIdAndFundCode(String orderId, String fundCode, StreamObserver<OrderDetailResult> responseObserver) {
        try {
            List<TrdOrderDetail> trdOrderDetails = getOrderDetailByGenOrderIdAndFundCode(orderId,
                fundCode);
            OrderDetailResult.Builder odrBuilder = OrderDetailResult.newBuilder();
            OrderDetail.Builder odBuilder = OrderDetail.newBuilder();

            if (!CollectionUtils.isEmpty(trdOrderDetails)) {
                trdOrderDetails.forEach(
                    trdOrderDetail -> {
                        odBuilder.clear();
                        MyBeanUtils.mapEntityIntoDTO(trdOrderDetail, odBuilder);
                        odrBuilder.addOrderDetailResult(odBuilder);
                    }
                );
            }
            responseObserver.onNext(odrBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            onError(responseObserver, ex);
        }
    }

    @Override
    public void getOrderDetailStatus(OrderDetailStatusRequest request,
                                     StreamObserver<OrderDetailStatusResponse> responseObserver) {

        try {
            String fundCode = request.getFundCode();
            long userProdId = request.getUserProdId();

            TrdOrderDetail trdOrderDetail = trdOrderDetailRepository.findTopByUserProdIdAndFundCodeOrderByBuysellDateDesc
                    (userProdId, fundCode);

            int orderDestalStatus = Integer.MIN_VALUE;
            if (trdOrderDetail != null)
                orderDestalStatus = trdOrderDetail.getOrderDetailStatus();

            OrderDetailStatusResponse.Builder builder = OrderDetailStatusResponse.newBuilder();
            builder.setStatus(orderDestalStatus);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error("获取orderDetail Status 出错{}", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("获取orderDetail 状态失败").asException());

        }
    }



    @Override
    public List<TrdOrderDetail> getOrderDetailByGenOrderIdAndFundCode(String orderId,
        String fundCode) throws IllegalAccessException {
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(fundCode)){
            throw new IllegalAccessException(String.format("orderId:%s fundCode:%s",orderId,
                fundCode));
        }
        List<TrdOrderDetail> orderDetails = trdOrderDetailRepository.findAllByOrderIdAndFundCode
            (orderId, fundCode);
        return orderDetails;
    }

    private void onError(StreamObserver responseObserver, Exception ex){
        responseObserver.onError(Status.INTERNAL
            .withDescription(ex.getMessage())
            .augmentDescription("orderGrpcException")
            .withCause(ex) // This can be attached to the Status locally, but NOT transmitted to
            // the client!
            .asRuntimeException());
    }

    @Override
    public TrdOrderDetail getOrderDetailByApplySerial(String applySerial) {

        TrdOrderDetail orderDetail = trdOrderDetailRepository.findByTradeApplySerial
            (applySerial);

        return orderDetail;
    }


    @Override
    public  Page<TrdOrder> getDefaultBankcardOrderByUserId(String userId, List<Long> bankCardList,
        Pageable pageable) {
        Page<TrdOrder> trdOrderList = trdOrderRepository
            .findDefaultOrderBankcard(userId, bankCardList, pageable);
        return trdOrderList;
    }

    @Override
    public Page<TrdOrderDetail> getFailedOrderDetail(int pageSize, int pageNo){
        Pageable pageable = new PageRequest(pageNo, pageSize);
        Page<TrdOrderDetail> trdOrderList = trdOrderDetailRepository.findFailedOrderinfo(pageable);
        return trdOrderList;
    }

    /**
     */
    @Override
    public void getFailedOrderDetails(com.shellshellfish.aaas.finance.trade.order.GetOrderDetailInfoByPage request,
        io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.order
            .OrderDetailPageResult> responseObserver) {
        try{
            Page<TrdOrderDetail> trdOrderDetails = getFailedOrderDetail(request.getPageSize(),
                request.getPageNo());
            OrderDetailPageResult.Builder odrBuilder = OrderDetailPageResult.newBuilder();
            odrBuilder.setTotalPages(trdOrderDetails.getTotalPages());
            OrderDetail.Builder odBuilder = OrderDetail.newBuilder();

            for(TrdOrderDetail trdOrderDetail: trdOrderDetails){
                odBuilder.clear();
                MyBeanUtils.mapEntityIntoDTO(trdOrderDetail, odBuilder);
                odrBuilder.addOrderDetailResult(odBuilder);
            }
            responseObserver.onNext(odrBuilder.build());
            responseObserver.onCompleted();
        }catch (Exception ex){

            logger.error("Error:", ex);
            onError(responseObserver, ex);
        }
    }

    @Override
    public void getFundsTradeLimit(FundQuerys request, StreamObserver<FundLimitResults> responseObserver) {
      try {
        List<FundCodeQuerys> querysList = request.getFundCodeQuerysList();
        int tradeType = request.getTradeType();
        List<String> fundCodeList=new ArrayList<>();
        for(FundCodeQuerys fundCodeQuery:querysList){
          fundCodeList.add(fundCodeQuery.getFundcode());
        }
        List<FundTradeLimitInfo> fundTradeInfoByFundcodes = fundInfoApiService
            .getFundTradeInfoByFundcodes(fundCodeList, tradeType);
        Builder builder = FundLimitResults.newBuilder();
        FundLimitDetail.Builder detailfBuilder = FundLimitDetail.newBuilder();
        for(FundTradeLimitInfo fundTradeLimitInfo:fundTradeInfoByFundcodes){
          detailfBuilder.setFundcode(fundTradeLimitInfo.getFundcode());
          detailfBuilder.setMinLimitValue(fundTradeLimitInfo.getMinLimitValue());
          detailfBuilder.setMinshare(fundTradeLimitInfo.getMinshare());
          builder.addFundLimitDetail(detailfBuilder);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
      }catch (Exception ex){
        logger.error("Error:", ex);
        onError(responseObserver, ex);
      }
    }

}

