package com.shellshellfish.aaas.finance.trade.pay.repositories;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdPayFlowRepository extends PagingAndSortingRepository<TrdPayFlow, Long> {
  @Override
  TrdPayFlow save(TrdPayFlow trdPayFlow);

  List<TrdPayFlow> findAllByFundSumConfirmedIsAndTrdTypeIs(Long fundSumConfirmed,  int trdType);

  List<TrdPayFlow> findAllByUserProdId(Long userProdId);

//  List<TrdPayFlow> findAllByOrderDetailId(Long orderDetailId);

  List<TrdPayFlow> findAllByOutsideOrderno(String outsideOrderno);

}
