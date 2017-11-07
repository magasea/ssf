package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.dao.userinfo.User;
import java.util.List;

public interface UserService {
	
	User findById(Long id);



	void saveUser(User user);

	void updateUser(User user);

	void deleteUserById(Long id);

	void deleteAllUsers();

	List<User> findAllUsers();

	boolean isUserExist(User user);
}