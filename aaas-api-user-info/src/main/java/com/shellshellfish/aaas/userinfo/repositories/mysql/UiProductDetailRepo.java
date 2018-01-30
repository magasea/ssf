package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */
public interface UiProductDetailRepo extends PagingAndSortingRepository<UiProductDetail, Long> {

  @Override
  UiProductDetail save(UiProductDetail uiProductDetail);

  List<UiProductDetail> findAllByUserProdIdIs(Long userProdId);

  UiProductDetail findByUserProdIdAndFundCode(Long userProdId, String fundCode);

  @Modifying
  @Transactional
  @Query("UPDATE UiProductDetail SET fund_quantity = :fundQuantity, fund_quantity_trade = "
      + ":fundQuantityTrade, update_date = :updateDate, "
      + "update_by = :updateBy, status = :status  WHERE user_prod_id = :userProdId and fund_code "
      + "= :fundCode")
  int updateByParam(@Param("fundQuantity") Long fundQuantity,@Param("fundQuantityTrade") Long
      fundQuantityTrade, @Param("updateDate") Long updateDate, @Param("updateBy") Long updateBy,
      @Param("userProdId") Long userProdId, @Param("fundCode") String fundCode, @Param("status") int status);



  @Modifying
  @Transactional
  @Query("UPDATE UiProductDetail SET fund_quantity = fund_quantity - :fundQuantity, update_date = "
      + ":updateDate, "
      + "update_by = :updateBy, status = :status  WHERE user_prod_id = :userProdId and fund_code "
      + "= :fundCode")
  int updateByReedemConfirm(@Param("fundQuantity") Long fundQuantity, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("userProdId") Long userProdId, @Param
      ("fundCode") String fundCode, @Param("status") int status);

  List<UiProductDetail> findAllByUserProdId(Long userProdId);



  @Modifying
  @Query("UPDATE UiProductDetail SET fund_share = :fundShare, update_date = :updateDate, "
      + "update_by = :updateBy WHERE user_prod_id = :userProdId and fund_code = :fundCode")
  int updateFundShareByParam(@Param("fundShare") Integer fundShare, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("userProdId") Long userProdId, @Param
      ("fundCode") String fundCode);


  @Modifying
  @Transactional
  @Query("UPDATE UiProductDetail SET fund_quantity_trade = fund_quantity_trade + "
      + ":fundQuantityTrade, update_date = :updateDate, update_by = :updateBy, status = :status  "
      + "WHERE user_prod_id = :userProdId and fund_code = :fundCode")
  int updateByAddBackQuantity(@Param("fundQuantityTrade") Long fundQuantityTrade, @Param
      ("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("userProdId") Long userProdId, @Param
      ("fundCode") String fundCode, @Param("status") int status);

  @Modifying
  @Transactional
  @Query("UPDATE UiProductDetail SET update_date = :updateDate, "
      + "update_by = :updateBy, status = :status  WHERE user_prod_id = :userProdId and fund_code "
      + "= :fundCode")
  int updateByParamForStatus(@Param("updateDate") Long updateDate, @Param("updateBy") Long updateBy,
      @Param("userProdId") Long userProdId, @Param("fundCode") String fundCode, @Param("status") int status);


  @Modifying
  @Transactional
  @Query("UPDATE UiProductDetail SET fund_quantity_trade = "
      + ":fundQuantityTrade, update_date = :updateDate, "
      + "update_by = :updateBy, status = :status  WHERE user_prod_id = :userProdId and fund_code "
      + "= :fundCode")
  int updateByParamDeductTrade(@Param("fundQuantityTrade") Long
      fundQuantityTrade, @Param("updateDate") Long updateDate, @Param("updateBy") Long updateBy,
      @Param("userProdId") Long userProdId, @Param("fundCode") String fundCode, @Param("status") int status);
}
