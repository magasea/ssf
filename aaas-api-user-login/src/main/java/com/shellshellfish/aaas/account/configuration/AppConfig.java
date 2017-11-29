package com.shellshellfish.aaas.account.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.shellshellfish.aaas.account.repositories.mysql.SmsVerificationRepositoryCustom;
import com.shellshellfish.aaas.account.repositories.mysql.SmsVerificationRepositoryCustomImpl;
import com.shellshellfish.aaas.account.service.AccountService;
import com.shellshellfish.aaas.account.service.RedisService;
import com.shellshellfish.aaas.account.service.ResourceManager;
import com.shellshellfish.aaas.account.service.SchemaManager;
import com.shellshellfish.aaas.account.service.impl.AccountServiceImpl;
import com.shellshellfish.aaas.account.service.impl.RedisServiceimpl;

@Configuration
public class AppConfig {
	
	
	@Autowired
    Environment env;

	@Bean
	public AccountService accountService() {
		return new AccountServiceImpl();
	}
	
	@Bean
	public ResourceManager resourceManager() {
		return new ResourceManager();
	}
	
	@Bean
	public SchemaManager schemaManager() {
		return new SchemaManager();
	}
	
	@Bean
	public SmsVerificationRepositoryCustom SmsVerificationRepositoryCustomImpl() {
		return new SmsVerificationRepositoryCustomImpl();
	}
	
	@Bean
	public RedisService redisService() {
		return new RedisServiceimpl();
	}
	
	
}
