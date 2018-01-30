package com.shellshellfish.aaas.finance.trade.order.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import org.springframework.data.repository.query.Param;

public interface TrdOrderRepository extends PagingAndSortingRepository<TrdOrder, Long> {

	@Override
	TrdOrder save(TrdOrder newOrder);

	List<TrdOrder> findTrdOrdersByUserId(Long userId);

	TrdOrder findByOrderId(String orderId);

	TrdOrder findByPreOrderId(Long preOrderId);

	List<TrdOrder> findByUserProdId(Long userProdId);

	List<TrdOrder> findTrdOrdersByOrderStatusIs(int orderStatus);
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TrdOrder SET order_status = :orderStatus, update_date = :updateDate, update_by "
			+ "= :updateBy WHERE id = :orderId")
	TrdOrder updateOrderStatus(@Param("orderStatus") int orderStatus, @Param("updateDate") long
			updateDate, @Param("updateBy") Long updateBy, @Param("orderId") Long orderId );

	List<TrdOrder> findByUserProdIdAndUserId(Long prodId, Long userId);

}
