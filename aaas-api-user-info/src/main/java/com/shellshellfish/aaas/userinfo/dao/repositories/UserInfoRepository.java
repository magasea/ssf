package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Long>{

  User findById(Long id);

}
