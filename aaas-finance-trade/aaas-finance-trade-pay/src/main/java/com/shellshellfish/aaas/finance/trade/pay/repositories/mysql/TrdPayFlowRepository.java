package com.shellshellfish.aaas.finance.trade.pay.repositories.mysql;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdPayFlowRepository extends PagingAndSortingRepository<TrdPayFlow, Long> {
  @Override
  TrdPayFlow save(TrdPayFlow trdPayFlow);

  List<TrdPayFlow> findAllByTradeConfirmSumIsAndTrdTypeIs(Long tradeConfirmSum,  int trdType);
  List<TrdPayFlow> findAllByTradeConfirmShareIsAndTrdTypeIs(Long tradeConfirmShare,  int trdType);

  List<TrdPayFlow> findAllByTradeConfirmShareIsAndTrdTypeIsAndTrdStatusIsGreaterThan(Long
      tradeConfirmShare,
      int trdType, int trdStatus );

  List<TrdPayFlow> findAllByUserProdId(Long userProdId);

//  List<TrdPayFlow> findAllByOrderDetailId(Long orderDetailId);

  List<TrdPayFlow> findAllByOutsideOrderno(String outsideOrderno);

}
