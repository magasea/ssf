package com.shellshellfish.aaas.finance.trade.order.message;


import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;

import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    OrderService orderService;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;

    public void receiveMessage(GenericMessage message) throws Exception {
        logger.info("Received fanout 1 message: " + message);

        if(TrdPayFlow.class.isInstance(message.getPayload())){
            TrdPayFlow trdPayFlow = (TrdPayFlow) message.getPayload();
            logger.info("receiveMessageFromFanout1: " + trdPayFlow.getFundCode());
            TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
            trdOrderDetail.setId(trdPayFlow.getOrderDetailId());
            trdOrderDetail.setBuyFee(trdPayFlow.getBuyFee());
            trdOrderDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
            trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setOrderDetailStatus(TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());
            trdOrderDetailRepository.save(trdOrderDetail);
        }else{
            logger.info("receiveMessageFromFanout1: " + message);
        }



//        orderService.notifyPay(trdPayFlowResult);
    }

}
