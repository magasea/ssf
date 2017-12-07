package com.shellshellfish.aaas.account.service;

import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;

public interface RedisService {
    boolean doSmsVerification(VerificationBodyDTO vbody);
    boolean saveVeribody(VerificationBodyDTO vbody);
    boolean doPwdSave(String cellphone, String passwordhash);
	void doLogout(String cellphone, String password);
	String doGetPwd(String cellphone, String passwordhash);
}
