package com.shellshellfish.aaas.userinfo.repositories.mysql;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public interface UiProductRepo extends PagingAndSortingRepository<UiProducts, Long> {

	@Override
	UiProducts save(UiProducts uiProducts);

	UiProducts findByProdId(Long prodId);

	List<UiProducts> findByUserId(Long userId);

	List<UiProducts> findAll();
}
