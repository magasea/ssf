package com.shellshellfish.aaas.userinfo.repositories.mysql;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import org.springframework.data.repository.query.Param;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public interface UiProductRepo extends PagingAndSortingRepository<UiProducts, Long> {

	@Override
	UiProducts save(UiProducts uiProducts);

	UiProducts findByProdId(long prodId);
	
	UiProducts findById(long id);
	


	List<UiProducts> findByUserId(Long userId);

	List<UiProducts> findAll();

	@Modifying
	@Query("UPDATE UiProducts SET update_by = :updateBy, update_date = :updateDate, "
			+ "update_by = :updateBy WHERE id = :userProdId")
	int updateUiProductsById(@Param("updateBy") Long updateBy, @Param("updateDate") Long
			updateDate, @Param("userProdId") Long userProdId);

}
