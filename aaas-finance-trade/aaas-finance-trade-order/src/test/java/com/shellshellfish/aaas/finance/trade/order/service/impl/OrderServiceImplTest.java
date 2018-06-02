package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by developer4 on 2018- 五月 - 28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class OrderServiceImplTest {

  @Autowired
  OrderService orderService;
  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Test
  public void syncBankInfos() {
    orderService.syncBankInfos();

  }

  @Test
  public void mytest() {
    List<TrdOrderDetail> result = null;
    result = trdOrderDetailRepository
        .findAllByUserProdId(159L);
    if (result != null && result.size() > 0) {
      Collections.sort(result, (o1, o2) -> {
        Long map1value = o1.getBuysellDate();
        Long map2value = o2.getBuysellDate();
        return map2value.compareTo(map1value);
      });
      String orderId = result.get(0).getOrderId();
      List<TrdOrderDetail> filterCollect = result.stream()
          .filter(filter -> orderId.equals(filter.getOrderId())).collect(Collectors.toList());
      result = filterCollect;
    }

    String s = JSON.toJSONString(result, true);
    System.out.println(s);
    System.out.println(result.size());

  }
}
