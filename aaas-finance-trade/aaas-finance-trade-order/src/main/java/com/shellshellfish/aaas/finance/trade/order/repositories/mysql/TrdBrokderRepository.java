package com.shellshellfish.aaas.finance.trade.order.repositories.mysql;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBroker;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdBrokderRepository extends PagingAndSortingRepository<TrdTradeBroker, Long> {

  @Override
  Optional<TrdTradeBroker> findById(Long id);

}
