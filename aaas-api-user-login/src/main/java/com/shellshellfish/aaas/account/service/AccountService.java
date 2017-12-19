package com.shellshellfish.aaas.account.service;

import java.util.List;

import com.shellshellfish.aaas.account.model.dao.User;
import com.shellshellfish.aaas.account.model.dto.LoginBodyDTO;
import com.shellshellfish.aaas.account.model.dto.PwdSettingBodyDTO;
import com.shellshellfish.aaas.account.model.dto.RegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UpdateRegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UserDTO;
import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;

public interface AccountService {
	List<User> isRegisteredUser(LoginBodyDTO loginBodyDTO);
	String isSettingPWD(PwdSettingBodyDTO pwdSettingBody);
	List<UserDTO> isRegisterredTel(RegistrationBodyDTO registrationBody);
	String isSmsVerified(UpdateRegistrationBodyDTO registrationBodyDTO);
	boolean addBankCard(String arg[]);
	String sendSmsMessage(String phone);
	boolean doSmsVerification(VerificationBodyDTO vbody);
	UserDTO doLogout(LoginBodyDTO loginBody);
	UserDTO getUserInfo(String uid) throws IllegalAccessException, InstantiationException;
}
