package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserAsset;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoBankCards;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoAssetsRepository extends CrudRepository<UiAsset, Long> {

  UiAsset findByUserId(Long userId);

}
