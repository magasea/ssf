package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import java.math.BigInteger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoAssetsRepository extends PagingAndSortingRepository<UiAsset, Long> {

  UiAsset findByUserId(Long userId);

}
