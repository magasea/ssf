package com.shellshellfish.account.repositories;

import com.shellshellfish.account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
