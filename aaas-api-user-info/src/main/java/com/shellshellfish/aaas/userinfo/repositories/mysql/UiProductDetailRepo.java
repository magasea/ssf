package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */
public interface UiProductDetailRepo extends PagingAndSortingRepository<UiProductDetail, Long> {

  @Override
  UiProductDetail save(UiProductDetail uiProductDetail);

  @Modifying
  @Query("UPDATE UiProductDetail SET  = :tradeApplySerial, order_status = "
      + ":orderDetailStatus ,  update_date = :updateDate, update_by = :updateBy WHERE id = :id")
  int updateByParam(@Param("tradeApplySerial") String tradeApplySerial, @Param("updateDate") Long
      updateDate, @Param("updateBy") Long updateBy,  @Param("id") Long id, @Param
      ("orderDetailStatus") int orderDetailStatus);
}
