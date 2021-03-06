package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.finance.trade.order.message.BroadcastMessageProducer;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdBrokderRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class TradeOpServiceImplTest {

  Logger logger = LoggerFactory.getLogger(TradeOpServiceImplTest.class);

  @Autowired
  FinanceProdInfoService financeProdInfoService;
  @Autowired
  TradeOpService tradeOpService;

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdBrokderRepository trdBrokderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Autowired
  BroadcastMessageProducer broadcastMessageProducer;


  @Test
  public void buyFinanceProduct() throws Exception {
    ProductBaseInfo productBaseInfo = new ProductBaseInfo();
    productBaseInfo.setGroupId(1L);
    productBaseInfo.setProdId(1L);
    List<ProductMakeUpInfo> productMakeUpInfos =  financeProdInfoService.getFinanceProdMakeUpInfo
        (productBaseInfo);
    FinanceProdBuyInfo financeProdBuyInfo = new FinanceProdBuyInfo();
    financeProdBuyInfo.setBankAcc("62127649173401236041");
    financeProdBuyInfo.setGroupId(1L);
    financeProdBuyInfo.setMoney(BigDecimal.valueOf(100000L));
    financeProdBuyInfo.setProdId(1L);
    financeProdBuyInfo.setOrderType(TrdOrderOpTypeEnum.BUY.ordinal());
    financeProdBuyInfo.setUserId(11L);
    TrdOrder trdOrder = tradeOpService.buyFinanceProduct(financeProdBuyInfo);
    logger.info("trdOrder:" + trdOrder);

  }

}