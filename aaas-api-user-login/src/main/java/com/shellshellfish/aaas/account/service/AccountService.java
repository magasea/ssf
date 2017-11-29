package com.shellshellfish.aaas.account.service;

import java.util.List;

import com.shellshellfish.aaas.account.body.VerificationBody;
import com.shellshellfish.aaas.account.model.dto.UserDTO;

public interface AccountService {
	List<UserDTO> isRegisteredUser(String cellphone, String passwordhash);
	boolean isSettingPWD(String id, String pwdsetting, String pwdconfirm);
	List<UserDTO> isRegisterredTel(String cellphone);
	boolean isSmsVerified(String cellphone,String verfiedcode);
	boolean addBankCard(String arg[]);
	boolean sendSmsMessage(String phone);
	boolean doSmsVerification(VerificationBody vbody);
}
