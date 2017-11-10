package com.shellshellfish.account.repositories;

import com.shellshellfish.account.model.SmsVerification;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsVerificationRepositoryCustom  {
	public List<Object[]> getSmsVerification(String cellphone,String verfiedcode);
}

