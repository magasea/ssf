package com.shellshellfish.aaas.account.repositories.mysql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.shellshellfish.aaas.account.model.dao.SmsVerification;

public class SmsVerificationRepositoryCustomImpl implements SmsVerificationRepositoryCustom {

	 @Autowired
	 @PersistenceContext
	 private EntityManager entityManager;
	
	 
	 @Override
	 public List<Object[]> getSmsVerification(String cellphone, String verfiedcode) {
		// TODO Auto-generated method stub
		 return entityManager
	                .createNativeQuery("select * from sms_verification "
	                		           +"where cell_phone='"+cellphone+"'"
	                		           +" and sms_code='"+verfiedcode+"'"
	                		           +" and expire_time> now()")
	                .getResultList();
		 
		 
	}

}
