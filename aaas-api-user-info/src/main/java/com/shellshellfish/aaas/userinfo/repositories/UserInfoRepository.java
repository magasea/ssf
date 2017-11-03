package com.shellshellfish.aaas.userinfo.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserInfoDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoDao, Long>{

  UserInfoDao findById(Long id);

}
