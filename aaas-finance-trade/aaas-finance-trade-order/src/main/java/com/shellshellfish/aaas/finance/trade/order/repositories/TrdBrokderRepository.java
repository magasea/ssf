package com.shellshellfish.aaas.finance.trade.order.repositories;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBroker;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdBrokderRepository extends PagingAndSortingRepository<TrdTradeBroker, Long> {

  @Override
  TrdTradeBroker findOne(Long id);

}
