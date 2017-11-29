package com.shellshellfish.aaas.account.repositories.mysql;

import java.util.List;

public interface SmsVerificationRepositoryCustom  {
	public List<Object[]> getSmsVerification(String cellphone,String verfiedcode);
}

