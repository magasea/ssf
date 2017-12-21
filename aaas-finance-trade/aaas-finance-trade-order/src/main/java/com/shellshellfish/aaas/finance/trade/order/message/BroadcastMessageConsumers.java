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
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    OrderService orderService;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;


    @Value("${spring.rabbitmq.topicOrder}")
    String topicOrder;


    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "${spring.rabbitmq.topicQueuePayName}", durable = "false"),
        exchange =  @Exchange(value = "${spring.rabbitmq.topicExchangeName}", type = "topic",
            durable = "true"),  key = "${spring.rabbitmq.topicOrder}")
    )
    public void receiveMessage(TrdPayFlow trdPayFlow) throws Exception {
        try{
            logger.info("Received fanout 1 message: " + trdPayFlow);
            logger.info("receiveMessageFromFanout1: " + trdPayFlow.getFundCode());
            TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
            trdOrderDetail.setId(trdPayFlow.getOrderDetailId());
            trdOrderDetail.setBuyFee(trdPayFlow.getBuyFee());
            trdOrderDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
            trdOrderDetail.setUpdateDate(TradeUtil.getUTCTime());
            trdOrderDetail.setOrderDetailStatus(trdPayFlow.getPayStatus());
            trdOrderDetailRepository.save(trdOrderDetail);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

}
