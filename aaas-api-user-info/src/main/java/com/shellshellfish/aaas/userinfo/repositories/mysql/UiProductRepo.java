package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public interface UiProductRepo  extends PagingAndSortingRepository<UiProducts, Long> {

  @Override
  UiProducts save(UiProducts uiProducts);
}
