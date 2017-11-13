package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import java.math.BigInteger;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoAssetsRepository extends CrudRepository<UiAsset, BigInteger> {

  UiAsset findByUserId(BigInteger userId);

}
