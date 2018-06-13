package com.shellshellfish.aaas.finance.trade.order.repositories.mysql;


import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedNativeQueries;

public interface TrdOrderDetailRepository extends PagingAndSortingRepository<TrdOrderDetail, Long> {

    public static final String FIND_PENDING_ORDERINFO = "SELECT * from ssftrdorder"
        + ".trd_order_detail tod WHERE tod.order_detail_status != 2 AND tod.order_detail_status != 7 AND "
        + "tod.order_detail_status != -1 AND tod.order_detail_status != -4";
    public static final String COUNT_PENDING_ORDERINFO = "SELECT count(1) from ssftrdorder"
        + ".trd_order_detail tod WHERE tod.order_detail_status != 2 AND tod.order_detail_status != 7 AND "
        + "tod.order_detail_status != -1 AND tod.order_detail_status != -4";

    @Query(value = FIND_PENDING_ORDERINFO, countQuery = COUNT_PENDING_ORDERINFO,nativeQuery = true)
    Page<TrdOrderDetail> findPendingOrderinfo( Pageable pageable);


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
                               updateDate, @Param("updateBy") Long updateBy, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TrdOrderDetail SET trade_apply_serial = :tradeApplySerial, "
            + " order_detail_status = :orderDetailStatus ,  update_date = :updateDate, update_by = :updateBy WHERE id = :id")
    void updateByParamWithSerial(@Param("tradeApplySerial") String tradeApplySerial, @Param
            ("orderDetailStatus") int orderDetailStatus, @Param("updateDate") Long
                                         updateDate, @Param("updateBy") Long updateBy, @Param("id") Long id);

    List<TrdOrderDetail> findAllByOrderId(String orderId);

    TrdOrderDetail findTopByUserProdIdOrderByCreateDateDesc(Long userProdId);

    TrdOrderDetail findByTradeApplySerial(String tradeApplySerial);


    List<TrdOrderDetail> findAllByUserProdIdAndOrderDetailStatus(Long userProdId,
                                                                 Integer orderDetailStatus);

    List<TrdOrderDetail> findAllByUserProdId(Long userProdId);

    List<TrdOrderDetail> findAllByOrderIdAndFundCode(String orderId, String fundCode);

    List<TrdOrderDetail> findAllByUserProdIdAndFundCodeAndTradeType(Long userProdId, String
        fundCode, Integer tradeType);
}
