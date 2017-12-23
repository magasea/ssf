package com.shellshellfish.aaas.account.repositories.mysql;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shellshellfish.aaas.account.model.dao.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByCellPhoneAndPasswordHash(String cellphone, String passwordhash);
	List<User> findByCellPhone(String cellphone);
	List<User> findById(long parseLong);
	User findByUuid(String uuid);
}
