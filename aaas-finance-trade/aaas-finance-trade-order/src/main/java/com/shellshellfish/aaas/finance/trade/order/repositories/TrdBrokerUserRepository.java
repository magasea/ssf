package com.shellshellfish.aaas.finance.trade.order.repositories;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBroker;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenwei on 2017- 十二月 - 13
 */

public interface TrdBrokerUserRepository extends PagingAndSortingRepository<TrdTradeBroker, Long> {

  List<TrdBrokerUser> findTrdTradeBrokersByUserId(Long userId);

}
