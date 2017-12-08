package com.shellshellfish.aaas.finance.trade.pay.message;


import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    public void receiveMessage(TrdOrderPay trdOrderPay) {
        logger.info("Received fanout 1 message: " + trdOrderPay);
    }

}
