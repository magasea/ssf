package com.shellshellfish.aaas.oeminfo.repositories;

import com.shellshellfish.aaas.oeminfo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}
