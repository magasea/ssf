package com.shellshellfish.aaas.finance.trade.pay.repositories.mongo;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.mongo.MongoFundNetInfo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by chenwei on 2018- 一月 - 31
 */
public interface MongoFundNetInfoRepo extends MongoRepository<MongoFundNetInfo, Long> {


  MongoFundNetInfo findByFundCodeAndTradeDate(String fundCode, String tradeDate);

  @Query("{$limit:1}")
  MongoFundNetInfo findByFundCode(String fundCode);

  @Query("{'fund_code': ?#{[0]},{$limit:?#{[1]}}}")
  List<MongoFundNetInfo> findByFundCodeAndDays(List<String> fundCode, int days);

}
