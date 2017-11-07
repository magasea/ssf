package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.UserBak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserBak, Long> {

    UserBak findByName(String name);

}
