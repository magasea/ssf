package com.shellshellfish.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shellshellfish.account.service.AccountService;
import com.shellshellfish.account.service.AccountServiceImpl;
import com.shellshellfish.account.service.ResourceManager;
import com.shellshellfish.account.service.SchemaManager;

@Configuration
public class AppConfig {
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
	
}
