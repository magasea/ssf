package com.shellshellfish.aaas.finance.trade.pay.message;


import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    PayService payService;

    @Autowired
    TrdPayFlowRepository trdPayFlowRepository;

    public void receiveMessage(GenericMessage message) throws Exception {
        if(TrdOrderDetail.class.isInstance(message.getPayload())) {
            TrdOrderDetail trdOrderPay = (TrdOrderDetail) message
                .getPayload();
            TrdPayFlow trdPayFlow = payService.payOrder(trdOrderPay);
            TrdPayFlow trdPayFlowResult =  trdPayFlowRepository.save(trdPayFlow);
            payService.notifyPay(trdPayFlowResult);
        }else{
            logger.info("receiveMessageFromFanout1: " + message);
        }

    }

}
