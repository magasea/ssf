package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.UserBak;
import com.shellshellfish.aaas.userinfo.repositories.UserRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	public UserBak findById(Long id) {
		return userRepository.findOne(id);
	}

	public UserBak findByName(String name) {
		return userRepository.findByName(name);
	}

	public void saveUser(UserBak user) {
		userRepository.save(user);
	}

	public void updateUser(UserBak user){
		saveUser(user);
	}

	public void deleteUserById(Long id){
		userRepository.delete(id);
	}

	public void deleteAllUsers(){
		userRepository.deleteAll();
	}

	public List<UserBak> findAllUsers(){
		return userRepository.findAll();
	}

	public boolean isUserExist(UserBak user) {
		return findByName(user.getName()) != null;
	}

}
