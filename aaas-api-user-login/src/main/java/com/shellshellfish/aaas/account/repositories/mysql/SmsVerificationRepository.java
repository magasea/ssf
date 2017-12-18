package com.shellshellfish.aaas.account.repositories.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shellshellfish.aaas.account.model.dao.SmsVerification;

public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long> {

	SmsVerification findByCellPhone(String cellPhone);
	List<SmsVerification> findByCellPhoneAndSmsCode(String cellPhone,String smsCode);
}
