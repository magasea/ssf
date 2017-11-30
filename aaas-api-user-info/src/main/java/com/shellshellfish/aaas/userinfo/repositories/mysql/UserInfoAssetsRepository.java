package com.shellshellfish.aaas.userinfo.repositories.mysql;

import java.math.BigInteger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;

public interface UserInfoAssetsRepository extends PagingAndSortingRepository<UiAsset, Long> {

  UiAsset findByUserId(Long userId);

}
