package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.UserBak;

import java.util.List;

public interface UserService {
	
	UserBak findById(Long id);

	UserBak findByName(String name);

	void saveUser(UserBak user);

	void updateUser(UserBak user);

	void deleteUserById(Long id);

	void deleteAllUsers();

	List<UserBak> findAllUsers();

	boolean isUserExist(UserBak user);
}