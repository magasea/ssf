package com.shellshellfish.aaas.finance.trade.order.repositories;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TrdOrderDetailRepository extends PagingAndSortingRepository<TrdOrderDetail, Long> {

  @Override
  TrdOrderDetail save(TrdOrderDetail newOrderDetail);

  @Modifying
  @Query("UPDATE TrdOrderDetail SET trade_apply_serial = :tradeApplySerial, order_status = "
      + ":orderDetailStatus ,  update_date = :updateDate, update_by = :updateBy WHERE id = :id")
  int updateByParam(@Param("tradeApplySerial") String tradeApplySerial, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("id") Long id, @Param
      ("orderDetailStatus") int orderDetailStatus);

  List<TrdOrderDetail> findAllByOrderId(String orderId);

//  TrdOrderDetail updateByParam(Map param);
}
