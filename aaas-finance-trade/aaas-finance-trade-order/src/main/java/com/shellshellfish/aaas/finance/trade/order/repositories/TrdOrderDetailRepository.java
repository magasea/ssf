package com.shellshellfish.aaas.finance.trade.order.repositories;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdOrderDetailRepository extends PagingAndSortingRepository<TrdOrderDetail, Long> {

  @Override
  TrdOrderDetail save(TrdOrderDetail newOrderDetail);
}
