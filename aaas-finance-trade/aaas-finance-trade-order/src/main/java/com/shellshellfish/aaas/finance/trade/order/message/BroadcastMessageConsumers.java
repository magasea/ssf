package com.shellshellfish.aaas.finance.trade.order.message;


import com.shellshellfish.aaas.common.constants.RabbitMQConstants;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    TradeOpService tradeOpService;

    @Autowired
    TrdOrderDetailRepository trdOrderDetailRepository;

    @Value("${spring.rabbitmq.topicOrder}")
    String topicOrder;



    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstants.QUEUE_ORDER_BASE + RabbitMQConstants.OPERATION_TYPE_UPDATE_ORDER, durable =
            "false"),
        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_ORDER )
    )
    public void receiveMessage(TrdPayFlow trdPayFlow) throws Exception {
        try{
            logger.info("Received fanout 1 message: " + trdPayFlow);
            logger.info("receiveMessageFromFanout1: " + trdPayFlow.getFundCode());
            Map<String, Object> trdOrderDetail = new HashMap<>();
            String tradeApplySerial =  trdPayFlow.getApplySerial();
            Long id = trdPayFlow.getOrderDetailId();
            Long buyFee = trdPayFlow.getBuyFee();
            Long updateBy =  SystemUserEnum.SYSTEM_USER_ENUM.getUserId();
            Long updateDate = TradeUtil.getUTCTime();
            Long fundNum = trdPayFlow.getFundSum() > 0? trdPayFlow.getFundSum(): null;
            Long fundNumConfirmed = trdPayFlow.getFundSumConfirmed() > 0 ? trdPayFlow
                .getFundSumConfirmed(): null;
            int orderDetailStatus = trdPayFlow.getTrdStatus();
            tradeOpService.updateByParam(tradeApplySerial, fundNum, fundNumConfirmed, updateDate,
                updateBy,  id, orderDetailStatus);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
    }


//    @RabbitListener(bindings = @QueueBinding(
//        value = @Queue(value = RabbitMQConstants.QUEUE_ORDER_BASE + RabbitMQConstants.OPERATION_TYPE_HANDLE_PREORDER, durable =
//            "false"),
//        exchange =  @Exchange(value = RabbitMQConstants.EXCHANGE_NAME, type = "topic",
//            durable = "true"),  key = RabbitMQConstants.ROUTING_KEY_PREORDER )
//    )
//    public void receivePreOrderMessage(TrdPayFlow trdPayFlow) throws Exception {
//        try{
//            logger.info("receivePreOrderMessage 1 message: with fundCode:" + trdPayFlow.getFundCode
//                () + " with preOrderId:" + trdPayFlow.getOrderDetailId());
//            tradeOpService.buyPreOrderProduct(trdPayFlow);
//        }catch (Exception ex){
//            ex.printStackTrace();
//            logger.error(ex.getMessage());
//        }
//    }
}
