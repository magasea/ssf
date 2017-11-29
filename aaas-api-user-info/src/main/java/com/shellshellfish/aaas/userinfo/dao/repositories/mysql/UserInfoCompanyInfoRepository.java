package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;

public interface UserInfoCompanyInfoRepository extends PagingAndSortingRepository<UiCompanyInfo,
    Long> {

  @Override
  UiCompanyInfo findOne(Long id);

  List<UiCompanyInfo> findAll();
}
