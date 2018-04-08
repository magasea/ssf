package com.shellshellfish.aaas.oeminfo.service.impl;

import com.shellshellfish.aaas.oeminfo.service.UserService;
import java.util.List;

import com.shellshellfish.aaas.oeminfo.repositories.UserRepository;
import com.shellshellfish.aaas.oeminfo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	public User findByName(String name) {
		return userRepository.findByName(name);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(User user){
		saveUser(user);
	}

	@Override
	public void deleteUserById(Long id) {
		delete(id);
	}

	public void delete(Long id){
		userRepository.deleteById(id);
	}

	public void deleteAllUsers(){
		userRepository.deleteAll();
	}

	public List<User> findAllUsers(){
		return userRepository.findAll();
	}

	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}

}
