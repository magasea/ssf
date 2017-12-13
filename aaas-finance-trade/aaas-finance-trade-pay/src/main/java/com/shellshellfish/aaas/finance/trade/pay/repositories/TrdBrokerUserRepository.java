package com.shellshellfish.aaas.finance.trade.pay.repositories;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenwei on 2017- 十二月 - 13
 */

public interface TrdBrokerUserRepository extends PagingAndSortingRepository<TrdBrokerUser, Long> {



  TrdBrokerUser findByUserId(Long userId);
}
