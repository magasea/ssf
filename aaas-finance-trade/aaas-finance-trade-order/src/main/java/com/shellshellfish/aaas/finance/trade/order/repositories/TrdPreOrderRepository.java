package com.shellshellfish.aaas.finance.trade.order.repositories;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdPreOrder;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TrdPreOrderRepository extends PagingAndSortingRepository<TrdPreOrder, Long> {

  @Override
  TrdPreOrder save(TrdPreOrder trdPreOrder);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE TrdPreOrder SET order_status = "
      + ":orderStatus , err_msg = :errMsg,  update_date = :updateDate, update_by = :updateBy "
      + "WHERE id = :id")
  void updateByParam(@Param("orderStatus") int orderStatus,@Param("errMsg") String errMsg, @Param
      ("updateDate") Long updateDate, @Param("updateBy") Long updateBy, @Param("id") Long id);

  List<TrdOrderDetail> findAllByOrderId(String orderId);

//  TrdOrderDetail updateByParam(Map param);
}
