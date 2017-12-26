package com.shellshellfish.aaas.finance.trade.pay.repositories;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdPayFlowRepository extends PagingAndSortingRepository<TrdPayFlow, Long> {
  @Override
  TrdPayFlow save(TrdPayFlow trdPayFlow);

  List<TrdPayFlow> findAllByFundSumConfirmedIsNull();

  List<TrdPayFlow> findAllByUserProdId(Long userProdId);

}
