package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.OrderStatusChangeDTO;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class CheckFundsOrderJobService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TrdOrderRepository trdOrderRepository;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;


    @Autowired
    TradeOpService tradeOpService;

    @Autowired
    PayService payService;

    @Autowired
    BroadcastMessageProducer broadcastMessageProducer;

    /**
     * 定时检查是否有订单状态为等待支付超过1个小时是的话发起支付
     */
    public void executeSampleJob() {

        List<TrdOrder> trdOrderList =  trdOrderRepository.findTrdOrdersByOrderStatusIs
            (TrdOrderStatusEnum.WAITPAY.getStatus());

        List<TrdOrder> trdOrderListWaitSell = trdOrderRepository.findTrdOrdersByOrderStatusIs
            (TrdOrderStatusEnum.WAITSELL.getStatus());
        if(!CollectionUtils.isEmpty(trdOrderList)){
            logger.error("there is some order need to pay by scheduler");
            for(TrdOrder trdOrder: trdOrderList){
                processOrderInJob(trdOrder);
            }
        }else if(!CollectionUtils.isEmpty(trdOrderListWaitSell)){
            logger.error("there is some order need to pay by scheduler");
            for(TrdOrder trdOrder: trdOrderListWaitSell){
                processOrderInJob(trdOrder);
            }
        }
    }

    private void processOrderInJob(TrdOrder trdOrder) {
        PayOrderDto payOrderDto = new PayOrderDto();
        List<TrdOrderDetail> orderDetailList = new ArrayList<>();
        payOrderDto.setUserProdId(trdOrder.getUserProdId());
        String userUUID = tradeOpService.getUserUUIDByUserId(trdOrder.getUserId());
        payOrderDto.setUserUuid(userUUID);
        TrdBrokerUser trdBrokerUser = tradeOpService.getBrokerUserByUserIdAndBandCard(trdOrder
            .getUserId(), trdOrder.getBankCardNum());
        payOrderDto.setTrdBrokerId(trdBrokerUser.getTradeBrokerId().intValue());
        payOrderDto.setTrdAccount(trdBrokerUser.getTradeAcco());
        List<com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail>
            orderDetailsInDB = trdOrderDetailRepository.findAllByOrderId(trdOrder.getOrderId());
        boolean allFinished = true;
        boolean allPayOrSelled = true;
        boolean needCancel = true;

        for(com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail trdOrderDetailDb: orderDetailsInDB){
            //先忽略那种 本来就没有分别份额的基金，这种基金交易状态是失败也很正常
            if(trdOrderDetailDb.getFundShare() == 0){
                logger.info("this suborder will be ignored for:" + trdOrderDetailDb.getId());
                continue;
            }
            if((trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus
                ()) && allFinished){
                allFinished = true;
            }
            if((trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus
                ()||trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM
                .getStatus()|| trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum
                .CONFIRMED.getStatus()) && allPayOrSelled){
                allPayOrSelled = true;
            }
            if((trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.FAILED
                .getStatus()) && needCancel){
                needCancel = true;
            }
            if(trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.WAITPAY.getStatus() ||
                trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()){
                if(trdOrderDetailDb.getCreateDate() < TradeUtil.getUTCTimeTodayStartTime(
                    ZoneId.systemDefault().getId())){
                    logger.error("this orderDetail in DB with orderDetail id:"+ trdOrderDetailDb
                        .getId() +" is out of time to retry, just let it go");
                    allFinished = false;
                    needCancel = true;
                    continue;
                }
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                BeanUtils.copyProperties(trdOrderDetailDb, trdOrderDetail);
                orderDetailList.add(trdOrderDetail);
                allFinished = false;
            }
        }
        if(allFinished){
            logger.info("this order's all orderDetail is finished, need update order status");
            trdOrderRepository.updateOrderStatus(TrdOrderStatusEnum.CONFIRMED.getStatus(),
                TradeUtil.getUTCTime(), SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdOrder
                    .getId());
            return;
        }else if(allPayOrSelled && trdOrder.getOrderType() == TrdOrderOpTypeEnum.BUY.getOperation()){

            trdOrderRepository.updateOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus(),
                TradeUtil.getUTCTime(), SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdOrder
                    .getId());
            OrderStatusChangeDTO orderStatusChangeDTO = new OrderStatusChangeDTO();
            orderStatusChangeDTO.setOrderDate(trdOrder.getOrderDate());
            orderStatusChangeDTO.setOrderStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
            orderStatusChangeDTO.setOrderType(TrdOrderOpTypeEnum.BUY.getOperation());
            orderStatusChangeDTO.setUserId(trdOrder.getUserId());

            broadcastMessageProducer.sendOrderStatusChangeMessages(orderStatusChangeDTO);
        }else if(allPayOrSelled && trdOrder.getOrderType() == TrdOrderOpTypeEnum.REDEEM.getOperation()){
            trdOrderRepository.updateOrderStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus(),
                TradeUtil.getUTCTime(), SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), trdOrder
                    .getId());
            OrderStatusChangeDTO orderStatusChangeDTO = new OrderStatusChangeDTO();
            orderStatusChangeDTO.setOrderDate(trdOrder.getOrderDate());
            orderStatusChangeDTO.setOrderStatus(TrdOrderStatusEnum.SELLWAITCONFIRM.getStatus());
            orderStatusChangeDTO.setOrderType(TrdOrderOpTypeEnum.REDEEM.getOperation());
            orderStatusChangeDTO.setUserId(trdOrder.getUserId());

            broadcastMessageProducer.sendOrderStatusChangeMessages(orderStatusChangeDTO);
        }
        else{
            //需要进行再让交易系统发起交易
            payOrderDto.setOrderDetailList(orderDetailList);
//            OrderPayReq.Builder reqBuilder = OrderPayReq.newBuilder();
//            BeanUtils.copyProperties(payOrderDto, reqBuilder);
            if(CollectionUtils.isEmpty(payOrderDto.getOrderDetailList()) ||  payOrderDto.getOrderDetailList()
                .size() <= 0){
                logger.info("this job find no orderDetail list to be handled by grpc");
                return;
            }else{
                payService.order2PayJob(payOrderDto);

//                OrderDetailPayReq.Builder ordDetailReqBuilder = OrderDetailPayReq.newBuilder();
//                for(TrdOrderDetail trdOrderDetail: payOrderDto.getOrderDetailList()){
//                    BeanUtils.copyProperties(trdOrderDetail, ordDetailReqBuilder);
//                    reqBuilder.addOrderDetailPayReq(ordDetailReqBuilder.build());
//                    ordDetailReqBuilder.clear();
//                }
            }
//            try {
//                payRpcServiceFutureStub.order2Pay(reqBuilder.build()).get().getResult();
//            } catch (InterruptedException e) {
//                logger.error("failed to handling payRpcServiceFutureStub.orderJob2Pay：" + e.getMessage());
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                logger.error("failed to handling payRpcServiceFutureStub.orderJob2Pay：" + e.getMessage());
//                e.printStackTrace();
//            }

        }

    }
}
