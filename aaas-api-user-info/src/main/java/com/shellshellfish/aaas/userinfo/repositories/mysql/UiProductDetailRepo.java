package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */
public interface UiProductDetailRepo extends PagingAndSortingRepository<UiProductDetail, Long> {

  @Override
  UiProductDetail save(UiProductDetail uiProductDetail);
}
