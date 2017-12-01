package com.shellshellfish.aaas.account.service;

import com.shellshellfish.aaas.account.body.VerificationBody;

public interface RedisService {
    boolean doSmsVerification(VerificationBody vbody);
    boolean saveVeribody(VerificationBody vbody);
    boolean doPwdSave(String cellphone, String passwordhash);
	void doLogout(String cellphone, String verfiedcode);
	String doGetPwd(String cellphone, String passwordhash);
}
