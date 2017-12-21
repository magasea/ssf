package com.shellshellfish.aaas.finance.trade.order.repositories;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import java.util.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TrdOrderDetailRepository extends PagingAndSortingRepository<TrdOrderDetail, Long> {

  @Override
  TrdOrderDetail save(TrdOrderDetail newOrderDetail);

  @Modifying
  @Query("UPDATE trd_order_detail tod SET tod.trade_apply_serial = :tradeApplySerial, tod"
      + ".order_status = :orderStatus , tod.update_date = :updateDate, tod.update_by = :updateBy "
      + "WHERE tod.id = :id")
  int updateAddress(@Param("tradeApplySerial") String tradeApplySerial, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("id") Long id);
  TrdOrderDetail updateByParam(Map param);
}
