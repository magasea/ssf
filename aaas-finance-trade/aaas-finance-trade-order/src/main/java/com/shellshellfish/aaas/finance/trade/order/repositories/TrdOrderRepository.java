package com.shellshellfish.aaas.finance.trade.order.repositories;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;

public interface TrdOrderRepository extends PagingAndSortingRepository<TrdOrder, Long> {

	@Override
	TrdOrder save(TrdOrder newOrder);

	List<TrdOrder> findTrdOrdersByUserId(Long userId);

	TrdOrder findByOrderId(String orderId);

	List<TrdOrder> findByUserProdId(Long userProdId);

	List<TrdOrder> findTrdOrdersByOrderStatusIsNot(int orderStatus);
}
