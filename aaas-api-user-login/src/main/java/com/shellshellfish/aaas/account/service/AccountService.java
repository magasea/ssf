package com.shellshellfish.aaas.account.service;

import java.util.List;

import com.shellshellfish.aaas.account.model.dao.User;
import com.shellshellfish.aaas.account.model.dto.UserDTO;
import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;

public interface AccountService {
	List<User> isRegisteredUser(String cellphone, String passwordhash);
	boolean isSettingPWD(String id, String pwdsetting, String pwdconfirm);
	List<UserDTO> isRegisterredTel(String cellphone);
	boolean isSmsVerified(String cellphone,String verfiedcode);
	boolean addBankCard(String arg[]);
	boolean sendSmsMessage(String phone);
	boolean doSmsVerification(VerificationBodyDTO vbody);
	UserDTO doLogout(String cellphone,String verfiedcode);
}
