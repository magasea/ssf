package com.shellshellfish.aaas.finance.trade.pay.repositories;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdPayFlowRepository extends PagingAndSortingRepository<TrdPayFlow, Long> {
  @Override
  TrdPayFlow save(TrdPayFlow trdPayFlow);
}
