package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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



    /**
     * 定时检查是否有订单状态为等待支付超过1个小时是的话发起支付
     */
    public void executeSampleJob() {

        List<TrdOrder> trdOrderList =  trdOrderRepository.findTrdOrdersByOrderStatusIsNot
            (TrdOrderStatusEnum.WAITPAY.getStatus());

        if(!CollectionUtils.isEmpty(trdOrderList)){
            logger.error("there is some order need to pay by scheduler");
        }else{
            return;
        }
        for(TrdOrder trdOrder: trdOrderList){
            processOrderInJob(trdOrder);
        }

    }

    private void processOrderInJob(TrdOrder trdOrder) {
        PayDto payDto = new PayDto();
        List<TrdOrderDetail> orderDetailList = new ArrayList<>();
        payDto.setUserProdId(trdOrder.getUserProdId());
//        payDto.setTrdBrokerId();
        String userUUID = tradeOpService.getUserUUIDByUserId(trdOrder.getUserId());
        payDto.setUserUuid(userUUID);
        TrdBrokerUser trdBrokerUser = tradeOpService.getBrokerUserByUserIdAndBandCard(trdOrder
            .getUserId(), trdOrder.getBankCardNum());
        payDto.setTrdBrokerId(trdBrokerUser.getTradeBrokerId().intValue());
        payDto.setTrdAccount(trdBrokerUser.getTradeAcco());
        List<com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail>
            orderDetailsInDB = trdOrderDetailRepository.findAllByOrderId(trdOrder.getOrderId());
        for(com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail trdOrderDetailDb: orderDetailsInDB){
            if(trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.WAITPAY.getStatus() ||
                trdOrderDetailDb.getOrderDetailStatus() == TrdOrderStatusEnum.WAITSELL.getStatus()){
                if(trdOrderDetailDb.getCreateDate() > TradeUtil.getUTCTimeHoursBefore(6) ){
                    logger.error("this orderDetail in DB with orderDetail id:"+ trdOrderDetailDb
                        .getId() +" is out of time to retry, just let it go");
                    continue;
                }
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                BeanUtils.copyProperties(trdOrderDetailDb, trdOrderDetail);
                orderDetailList.add(trdOrderDetail);
            }
        }
        payDto.setOrderDetailList(orderDetailList);
    }
}
