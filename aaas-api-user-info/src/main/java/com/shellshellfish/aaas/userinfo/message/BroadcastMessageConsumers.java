package com.shellshellfish.aaas.userinfo.message;


import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.message.order.TrdLog;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "${spring.rabbitmq.topicQueuePayName}", durable = "false"),
        exchange =  @Exchange(value = "${spring.rabbitmq.topicExchangeName}", type = "topic",
            durable = "true"),  key = "${spring.rabbitmq.topicUserinfo}")
    )
    public void receiveMessage(TrdPayFlow trdPayFlow, Channel channel, @Header(AmqpHeaders
        .DELIVERY_TAG) long tag) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        try{
            uiProductDetailRepo.updateByParam(trdPayFlow.getFundSum(), TradeUtil.getUTCTime(),
                trdPayFlow.getUserId(),trdPayFlow.getUserProdId() ,trdPayFlow.getFundCode());
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
        
    }

}
