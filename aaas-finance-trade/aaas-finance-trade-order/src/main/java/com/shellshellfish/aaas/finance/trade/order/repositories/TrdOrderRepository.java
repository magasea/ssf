package com.shellshellfish.aaas.finance.trade.order.repositories;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdOrderRepository extends PagingAndSortingRepository<TrdOrder, Long> {

  @Override
  TrdOrder save(TrdOrder newOrder);
}