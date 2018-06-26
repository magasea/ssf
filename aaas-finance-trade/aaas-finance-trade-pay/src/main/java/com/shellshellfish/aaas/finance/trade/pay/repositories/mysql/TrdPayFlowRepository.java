package com.shellshellfish.aaas.finance.trade.pay.repositories.mysql;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TrdPayFlowRepository extends PagingAndSortingRepository<TrdPayFlow, Long> {
  public static final String FIND_SERIAL_ORDERINFO = "SELECT tpf.id, tpf.apply_serial, "
      + "tpf.outside_orderno, order_detail_id FROM trd_pay_flow tpf WHERE tpf.trd_status = 2 or "
      + "tpf.trd_status = 7 ORDER BY tpf.create_date ";
  public static final String COUNT_SERIAL_ORDERINFO = "SELECT count(1) FROM trd_pay_flow tpf "
      + "WHERE tpf.trd_status = 2 OR tpf.trd_status = 7 ";


  @Query(value = FIND_SERIAL_ORDERINFO, countQuery = COUNT_SERIAL_ORDERINFO, nativeQuery = true)
  Page<Object[]> findSerialOrderinfo( Pageable pageable);


  @Override
  TrdPayFlow save(TrdPayFlow trdPayFlow);

  List<TrdPayFlow> findAllByTrdTypeIsAndTrdStatusIs(int trdType, int trdStatus);
  List<TrdPayFlow> findAllByTradeConfirmShareIsAndTrdTypeIs(Long tradeConfirmShare,  int trdType);



  List<TrdPayFlow> findAllByUserProdId(Long userProdId);

//  List<TrdPayFlow> findAllByOrderDetailId(Long orderDetailId);

  List<TrdPayFlow> findAllByOutsideOrderno(String outsideOrderno);

  List<TrdPayFlow> findAllByApplySerial(String applySerial);

}
