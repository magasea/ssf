package com.shellshellfish.aaas.common.dao.repositories;

import com.shellshellfish.aaas.common.model.dao.userinfo.UserInfoDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoDao, Long>{

  UserInfoDao findById(Long id);

}
