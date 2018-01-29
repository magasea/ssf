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

  @Modifying(clearAutomatically = true)
  @Query("UPDATE TrdOrderDetail SET trade_apply_serial = :tradeApplySerial, fu"
      + "nd_sum = :fundSum,fund_sum_confirmed = :fundSumConfirmed, fund_num = :fundNum, "
      + "fund_num_confirmed = :fundNumConfirmed, order_detail_status = "
      + ":orderDetailStatus ,  update_date = :updateDate, update_by = :updateBy WHERE id = :id")
  void updateByParam(@Param("tradeApplySerial") String tradeApplySerial, @Param("fundSum") long
      fundSum, @Param("fundSumConfirmed") long fundSumConfirmed, @Param("fundNum")
      long fundNum, @Param("fundNumConfirmed") long fundNumConfirmed, @Param
      ("orderDetailStatus") int orderDetailStatus, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE TrdOrderDetail SET trade_apply_serial = :tradeApplySerial, "
      + " order_detail_status = :orderDetailStatus ,  update_date = :updateDate, update_by = :updateBy WHERE id = :id")
  void updateByParamWithSerial(@Param("tradeApplySerial") String tradeApplySerial, @Param
      ("orderDetailStatus") int orderDetailStatus, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("id") Long id);

  List<TrdOrderDetail> findAllByOrderId(String orderId);

  TrdOrderDetail findByTradeApplySerial(String tradeApplySerial);

//  TrdOrderDetail updateByParam(Map param);
}
