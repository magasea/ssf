package com.shellshellfish.aaas.finance.trade.pay.message;



import com.rabbitmq.client.Channel;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

    @Autowired
    PayService payService;

    private final CountDownLatch latch = new CountDownLatch(1);



    @RabbitListener( containerFactory = "jsaFactory",bindings = @QueueBinding(
        value = @Queue(value = "${spring.rabbitmq.topicQueueOrderName}", durable = "false"),
        exchange =  @Exchange(value = "${spring.rabbitmq.topicExchangeName}", type = "topic",
            durable = "true"),  key = "${spring.rabbitmq.topicPay}")
    )
    public void receiveMessage(PayDto message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)
        long tag) {
        try {
            PayDto payDto = payService.payOrder(message);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        try {
            channel.basicAck(tag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

}
