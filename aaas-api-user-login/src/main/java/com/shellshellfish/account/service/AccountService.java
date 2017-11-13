package com.shellshellfish.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shellshellfish.account.model.Account;
import com.shellshellfish.account.model.User;

public interface AccountService {
    boolean isRegisteredUser(String cellphone, String passwordhash);
	boolean isSettingPWD(String id, String pwdsetting, String pwdconfirm);
	boolean isRegisterredTel(String cellphone);
	boolean isSmsVerified(String cellphone,String verfiedcode);
	boolean addBankCard(String arg[]);
}
