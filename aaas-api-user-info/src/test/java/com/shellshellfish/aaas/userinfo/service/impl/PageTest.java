package com.shellshellfish.aaas.userinfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.dao.service.impl.UserInfoRepoServiceImpl;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
public class PageTest {

  @Autowired
  MongoTemplate mongoTemplate;
  @Autowired
  UserInfoRepoService userInfoRepoService;

  @Autowired
  MongoUserTrdLogMsgRepo mongoUserTrdLogMsgRepo;

  @Autowired
  UserInfoServiceImpl userInfoService;

  @Test
  public void mytest() {
    long t1 = System.currentTimeMillis();
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("user_id").is(5628)),
        Aggregation.match(Criteria.where("order_id").exists(true)),
        Aggregation.group("order_id").last("trade_date").as("trade_date"),
        Aggregation.sort(Direction.DESC, "trade_date"),
        Aggregation.skip(0L),
        Aggregation.limit(80)

    );
    AggregationResults<Document> aggregate = mongoTemplate
        .aggregate(aggregation, "ui_trdlog", Document.class);
    List<Document> mappedResults = aggregate.getMappedResults();
    List<String> list = new LinkedList<>();
    Map<String, Long> map1 = new HashMap<>();
    //  int size = mappedResults.size();
    //System.out.println(mappedResults);
    for (Document document : mappedResults) {
      String id = document.get("_id", String.class);
      Long trade_date = document.get("trade_date", Long.class);
      System.out.println("_id:" + id + ":" + trade_date);
      list.add(id);
      map1.put(id, trade_date);
    }
    System.out.println(list.size());
    System.out.println(Arrays.toString(list.toArray()));
    List<MongoUiTrdLogDTO> byOrderIdIn = userInfoRepoService.findByOrderIdIn(map1.keySet());
    Collections.sort(byOrderIdIn, new Comparator<MongoUiTrdLogDTO>() {
      @Override
      public int compare(MongoUiTrdLogDTO o1, MongoUiTrdLogDTO o2) {
        return o2.getTradeDate().compareTo(o1.getTradeDate());
      }
    });

    //byOrderIdIn.forEach(mongoUiTrdLogDTO -> System.out.println(mongoUiTrdLogDTO));

    //System.out.println(byOrderIdIn.size());
    // System.out.println(Arrays.toString(list.toArray()));
    // System.out.println(mongoUserTrdLogMsgRepo == null);
    //List<MongoUiTrdLog> byOrderIdIn = mongoUserTrdLogMsgRepo.findByOrderIdIn(list);
    //System.out.println(byOrderIdIn.size());
    Map<String, Map<String, Object>> stringMapMap = userInfoService
        .combineTradeLogsToFunds(byOrderIdIn);

    String s = JSON.toJSONString(stringMapMap, true);
    //  System.out.println(s);
    final List<Map<String, Object>> tradeLogs = new ArrayList<>();
    userInfoService.combineFundsToProduct(stringMapMap, tradeLogs);
 /*   String s1 = JSON.toJSONString(tradeLogs, true);
    System.out.println(tradeLogs.size());
    System.out.println(s1);*/
    System.out.println("--------------------------------------------------------");
    //System.out.println(tradeLogs.size());
    for (Map<String, Object> log : tradeLogs) {
      String orderId = (String) log.get("orderId");
      Long aLong = map1.get(orderId);
      System.out.println(aLong);
      String dateString = TradeUtil.getReadableDateTime(aLong).split("T")[0];
      log.put("date", dateString);
      log.put("dateLong", aLong / 1000);
      System.out.println(dateString);
      log.remove("orderId");
    }
    Collections.sort(tradeLogs, (o1, o2) -> {
      Long map1value = (Long) o1.get("dateLong");
      Long map2value = (Long) o2.get("dateLong");
      return map2value.compareTo(map1value);
    });

    System.out.println("--------------------------------------------------------");
    System.out.println(JSON.toJSONString(tradeLogs, true));
    System.out.println(tradeLogs.size());
    System.out.println("--------------------------------------------------------");
    //System.out.println(stringMapMap.size());
    //System.out.println(System.currentTimeMillis() - t1);
    //stringMapMap.forEach((s, stringObjectMap) -> System.out.println(s));

  }

  @Test
  public void testProductName() throws InstantiationException, IllegalAccessException {
    Map<String, String> allProducts = userInfoRepoService.findAllProducts();
    System.out.println(allProducts.size());
    allProducts.forEach((s, s2) -> System.out.println(s2));
  }

  @Test
  public void version3() {
    Long t1 = System.currentTimeMillis();
    String uuid = "3a4401ae-d6f9-49ee-97e2-ce7ebf122822";
    Map<String, Object> tradLogsOfUser3 = userInfoService.getTradLogsOfUser3(uuid, 10, 2, 0);
    System.out.println(
        "------------------------------------------------------------------------------------------------");
    System.out.println(System.currentTimeMillis() - t1);
    System.out.println(
        "------------------------------------------------------------------------------------------------");
    String s = JSON.toJSONString(tradLogsOfUser3, true);
    System.out.println(s);
  }

  @Test
  public void getTotal() {
    System.out.println("=====================================================================");
    int total = userInfoService.getTotal(2, 5628L);
    System.out.println(total);
    System.out.println("=====================================================================");
  }


}
