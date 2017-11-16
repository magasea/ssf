package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiCompanyInfo;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoCompanyInfoRepository extends PagingAndSortingRepository<UiCompanyInfo,
    Long> {

  @Override
  UiCompanyInfo findOne(Long id);

  List<UiCompanyInfo> findAll();
}
