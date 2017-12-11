package com.shellshellfish.aaas.finance.trade.order.message;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class BroadcastMessageProducerTest {

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;

  @Test
  public void sendMessages() throws Exception {
    TrdOrderPay trdOrderPay = new TrdOrderPay();
    broadcastMessageProducer.sendMessages(trdOrderPay);
  }

}