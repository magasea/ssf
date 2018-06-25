package com.shellshellfish.aaas.finance.trade.order.repositories.mysql;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

import java.util.List;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
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
			updateDate, @Param("updateBy") Long updateBy, @Param("orderId") Long orderId);

	@QueryHints(value = { @QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
	@Query(value = "SELECT * FROM trd_order t where t.bank_card_num in (:bankCardList) and t.user_id=:userId ORDER BY order_date desc ",nativeQuery = true )
	Page<TrdOrder> findDefaultOrderBankcard(@Param("userId")String userId,@Param("bankCardList") List<Long> bankCardList, Pageable pageable);


	List<TrdOrder> findByUserProdIdAndUserId(Long prodId, Long userId);

	TrdOrder findFirstByUserProdIdAndOrderStatus(Long userProdId, Integer orderStatus);



}
