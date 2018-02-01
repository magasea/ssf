package com.shellshellfish.aaas.finance.trade.order.repositories;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBroker;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by chenwei on 2017- 十二月 - 13
 */

public interface TrdBrokerUserRepository extends PagingAndSortingRepository<TrdBrokerUser, Long> {

  List<TrdBrokerUser> findByUserId(Long userId);


  TrdBrokerUser findByUserIdAndBankCardNum(Long userId, String bankCardNum);

  @Modifying
  @Query("UPDATE TrdBrokerUser SET trade_acco = :trdAcco,  update_date = :updateDate, update_by ="
      + " :updateBy WHERE user_id = :userId")
  int updateTradeAcco(@Param("trdAcco") String trdAcco, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("userId") Long userId);

  @Override
  TrdBrokerUser save(TrdBrokerUser trdBrokerUser);

  TrdBrokerUser findByTradeAccoAndTradeBrokerId(String tradeAcco, int tradeBrokerId);
}
