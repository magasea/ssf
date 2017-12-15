package com.shellshellfish.aaas.finance.trade.order.message;


import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    OrderService orderService;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;

//    public void receiveMessage( trdOrderPay) throws Exception {
//        logger.info("Received fanout 1 message: " + trdOrderPay);
//
////        orderService.notifyPay(trdPayFlowResult);
//    }

}
