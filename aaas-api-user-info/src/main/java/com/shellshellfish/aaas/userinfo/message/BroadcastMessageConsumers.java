package com.shellshellfish.aaas.userinfo.message;


import com.shellshellfish.aaas.common.message.order.TrdLog;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);




    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "${spring.rabbitmq.topicQueueOrderName}", durable = "false"),
        exchange =  @Exchange(value = "${spring.rabbitmq.topicExchangeName}", type = "topic",
            durable = "true"),  key = "${spring.rabbitmq.topicUserinfo}")
    )
    public void receiveMessage(TrdPayFlow trdPayFlow) throws Exception {
        logger.info("Received fanout 1 message: " + trdPayFlow);
        //update ui_products 和 ui_product_details
        UiProducts uiProducts = new UiProducts();
        uiProducts.setId(trdPayFlow.getUserProdId());
        uiProducts.setStatus(trdPayFlow.getPayStatus());
        UiProductDetail uiProductDetail = new UiProductDetail();
        
    }

}
